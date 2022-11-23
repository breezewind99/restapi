package com.cnettech.restapi.common;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;

import net.sourceforge.lame.lowlevel.LameEncoder;
import net.sourceforge.lame.mp3.Lame;
import net.sourceforge.lame.mp3.MPEGMode;

public class MP3 {

    private static final boolean USE_VARIABLE_BITRATE = false;
    private static final int GOOD_QUALITY_BITRATE = 128;

    private static final String MP3_MAGIC_NUMBER = "FF FB";

    private static final int MP3_TRACK_LENGTH = 198477;

    public void ConvertWaveToMp3(String Source, String Target) throws IOException, UnsupportedAudioFileException {
        InputStream wavFileInputStream = new FileInputStream(Source);
        InputStream bufferedIn = new BufferedInputStream(wavFileInputStream);
        AudioInputStream wavTestFileAudioInputStream = AudioSystem.getAudioInputStream(bufferedIn);

        byte[] mp3Bytes = encodeToMp3(wavTestFileAudioInputStream);
        wavTestFileAudioInputStream.close();
        bufferedIn.close();
        wavFileInputStream.close();

        writeToFile(mp3Bytes, Target); // Just for the records...
    }

    protected void writeToFile(byte[] bytes, String filename) throws IOException {
        try (FileOutputStream stream = new FileOutputStream(filename)) {
            stream.write(bytes);
        }
    }
    //---- Helper methods

    /**
     * The actual encoding method to be used in your own project.
     */
    private byte[] encodeToMp3(AudioInputStream audioInputStream) throws IOException {
        LameEncoder encoder = new LameEncoder(audioInputStream.getFormat(), GOOD_QUALITY_BITRATE, MPEGMode.MONO, Lame.QUALITY_MIDDLE_LOW, USE_VARIABLE_BITRATE);

        ByteArrayOutputStream mp3 = new ByteArrayOutputStream();
        byte[] inputBuffer = new byte[encoder.getPCMBufferSize()];
        byte[] outputBuffer = new byte[encoder.getPCMBufferSize()];

        int bytesRead;
        int bytesWritten;

        while(0 < (bytesRead = audioInputStream.read(inputBuffer))) {
            bytesWritten = encoder.encodeBuffer(inputBuffer, 0, bytesRead, outputBuffer);
            mp3.write(outputBuffer, 0, bytesWritten);
        }

        encoder.close();
        return mp3.toByteArray();
    }
}
