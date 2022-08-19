package com.cnettech.restapi.common;

import com.cnettech.restapi.common.jna.SslEncryption;
import com.sun.jna.Native;
import ie.corballis.sox.SoXEffect;
import ie.corballis.sox.SoXEncoding;
import ie.corballis.sox.Sox;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class WaveProcess {

    public String Sox_Path;

    public WaveProcess(String sox_path, int width, int height) {
        Sox_Path = sox_path;
    }

    public void WaveDecryption(String Source_File, String Target_File) {
        int ResultLib = 0;
//        String sValue = "02B317631220B55742F5712B5E025B0ED3027CD58E423AFBF8ABEAAA2716BCE1";
        String skey = "7912A53404EA2165E842A0228159E89557F0A8C1717F88CEC04B7F42F077DDE6";
        String sValue = "02B317631220B55742F5712B5E025B0ED3027CD58E423AFBF8ABEAAA2716BCE1";
        final SslEncryption eSslEncryption= (SslEncryption) Native.load("cnetssl", SslEncryption.class);
        ResultLib = eSslEncryption.AES_Init_SSL(skey, skey.length(), "25800450", 1);
        ResultLib = eSslEncryption.AES_Dec_SSL_V2(Source_File, Target_File, sValue, sValue.length(), 1, 0);
        System.out.println("OUT" + ResultLib);
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
