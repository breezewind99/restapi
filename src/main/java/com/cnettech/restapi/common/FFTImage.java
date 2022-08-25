package com.cnettech.restapi.common;

import lombok.extern.slf4j.Slf4j;

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
import java.util.ArrayList;

@Slf4j
public class FFTImage {
    public static int width = 534;
    public static int height = 80;
    public static int mid = height / 2;

    public static boolean MakeImage(String Wavfilepath, String Imagefilepath)
    {
        log.info("FFT 이미지 생성 파일 Src : " + Wavfilepath + ", Tar Img : " + Imagefilepath);
        AudioInputStream audio_input_stream = null;

        try {
            File audio_file = new File(Wavfilepath);
            audio_input_stream = AudioSystem.getAudioInputStream(audio_file);

            int 	bytesRead;
            int 	samplesPerPixel = 598;
            int 	bytesPerSample =  (audio_input_stream.getFormat().getSampleSizeInBits() / 8) * audio_input_stream.getFormat().getChannels();
            byte[]  waveData = new byte[samplesPerPixel * bytesPerSample + 1];

		   /*
     	   log.info("---------------------------------------------------");
           log.info("Size:			" + audio_file.length());
           log.info("Number of channels:	" + audio_input_stream.getFormat().getChannels());
	       log.info("Sampling rate:		" + audio_input_stream.getFormat().getSampleRate());
	       log.info("Bit depth:		" + audio_input_stream.getFormat().getSampleSizeInBits());
	       log.info("bytesPerSample:		" + bytesPerSample);
           log.info("---------------------------------------------------");
           */

            ArrayList<Double> FFTData = new ArrayList<>();

            while(true) {
                bytesRead = audio_input_stream.read(waveData, 0, samplesPerPixel * bytesPerSample);
                if (bytesRead <= 0) break;

                int 	LoopI;
                short 	low = 0;
                short 	high = 0;

                for(LoopI = 0; LoopI < bytesRead; LoopI += 2){
                    short sample = (short) (((waveData[LoopI + 1] & 0xFF) << 8) | (waveData[LoopI] & 0xFF));
                    if(sample < low) low = sample;
                    if(sample > high) high = sample;
                }

                double lowPercent = ((double)low - Short.MIN_VALUE) / 0xFFFF;
                double fftValue = (1 - lowPercent - 0.5) * 2;

                //log.info("data: " + low + " : " + lowPercent + " : " + String.format("%.4f", fftValue));

                FFTData.add(Double.parseDouble(String.format("%.4f", fftValue)));
            }
            audio_input_stream.close();

            //Image ����
            return CreateImage(FFTData, Imagefilepath);
        }
        catch(UnsupportedAudioFileException | IOException ef) {
            ef.printStackTrace();
        }

        if(audio_input_stream != null) {
            try {
                audio_input_stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static boolean CreateImage(ArrayList<Double> FFTData, String Imagefilepath)
    {
        try
        {
            BufferedImage img = new BufferedImage(width, height, 1);

            Graphics2D g = img.createGraphics();

            g.setColor(new Color(23, 26, 32));
            g.fillRect(0, 0, width, height);

            ArrayList<Double> ImageData = new ArrayList<>();

            int i; int before; int after;
            double sfactor; double atpoint; double tmp;

            sfactor = (double)(FFTData.size() - 1) / (width - 1);

            ImageData.add(FFTData.get(0));
            i = 1;
            while (i < width - 1) {
                tmp = i * sfactor;
                before = (int)Math.floor(tmp);
                after = (int)Math.ceil(tmp);
                atpoint = tmp - before;

                ImageData.add(Double.valueOf(((Double)FFTData.get(before)).doubleValue() + (((Double)FFTData.get(after)).doubleValue() - ((Double)FFTData.get(before)).doubleValue()) * atpoint));
                i++;
            }
            ImageData.add((Double)FFTData.get(FFTData.size() - 1));

            int w = width / ImageData.size();

            g.setColor(new Color(50, 255, 255));

            for (int j = 0; j < ImageData.size(); j++) {
                Rectangle2D rect = new Rectangle2D.Double(w * j, mid - mid * ((Double)ImageData.get(j)).doubleValue(), w, mid * ((Double)ImageData.get(j)).doubleValue() * 2.0D);
                g.draw(rect);
            }

            ImageIO.write(img, "jpeg", new File(Imagefilepath));

            return true;
        }
        catch (Exception e)
        {
            log.info("err");
        }

        return false;
    }

    public static boolean MakeFFT(String Wavfilepath, String Fftfilepath)
    {
        AudioInputStream audio_input_stream = null;

        try {
            File audio_file = new File(Wavfilepath);
            audio_input_stream = AudioSystem.getAudioInputStream(audio_file);

            int 	bytesRead;
            int 	samplesPerPixel = 598;
            int 	bytesPerSample =  (audio_input_stream.getFormat().getSampleSizeInBits() / 8) * audio_input_stream.getFormat().getChannels();
            byte[]  waveData = new byte[samplesPerPixel * bytesPerSample + 1];

            log.info("---------------------------------------------------");
            log.info("Size:			" + audio_file.length());
            log.info("Number of channels:	" + audio_input_stream.getFormat().getChannels());
            log.info("Sampling rate:		" + audio_input_stream.getFormat().getSampleRate());
            log.info("Bit depth:		" + audio_input_stream.getFormat().getSampleSizeInBits());
            log.info("bytesPerSample:		" + bytesPerSample);
            log.info("---------------------------------------------------");

            StringBuffer FftBuffer = new StringBuffer();

            //int i = 1;

            while(true) {
                bytesRead = audio_input_stream.read(waveData, 0, samplesPerPixel * bytesPerSample);
                if (bytesRead <= 0) break;

                int 	LoopI;
                short 	low = 0;
                short 	high = 0;

                for(LoopI = 0; LoopI < bytesRead; LoopI += 2){
                    short sample = (short) (((waveData[LoopI + 1] & 0xFF) << 8) | (waveData[LoopI] & 0xFF));
                    if(sample < low) low = sample;
                    if(sample > high) high = sample;
                }

                double lowPercent = ((double)low - Short.MIN_VALUE) / 0xFFFF;
                double fftValue = (1 - lowPercent - 0.5) * 2;

                //log.info("data: " + low + " : " + lowPercent + " : " + String.format("%.4f", fftValue));

                FftBuffer.append(String.format("%.4f", fftValue));
                FftBuffer.append(",");

                //if( i++ % 20 == 0) {
                //FftBuffer.append("\n");
                //}
            }
            audio_input_stream.close();

            FileWriter writer = new  FileWriter(Fftfilepath);
            writer. write(FftBuffer.toString());
            writer.close();

            return true;
        }
        catch(UnsupportedAudioFileException | IOException ef) {
            ef.printStackTrace();
        }

        if(audio_input_stream != null) {
            try {
                audio_input_stream.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        return false;
    }
}
