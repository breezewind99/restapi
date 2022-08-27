package com.cnettech.restapi.controller;

import com.cnet.CnetCrypto.CNCrypto;
import com.cnettech.restapi.common.FFTImage;
import com.cnettech.restapi.common.LibFile;
import com.cnettech.restapi.common.WaveProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    /**
     *
     * @param refer 요청 내용
     * @return 대상 파일명 리턴
     */
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

        String[] Request = tmp.split("\\|");

        // 파일 경로
        String filepath = Request[2];

        // 원본 파일명 세팅
        String SourceFilename = (Storage_Default + File.separator + filepath).replace("/",File.separator).replace(File.separator + File.separator, File.separator);

        // 대상 파일명 세팅
        String TargetFilename = TempPath + Paths.get(SourceFilename).getFileName().toString();

        log.info(String.format("Storage File = %s, Target File = %s", SourceFilename, TargetFilename));

        // 임시 폴더에 대상파일이 있는지 확인
        if (!LibFile.FileExist(TargetFilename)) {
            // 임시 폴더에 대상파일이 없음 -> 파일 신규 생성 (jpg, wav, mp3, pcm)
            try{
                // 임시 폴더가 없을경우 생성
                Files.createDirectories(Paths.get(TargetFilename).getParent());
                if(waveProcess == null) waveProcess = new WaveProcess(Sox_Path);
                String tmp_SourceFilename = waveProcess.WaveDecryption(SourceFilename, TargetFilename);
                TargetFilename = waveProcess.WaveConvert(tmp_SourceFilename, TargetFilename);
            } catch (Exception e) {
                log.error("WaveConvert Error : " + refer);
                //e.printStackTrace();
                return "";
            }
        }
        return TargetFilename;
    }

    public HttpHeaders SetHeader() {
        HttpHeaders header = new HttpHeaders();
        header.add("Access-Control-Allow-Headers","origin, x-requested-with, content-type, accept");
        header.add("Connection", "Close");
        header.add("Server", "Cnet Media WebServer");
        header.add("Accept-Ranges", "bytes");
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

        if (TargetFile.equals("")) return SetError("Error Process File");


        try {
            log.info("Wave : TargetFile = " + TargetFile);
            File file = new File(TargetFile);

            InputStreamResource resource3 = new InputStreamResource(Files.newInputStream(file.toPath()));

            return ResponseEntity.ok()
                    .headers(SetHeader())
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("audio/wav"))
                    .body(resource3);
        } catch ( Exception e) {
            return SetError("Error Loading File");
        }
    }

    @GetMapping("/mp3")
    public ResponseEntity MP3(@RequestParam(defaultValue = "test") String refer) {

        log.info("MP3 : refer = " + refer);
        String TargetFile = ProcessMsg(refer);

        if (TargetFile.equals("")) return SetError("Error Process File");

        try {
            log.info("MP3 : TargetFile = " + TargetFile.replace(".wav",".mp3"));
            File file = new File(TargetFile.replace(".wav",".mp3"));

            InputStreamResource resource = new InputStreamResource(Files.newInputStream(file.toPath()));

            return ResponseEntity.ok()
                    .headers(SetHeader())
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("audio/mpeg"))
                    .body(resource);
        } catch ( Exception e) {
            return SetError("Error Loading File");
        }
    }

    @GetMapping("/fft")
    public ResponseEntity<InputStreamResource> FFT(@RequestParam(defaultValue = "test") String refer) {
        log.info("FFT : refer = " + refer);
        String TargetFile = ProcessMsg(refer);

        if (TargetFile.equals("")) return SetError("Error Process File");

        try {
            log.info("FFT : TargetFile = " + TargetFile.replace(".mp3",".wav").replace(".wav",".jpg"));
            File file = new File(TargetFile.replace(".mp3",".wav").replace(".wav",".jpg"));
            InputStreamResource resource = new InputStreamResource(Files.newInputStream(file.toPath()));

            return ResponseEntity.ok()
                    .headers(SetHeader())
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("image/jpg"))
                    .body(resource);
        } catch ( Exception e) {
            return SetError("Error Loading File");
        }
    }

    @GetMapping("/ffttext")
    public ResponseEntity<InputStreamResource> FFT_Text(@RequestParam(defaultValue = "test") String refer) {
        log.info("FFTTEXT : refer = " + refer);
        String TargetFile = ProcessMsg(refer);

        if (TargetFile.equals("")) return SetError("Error Reading File");

        try {
            log.info(String.format("FFTTEXT : TargetFile : %s", TargetFile.replace(".mp3",".wav").replace(".wav",".txt")));
            File file = new File(TargetFile.replace(".mp3",".wav").replace(".wav",".txt"));

            InputStreamResource resource = new InputStreamResource(Files.newInputStream(file.toPath()));

            return ResponseEntity.ok()
                    .headers(SetHeader())
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("text/plain"))
                    .body(resource);
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    @GetMapping("/decrypt")
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

//    @GetMapping("/convert")
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

//    @GetMapping("/decryptstring")
    public void decryptstring(@RequestParam(defaultValue = "test") String ref) {
        try {
            CNCrypto aes = new CNCrypto("AES","!@CNET#$");
            String returnValue = aes.Decrypt(ref);
            log.info("Convert Valuue : " + returnValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @GetMapping("/ffttest")
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

}
