package com.cnettech.restapi.common;

import com.cnettech.restapi.common.jna.SslEncryption;
import com.sun.jna.Native;
import ie.corballis.sox.SoXEncoding;
import ie.corballis.sox.Sox;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Slf4j
public class WaveProcess {

    public String Sox_Path;

    public WaveProcess(String sox_path) {
        Sox_Path = sox_path;
    }


    /**
     *
     * @param Source_File 원본파일
     * @param Target_File 대상파일
     * @return 대상 파일
     */
    public String WaveDecryption(String Source_File, String Target_File) {
        int ResultLib;
        if (Source_File.equals("") || Target_File.equals("")) {
            log.error(String.format("Error WaveDecryption Source : %s, Target : %s", Source_File, Target_File));
            return "";
        }
        try {
            log.info(String.format("WaveDecryption CheckRiff : %s", Source_File));

            // 파일이 없으면 복사
            if (!LibFile.FileExist(Target_File)) {
                File file = new File(Source_File);
                File newFile = new File(Target_File);
                try {
                    Files.copy(file.toPath(), newFile.toPath(), REPLACE_EXISTING);
                } catch (Exception e) {
                    log.error(String.format("Error WaveDecryption FileCopy Source : %s, Target : %s", Source_File, Target_File));
                    //e.printStackTrace();
                    return "";
                }
            }

            // 대상 파일의 복호화 처리
            if (CheckRiff(Target_File)) return Target_File;
            String[] sValue = new String[] {
                    "02B317631220B55742F5712B5E025B0ED3027CD58E423AFBF8ABEAAA2716BCE1",
                    "B3D00127CB4A695935D34FEB30EC76699FAFDA58A3E9B966254C5E9533090854",
                    "C614C96E7B2BB970BF9DE92A1A0FD98203339870B4E741C04528FA5A1FFFBAC5",
                    "C23896E69FED3B1DC03E989C6013E9D14D1B3DE27D0397001783986D05C5A9D5",
                    "39A2E9B3F8BF3A0452CA8072173EAAF0021F81A83247944E6C0B7BDA3ABC1294",
                    "8029C5982387CD5986FEB3210C73E37F6EB8E133BE2BDDA1A0C20DCBB511FC80",
                    "A1F17FDDCE72385C1E37604F037B9FFD0487656C756247693B5A0AF326E4F29A",
                    "8208237F66FE73DB3E368429053E47AF8F3B05B0BB837195C364014C6D2B8152",
                    "E4230B9DECE5879D7304BFC6F8F85314BB6B9226E504F95A2BC3D2E3363838A1",
                    "35FD24B172BC38B1B9278D1A89ADD04F913CD627F6C2DEB94900EA2218BC1933"
            };
            File file = new File(Source_File);
            String fileName = file.getName();
            int KeyValue = Integer.parseInt(KeyValue(fileName));

            final SslEncryption eSslEncryption= Native.load("cnetssl", SslEncryption.class);
            ResultLib = eSslEncryption.AES_Init_SSL("", 0, "25800450", 1);
            log.info("AES_Init_SSL Result : " + ResultLib);
            log.info("AES_Dec_SSL_V2 Check KeyValue : " + KeyValue);
            ResultLib = eSslEncryption.AES_Dec_SSL_V2(Source_File, Target_File, sValue[KeyValue], sValue[KeyValue].length(), 1, 0);
            log.info("AES_Dec_SSL_V2 File : " + Source_File + ", Result : " + ResultLib);
            KeyValue = 0;
            while(!CheckRiff(Target_File)) {
                log.info("AES_Dec_SSL_V2 Check KeyValue : " + KeyValue);
                ResultLib = eSslEncryption.AES_Dec_SSL_V2(Source_File, Target_File, sValue[KeyValue], sValue[KeyValue].length(), 1, 0);
                log.info("AES_Dec_SSL_V2 File : " + Source_File + ", Result : " + ResultLib);
                KeyValue = KeyValue + 1;
                if (KeyValue > 9) {
                    break;
                }
            }
        } catch (Exception e) {
            return "";
        }
        return Target_File;
    }

    public boolean CheckRiff(String filename) throws HandledException {
        try {
            byte[] buffer = new byte[4];
            InputStream is = Files.newInputStream(Paths.get(filename));
            Integer readByte = is.read(buffer,0,4);
            String coverted = new String(buffer);
            is.close();
            log.info("Check File RIFF : " + filename + ", Result : " + coverted);
            if ("RIFF".equals(coverted)) {
                return true;
            }
        } catch (Exception e){
            log.error("CheckRiff Error : " + filename);
            throw new HandledException("CheckRiff","CheckRiff Exception", e);
        }
        return false;
    }


    public String KeyValue(String filename) {
        try {
            String str = filename.substring(0,14);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = format.parse(str);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            log.info("KeyValue : " + filename + ", Result : " + filename.charAt(3));
            return filename.substring(3, 4);
        } catch (ParseException e) {
            log.error("KeyValue Error : " + filename);
        }
        return "0";
    }

    public String WaveConvert(String Source_File) {

        // 대상 및 원본 파일명이 없는지 여부 체크
        if (Source_File.equals("")) {
            log.error(String.format("Error WaveConvert Source : %s", Source_File));
            return "";
        }
        log.info(String.format("WaveConvert Start Source : %s", Source_File));

        // MP3로 요청이 올경우 작업처리
        String tmp_Source_File = Source_File.replace(".mp3",".wav");
        String tmp_PCM_File = tmp_Source_File.replace(".wav",".pcm.wav");
        String tmp_JPG_File = tmp_Source_File.replace(".wav", ".jpg");
        String tmp_TXT_File = tmp_Source_File.replace(".wav", ".txt");
        String tmp_MP3_File = tmp_Source_File.replace(".wav", ".mp3");
        if (tmp_Source_File.equals("")) return "";

        Sox sox_pcm = new Sox(Sox_Path);
        if (!LibFile.FileExist(tmp_PCM_File)) {
            try {
                log.info(String.format("WaveConvert PCM Make Source : %s, Target : %s", tmp_Source_File, tmp_PCM_File));
                // Gsm To Pcm
                sox_pcm
                        .sampleRate(8000)
                        .inputFile(tmp_Source_File)
                        .encoding(SoXEncoding.SIGNED_INTEGER)
                        .bits(16)
                        .outputFile(tmp_PCM_File)
                        .execute();
            } catch (Exception e) {
                log.error(String.format("Error WaveConvert PCM Make Source : %s, Target : %s", tmp_Source_File, tmp_PCM_File));
                return "";
            }
        }
        // FFT jpg이미지 생성
        if (!LibFile.FileExist(tmp_JPG_File)) {
            log.info(String.format("WaveConvert MakeImage Make Source : %s, Target : %s" ,tmp_PCM_File, tmp_JPG_File));
            FFTImage.MakeImage(tmp_PCM_File, tmp_JPG_File);
        }

        // FFT txt파일 생성
        if (!LibFile.FileExist(tmp_TXT_File)) {
            log.info(String.format("WaveConvert MakeFFT Make Source : %s, Target : %s", tmp_Source_File, tmp_TXT_File));
            FFTImage.MakeFFT(tmp_PCM_File, tmp_TXT_File);
        }
        if (!LibFile.FileExist(tmp_MP3_File)) {
            try {
                log.info(String.format("WaveConvert MP3 Make Source : %s, Target : %s", tmp_PCM_File, tmp_MP3_File));
                // Gsm To MP3
                MP3 mp3 = new MP3();
                mp3.ConvertWaveToMp3(tmp_PCM_File, tmp_MP3_File);
            } catch (Exception e) {
                log.error(String.format("Error MP3 Make Source : %s, Target : %s", tmp_PCM_File, tmp_MP3_File));
                return "";
            }
        }
        return tmp_MP3_File;
    }
}
