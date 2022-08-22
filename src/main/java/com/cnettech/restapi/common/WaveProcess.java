package com.cnettech.restapi.common;

import com.cnettech.restapi.common.jna.SslEncryption;
import com.sun.jna.Native;
import ie.corballis.sox.SoXEncoding;
import ie.corballis.sox.Sox;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Slf4j
public class WaveProcess {

    public String Sox_Path;

    public WaveProcess(String sox_path, int width, int height) {
        Sox_Path = sox_path;
    }

    public void WaveDecryption(String Source_File, String Target_File) {
        int ResultLib = 0;

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
//        String sValue = "02B317631220B55742F5712B5E025B0ED3027CD58E423AFBF8ABEAAA2716BCE1";
//        String sValue = "B3D00127CB4A695935D34FEB30EC76699FAFDA58A3E9B966254C5E9533090854";
//        String sValue = "C614C96E7B2BB970BF9DE92A1A0FD98203339870B4E741C04528FA5A1FFFBAC5";
//        String sValue = "C23896E69FED3B1DC03E989C6013E9D14D1B3DE27D0397001783986D05C5A9D5";
//        String sValue = "39A2E9B3F8BF3A0452CA8072173EAAF0021F81A83247944E6C0B7BDA3ABC1294";
//        String sValue = "8029C5982387CD5986FEB3210C73E37F6EB8E133BE2BDDA1A0C20DCBB511FC80";
//        String sValue = "A1F17FDDCE72385C1E37604F037B9FFD0487656C756247693B5A0AF326E4F29A";
//        String sValue = "8208237F66FE73DB3E368429053E47AF8F3B05B0BB837195C364014C6D2B8152";
//        String sValue = "E4230B9DECE5879D7304BFC6F8F85314BB6B9226E504F95A2BC3D2E3363838A1";
//        String sValue = "35FD24B172BC38B1B9278D1A89ADD04F913CD627F6C2DEB94900EA2218BC1933";

        final SslEncryption eSslEncryption= Native.load("cnetssl", SslEncryption.class);
        ResultLib = eSslEncryption.AES_Init_SSL("", 0, "25800450", 1);
        log.info("AES_Init_SSL Result : " + ResultLib);
        ResultLib = eSslEncryption.AES_Dec_SSL_V2(Source_File, Target_File, sValue[2], sValue[2].length(), 1, 0);
        log.info("AES_Dec_SSL_V2 File : " + Source_File + ", Result : " + ResultLib);
    }

    public boolean CheckRiff(String filename) {
        try {

            byte[] buffer = new byte[4];
            InputStream is = new FileInputStream(filename);
            is.read(buffer);
            String coverted = new String(buffer);
            is.close();
            log.info("Check File RIFF : " + filename + ", Result : " + coverted);
            if ("RIFF".equals(coverted)) {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
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
            int year = calendar.get(Calendar.YEAR);
            log.info("KeyValue : " + filename + ", Result : " + filename.substring(3, 4));
            return filename.substring(3, 4);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void WaveConvert(String Source_File, String Target_File) {
        System.out.println("Convert Source : " + Source_File + ", Target : " + Target_File);
        // MP3로 요청이 올경우 작업처리
        String source = Source_File.replace(".mp3",".wav");
        String target = Target_File.replace(".mp3",".wav");



        Sox sox_pcm = new Sox(Sox_Path);
        Sox sox_mp3 = new Sox(Sox_Path);
        try{


            System.out.println("Target : " + target.replace(".wav",".pcm.wav"));
            // Gsm To Pcm
            sox_pcm
                    .sampleRate(8000)
                    .inputFile(source)
                    .encoding(SoXEncoding.SIGNED_INTEGER)
                    .bits(16)
                    .outputFile(target.replace(".wav",".pcm.wav"))
                    .execute();
        } catch (Exception e){
            e.printStackTrace();
        }

        FFTImage.MakeImage(target.replace(".wav",".pcm.wav"), target.replace(".wav",".jpg"));

        try{
            System.out.println("Target : " + target.replace(".wav",".mp3"));
            // Gsm To MP3
            sox_mp3
                .sampleRate(8000)
                .inputFile(source)
                .argument("-t", "mp3")
                .outputFile(target.replace(".wav",".mp3"))
                .execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
