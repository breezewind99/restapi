package com.cnettech.restapi.common;

import java.io.File;

/**
 * 파일처리 라이브러리
 */
public class LibFile {
    public static boolean FileExist(String FilePath) {
        File file = new File(FilePath);
        return file.exists();
    }
}
