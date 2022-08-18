package com.cnettech.restapi.common.jna;

import com.sun.jna.Library;

public interface SslEncryption extends Library{
    public int AES_Dec_SSL_V2(String mBaseFile, String mDescFile, String Key ,int KeyLen, int select, int key_type);
    public int AES_Init_SSL(String Key ,int KeyLen, String Ps, int key_type);
}
