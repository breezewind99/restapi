package com.cnettech.restapi.controller;

import com.cnet.CnetCrypto.CNCrypto;
import com.cnettech.restapi.common.FFTImage;
import com.cnettech.restapi.common.WaveProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

@Slf4j
@RestController
@RequestMapping("/media")
public class Media {

    @Value("${MEDIA.Storage_Default}")
    private String Storage_Default;
    @Value("${MEDIA.Temp_Path}")
    private String TempPath;

    @Value("${FFT.Image_Width}")
    public int width;
    @Value("${FFT.Image_Height}")
    public int height;
    @Value("${MEDIA.Sox}")
    public String Sox_Path;

    public WaveProcess waveProcess;

    @GetMapping("/wave")
    public ResponseEntity<InputStreamResource> Wave(@RequestParam(defaultValue = "test") String refer) throws IOException {
//        localhost:8080/media/wave/?ref=test.mp3
        //waveProcess = new WaveProcess(Sox_Path, width, height);

        log.info("Wave : refer = " + refer);
        String tmp = "";
        try {
            if(!refer.contains("\\|")) {
                CNCrypto aes = new CNCrypto("AES","!@CNET#$");
                tmp = aes.Decrypt(refer);
            } else {
                tmp = refer;
            }
            System.out.println("Convert Valuue : " + tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String temp[] = tmp.split("\\|");

        // 파일 경로
        String filepath = temp[2];
        String path = (Storage_Default + File.separator + filepath).replace("/",File.separator).replace(File.separator + File.separator,File.separator);
        System.out.println("path = " + path);
        String TargetFile = TempPath + Paths.get(path).getFileName().toString();
        boolean OriginExist = Files.exists(Paths.get(path));
        boolean TargetExist = Files.exists(Paths.get(TargetFile));
        if (!TargetExist) {
            // 임시 폴더에 대상 파일이 없는경우 컨버팅 한다.
            Files.createDirectories(Paths.get(TargetFile).getParent());
            if(waveProcess == null) waveProcess = new WaveProcess(Sox_Path, width, height);
            waveProcess.WaveConvert(path, TargetFile);
        }

        try {
            File file = new File(TargetFile);
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
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/fft")
    public ResponseEntity<InputStreamResource> fft(@RequestParam(defaultValue = "test") String refer) throws IOException {
//        localhost:8080/media/wave/?ref=test.mp3

        System.out.println("ref = " + refer);
        String tmp = "";
        try {
            if(!refer.contains("|")) {
                CNCrypto aes = new CNCrypto("AES","!@CNET#$");
                tmp = aes.Decrypt(refer);
            } else {
                tmp = refer;
            }
            System.out.println("Convert Valuue : " + tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String temp[] = tmp.split("\\|");

        // 파일 경로
        String filepath = temp[2];
        String path = (Storage_Default + File.separator + filepath).replace("/",File.separator).replace(File.separator + File.separator,File.separator);
        System.out.println("path = " + path);
        String TargetFile = TempPath + Paths.get(path).getFileName().toString();
        boolean OriginExist = Files.exists(Paths.get(path));
        boolean TargetExist = Files.exists(Paths.get(TargetFile));
        if (!TargetExist) {
            Files.createDirectories(Paths.get(TargetFile).getParent());
            // 임시 폴더에 대상 파일이 없는경우 컨버팅 한다.
            if(waveProcess == null) waveProcess = new WaveProcess(Sox_Path, width, height);
            waveProcess.WaveConvert(path, TargetFile);
        }

        try {
            File file = new File(TargetFile.replace(".wav",".jpg"));
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
                    .contentType(MediaType.parseMediaType("image/jpg"))
                    .body(resource3);
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/decrypt")
    public void Decrypt(@RequestParam(defaultValue = "test") String refer) throws IOException {
//        localhost:8080/media/wave/?ref=test.mp3
        log.info("Decrypt : refer = " + refer);
        log.debug("Decrypt : refer = " + refer);
        log.error("Decrypt : refer = " + refer);
        System.out.println("ref = " + refer);
        String temp[] = refer.split("\\|");
        // 파일 경로
        String filepath = temp[2];
        String path = (Storage_Default + File.separator + filepath).replace("/",File.separator).replace(File.separator + File.separator,File.separator);

        log.info("Decrypt : path = " + path);
        String path_Dec = path + ".dec";
        if(waveProcess == null) waveProcess = new WaveProcess(Sox_Path, width, height);
        waveProcess.WaveDecryption(path, path_Dec);
    }

    @GetMapping("/convert")
    public void Convert(@RequestParam(defaultValue = "test") String ref) throws IOException {
//        localhost:8080/media/wave/?ref=test.mp3
        System.out.println("ref = " + ref);
        String temp[] = ref.split("\\|");
        // 파일 경로
        String filepath = temp[2];
        String path = (filepath).replace("/",File.separator).replace(File.separator + File.separator,File.separator);
        System.out.println("path = " + path);
        if(waveProcess == null) waveProcess = new WaveProcess(Sox_Path, width, height);
        waveProcess.WaveConvert(path, path);
    }

    @GetMapping("/decryptstring")
    public void decryptstring(@RequestParam(defaultValue = "test") String ref) {
        try {
            CNCrypto aes = new CNCrypto("AES","!@CNET#$");
            String returnValue = aes.Decrypt(ref);
            System.out.println("Convert Valuue : " + returnValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/ffttest")
    public void ffttest(@RequestParam(defaultValue = "test") String ref) {
        try {

            System.out.println("ref = " + ref);
            String temp[] = ref.split("\\|");
            // 파일 경로
            String filepath = temp[2];
            String path = (filepath).replace("/",File.separator).replace(File.separator + File.separator,File.separator);
            System.out.println("path = " + path);
            //WaveProcess.MakeImage(path.replace(".wav",".pcm.wav"), path.replace(".wav",".jpg"));
            if(waveProcess == null) waveProcess = new WaveProcess(Sox_Path, width, height);
            waveProcess.WaveConvert(path,path);
            FFTImage.MakeImage(path.replace(".wav",".pcm.wav"), path.replace(".wav",".jpg"));
            FFTImage.MakeFFT(path.replace(".wav",".pcm.wav"), path.replace(".wav","2.jpg"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
