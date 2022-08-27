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
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@CrossOrigin(origins = "*")
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

    public String ProcessMsg(String refer) {

        String tmp;
        try {
            if(!refer.contains("|")) {
                String UrlDecode = URLDecoder.decode(refer,"UTF-8");
                CNCrypto aes = new CNCrypto("AES","!@CNET#$");
                tmp = aes.Decrypt(UrlDecode);
            } else {
                tmp = refer;
            }
            log.info("Decrypt Value : " + tmp);
        } catch (UnsupportedEncodingException e) {
            log.error("UrlDecode Error : " + refer);
            //e.printStackTrace();
            return "";
        } catch (Exception e) {
            log.error("CNCrypto.Decrypt Error : " + refer);
            //e.printStackTrace();
            return "";
        }

        String[] temp = tmp.split("\\|");

        // 파일 경로
        String filepath = temp[2];
        String path = (Storage_Default + File.separator + filepath).replace("/",File.separator).replace(File.separator + File.separator,File.separator);
        log.info("Storage Path = " + path);
        String TargetFile = TempPath + Paths.get(path).getFileName().toString();
        boolean TargetExist = Files.exists(Paths.get(TargetFile));
        if (!TargetExist) {
            // 임시 폴더에 대상 파일이 없는경우 컨버팅 한다.
            try{
                Files.createDirectories(Paths.get(TargetFile).getParent());
                if(waveProcess == null) waveProcess = new WaveProcess(Sox_Path);
                TargetFile = waveProcess.WaveConvert(path, TargetFile);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        return TargetFile;
    }

    public HttpHeaders SetHeader() {
        HttpHeaders header = new HttpHeaders();
        header.add("Access-Control-Allow-Headers","origin, x-requested-with, content-type, accept");
//            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
//            header.add("Pragma", "no-cache");
        header.add("Connection", "Close");
        header.add("Server", "Cnet Media WebServer");
        header.add("Accept-Ranges", "bytes");
//            header.add("Expires", "0");
        return header;
    }

    public ResponseEntity SetError(String ErrorString) {
        return ResponseEntity.ok()
                .headers(SetHeader())
                .contentLength(ErrorString.length())
                .contentType(MediaType.parseMediaType("text/plain"))
                .body(ErrorString);
    }

    @GetMapping("/wave")
    public ResponseEntity Wave(@RequestParam(defaultValue = "test") String refer) {
        log.info("Wave : refer = " + refer);
        String TargetFile = ProcessMsg(refer);

        if (TargetFile.equals("")) {
            return SetError("Error Reading File");
        }

        try {
            log.info("Wave : TargetFile = " + TargetFile);
            File file = new File(TargetFile);


            // 대용량일 경우 resource3을 사용해야함
//	        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(fPath ));
//	        Resource resouce2 = resourceLoader.getResource(path);
            InputStreamResource resource3 = new InputStreamResource(Files.newInputStream(file.toPath()));

            return ResponseEntity.ok()
                    .headers(SetHeader())
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("audio/wav"))
                    .body(resource3);
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/mp3")
    public ResponseEntity<InputStreamResource> MP3(@RequestHeader Map<String, String> headers, @RequestParam(defaultValue = "test") String refer) {

        log.info("MP3 : refer = " + refer);
        log.info("Header : refer = " + headers);
        String TargetFile = ProcessMsg(refer);

        try {
            log.info("MP3 : TargetFile = " + TargetFile.replace(".wav",".mp3"));
            File file = new File(TargetFile.replace(".wav",".mp3"));

            HttpHeaders header = new HttpHeaders();
            header.add("Access-Control-Allow-Headers","origin, x-requested-with, content-type, accept");
//            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
//            header.add("Pragma", "no-cache");
            header.add("Connection", "Close");
            header.add("Server", "Cnet Media WebServer");
            header.add("Accept-Ranges", "bytes");
//            header.add("Expires", "0");

            // 대용량일 경우 resource3을 사용해야함
//	        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(fPath ));
//	        Resource resouce2 = resourceLoader.getResource(path);
            InputStreamResource resource3 = new InputStreamResource(Files.newInputStream(file.toPath()));

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
    public ResponseEntity<InputStreamResource> fft(@RequestParam(defaultValue = "test") String refer) {
        log.info("FFT : refer = " + refer);
        String TargetFile = ProcessMsg(refer);

        try {
            log.info("FFT : TargetFile = " + TargetFile.replace(".wav",".jpg"));
            File file = new File(TargetFile.replace(".wav",".jpg"));

            HttpHeaders header = new HttpHeaders();
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");

            // 대용량일 경우 resource3을 사용해야함
//	        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(fPath ));
//	        Resource resouce2 = resourceLoader.getResource(path);
            InputStreamResource resource3 = new InputStreamResource(Files.newInputStream(file.toPath()));

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
    public void Decrypt(@RequestParam(defaultValue = "test") String refer) {
//        localhost:8080/media/wave/?ref=test.mp3
        log.info("Decrypt : refer = " + refer);
        log.debug("Decrypt : refer = " + refer);
        log.error("Decrypt : refer = " + refer);
        log.info("ref = " + refer);
        String[] temp = refer.split("\\|");
        // 파일 경로
        String filepath = temp[2];
        String path = (Storage_Default + File.separator + filepath).replace("/",File.separator).replace(File.separator + File.separator,File.separator);

        log.info("Decrypt : path = " + path);
        String path_Dec = path + ".dec";
        if(waveProcess == null) waveProcess = new WaveProcess(Sox_Path);
        waveProcess.WaveDecryption(path, path_Dec);
    }

    @GetMapping("/convert")
    public void Convert(@RequestParam(defaultValue = "test") String ref) {
//        localhost:8080/media/wave/?ref=test.mp3
        log.info("ref = " + ref);
        String[] temp = ref.split("\\|");
        // 파일 경로
        String filepath = temp[2];
        String path = (filepath).replace("/",File.separator).replace(File.separator + File.separator,File.separator);
        log.info("path = " + path);
        if(waveProcess == null) waveProcess = new WaveProcess(Sox_Path);
        waveProcess.WaveConvert(path, path);
    }

    @GetMapping("/decryptstring")
    public void decryptstring(@RequestParam(defaultValue = "test") String ref) {
        try {
            CNCrypto aes = new CNCrypto("AES","!@CNET#$");
            String returnValue = aes.Decrypt(ref);
            log.info("Convert Valuue : " + returnValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/ffttest")
    public void ffttest(@RequestParam(defaultValue = "test") String ref) {
        try {

            log.info("ref = " + ref);
            String[] temp = ref.split("\\|");
            // 파일 경로
            String filepath = temp[2];
            String path = (filepath).replace("/",File.separator).replace(File.separator + File.separator,File.separator);
            log.info("path = " + path);
            //WaveProcess.MakeImage(path.replace(".wav",".pcm.wav"), path.replace(".wav",".jpg"));
            if(waveProcess == null) waveProcess = new WaveProcess(Sox_Path);
            waveProcess.WaveConvert(path,path);
            FFTImage.MakeImage(path.replace(".wav",".pcm.wav"), path.replace(".wav",".jpg"));
            FFTImage.MakeFFT(path.replace(".wav",".pcm.wav"), path.replace(".wav","2.jpg"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/ffttext")
    public ResponseEntity<InputStreamResource> ffttext(@RequestParam(defaultValue = "test") String refer) throws IOException {
//        localhost:8080/media/wave/?ref=test.mp3

        log.info("ffttext ref = " + refer);
        String tmp = "";
        try {
            if(!refer.contains("|")) {
                CNCrypto aes = new CNCrypto("AES","!@CNET#$");
                tmp = aes.Decrypt(refer);
            } else {
                tmp = refer;
            }
            log.info("Convert Value : " + tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] temp = tmp.split("\\|");

        // 파일 경로
        String filepath = temp[2];
        String path = (Storage_Default + File.separator + filepath).replace("/",File.separator).replace(File.separator + File.separator,File.separator);
        log.info("path = " + path);
        String TargetFile = TempPath + Paths.get(path).getFileName().toString();
//        boolean OriginExist = Files.exists(Paths.get(path));
        boolean TargetExist = Files.exists(Paths.get(TargetFile));
        if (!TargetExist) {
            Files.createDirectories(Paths.get(TargetFile).getParent());
            // 임시 폴더에 대상 파일이 없는경우 컨버팅 한다.
            if(waveProcess == null) waveProcess = new WaveProcess(Sox_Path);
            waveProcess.WaveConvert(path, TargetFile);
        }

        try {
            log.info("FFT 요청 파일명 : " + TargetFile.replace(".mp3",".wav").replace(".wav",".txt"));
            File file = new File(TargetFile.replace(".mp3",".wav").replace(".wav",".txt"));
//            String fileName = file.getName();
            // 파일 확장자
//            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
//            Path fPath = Paths.get(file.getAbsolutePath());

            HttpHeaders header = new HttpHeaders();
            //header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");

            // 대용량일 경우 resource3을 사용해야함
//	        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(fPath ));
//	        Resource resouce2 = resourceLoader.getResource(path);
            InputStreamResource resource3 = new InputStreamResource(Files.newInputStream(file.toPath()));

            return ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    //.contentType(MediaType.parseMediaType("application/octet-stream"))
                    .contentType(MediaType.parseMediaType("text/plain"))
                    .body(resource3);
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
