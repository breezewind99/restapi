package com.cnettech.restapi.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/media")
public class Media {

    @GetMapping("/wave")
    public ResponseEntity<InputStreamResource> Wave(@RequestParam(defaultValue = "test") String ref) throws IOException {
//        localhost:8080/?fName=test.mp3
        System.out.println("fName = " + ref);
        // 파일 경로
        String file_Path = "E:\\temp\\";
        String path = file_Path + File.separator + ref;
        // 파일 존재 유무
        boolean fExist = Files.exists(Paths.get(path));
        if (fExist) {
            File file = new File(path);
            String fileName = file.getName();
            // 파일 확장자
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            HttpHeaders header = new HttpHeaders();
            Path fPath = Paths.get(file.getAbsolutePath());

            //header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");

            // 대용량일 경우 resource3을 사용해야함
//	        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(fPath ));
//	        Resource resouce2 = resourceLoader.getResource(path);
            InputStreamResource resource3 = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    //.contentType(MediaType.parseMediaType("application/octet-stream"))
                    .contentType(MediaType.parseMediaType("audio/mpeg"))
                    .body(resource3);
        }
        return null;
    }
}
