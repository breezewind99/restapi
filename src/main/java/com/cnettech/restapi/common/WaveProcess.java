package com.cnettech.restapi.common;

import com.cnettech.restapi.common.jna.SslEncryption;
import com.sun.jna.Native;

public class WaveProcess {

    public void WaveDecryption(String Source_File, String Target_File) {
        int ResultLib = 0;
        String sValue = "02B317631220B55742F5712B5E025B0ED3027CD58E423AFBF8ABEAAA2716BCE1";
        final SslEncryption eSslEncryption= (SslEncryption) Native.load("cnetssl", SslEncryption.class);
        ResultLib = eSslEncryption.AES_Init_SSL("", 16, "25800450", 1);
        ResultLib = eSslEncryption.AES_Dec_SSL_V2(Source_File, Target_File,sValue, sValue.length(), 1, 0);
    }
    
}
