package com.cnettech.restapi.controller;

import com.cnet.CnetCrypto.CNCrypto;
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

    @Value("${MEDIA.Storage_Backup}")
    private String Storage_Backup;

    @Value("${MEDIA.Temp_Path}")
    private String TempPath;

    @Value("${FFT.Image_Width}")
    public int width;
    @Value("${FFT.Image_Height}")
    public int height;
    @Value("${MEDIA.Sox}")
    public String Sox_Path;

    public WaveProcess waveProcess;

    private String DecodeRef(String refer) {
        String ReturnValue = "";
        try {
            CNCrypto aes = new CNCrypto("AES","!@CNET#$");
            ReturnValue = aes.Decrypt(refer);
            return ReturnValue;
        }catch (Exception e) {

        }

        try {
            String tmp_decode = URLDecoder.decode(refer,"UTF-8");
            CNCrypto aes = new CNCrypto("AES","!@CNET#$");
            ReturnValue = aes.Decrypt(tmp_decode);
            return ReturnValue;
        }catch (Exception e) {

        }
        return ReturnValue;
    }

    private String EncodeRef(String refer) {
        String ReturnValue = "";
        try {
            CNCrypto aes = new CNCrypto("AES","!@CNET#$");
            ReturnValue = aes.Encrypt(refer);
            return ReturnValue;
        }catch (Exception e) {

        }
        return ReturnValue;
    }

    /**
     *
     * @param refer 요청 내용
     * @return 대상 파일명 리턴
     */
    public String ProcessMsg(String refer) {

        String tmp;
        log.info("ProcessMsg refer : " + refer);
        try {
            if(!refer.contains("|")) {
                tmp = DecodeRef(refer);
            } else {
                tmp = refer;
            }
            log.info("Decrypt Value : " + tmp);
        } catch (Exception e) {
            log.error("CNCrypto.Decrypt Error : " + refer);
            e.printStackTrace();
            return "";
        }

        String[] Request = tmp.split("\\|");

        // 파일 경로
        String filepath = Request[2];
        // 임시 파일명 세팅
        String tmpFilename = "";
        // 원본 파일명 세팅
        String SourceFilename = "";

        /*
        // 롯데 홈쇼핑 관련 소스
        if (!Request[0].equals("01")) {
            try {
                String SendUrl = "http://10.144.31.30:8888/refer=" + EncodeRef(Request[0] + "|" + Request[1] + "|" + Request[2].replace(".mp3", "").replace(".wav", "").replace(".fft", "")) + ".fft";
                URL url = new URL(SendUrl);
                //url.openConnection();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
                    for (String line; (line = reader.readLine()) != null; ) {
                        //System.out.println(line);
                        break;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {
            }
            String[] tempFile = filepath.split("\\/");
            tmpFilename = (Storage_Default + File.separator + "Migration" + File.separator + "temp" + File.separator + tempFile[2]).replace("/",File.separator).replace(File.separator + File.separator, File.separator)
            SourceFilename = tmpFilename
                    .replace(".mp3",".wav")
                    .replace(".jpg",".wav");
        } else {
            tmpFilename = (Storage_Default + File.separator + "Record" + File.separator + filepath).replace("/",File.separator).replace(File.separator + File.separator, File.separator)
            SourceFilename = tmpFilename
                    .replace(".mp3",".wav")
                    .replace(".jpg",".wav");
        }

        log.info(String.format("Main Storage Find File = %s", SourceFilename));
        if (!LibFile.FileExist(SourceFilename)) {
            // 최초 자료가 없을 경우
            // 백업에서 조회한다
            log.info(String.format("Backup Storage Find File = %s", SourceFilename));
            tmpFilename = (Storage_Backup + File.separator + "Record" + File.separator + filepath).replace("/", File.separator).replace(File.separator + File.separator, File.separator);
            // 최초 자료가 없을 경우
            // 백업에서 조회한다
            SourceFilename = tmpFilename
                    .replace(".mp3", ".wav")
                    .replace(".jpg", ".wav");
        }
        // 롯데 홈쇼핑 관련 소스 종료
        */
        tmpFilename = (Storage_Default + File.separator + filepath).replace("/",File.separator).replace(File.separator + File.separator, File.separator);
        SourceFilename = tmpFilename
                .replace(".mp3",".wav")
                .replace(".jpg",".wav");
        log.info(String.format("Main Storage Find File = %s", SourceFilename));
        if (!LibFile.FileExist(SourceFilename)) {
            tmpFilename = (Storage_Backup + File.separator + filepath).replace("/",File.separator).replace(File.separator + File.separator, File.separator);
            // 최초 자료가 없을 경우
            // 백업에서 조회한다
            SourceFilename = tmpFilename
                    .replace(".mp3", ".wav")
                    .replace(".jpg", ".wav");
            log.info(String.format("Backup Storage Find File = %s", SourceFilename));
        }


        // 대상 파일명 세팅
        String TargetFilename = TempPath + Paths.get(tmpFilename).getFileName().toString();
        log.info(String.format("Storage File = %s, Target File = %s", SourceFilename, TargetFilename));

        // 원본 파일 존재 여부 확인
        if (!LibFile.FileExist(SourceFilename)) {
            log.error(String.format("ProcessMsg Origin File Not Found : %s" , SourceFilename));
            return "";
        }
        // 임시 폴더에 대상파일이 있는지 확인
        if (!LibFile.FileExist(TargetFilename)) {
            // 임시 폴더에 대상파일이 없음 -> 파일 신규 생성 (jpg, wav, mp3, pcm)
            try{
                // 임시 폴더가 없을경우 생성
                Files.createDirectories(Paths.get(TargetFilename).getParent());
                if(waveProcess == null) waveProcess = new WaveProcess(Sox_Path);
                String tmp_SourceFilename = waveProcess.WaveDecryption(SourceFilename, TargetFilename.replace(".mp3",".wav"));
                TargetFilename = waveProcess.WaveConvert(tmp_SourceFilename);
            } catch (Exception e) {
                log.error("WaveConvert Error : " + TargetFilename);
                e.printStackTrace();
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
            e.printStackTrace();
            return SetError("Error Loading File");
        }
    }

    @GetMapping("/fft")
    public ResponseEntity FFT(@RequestParam(defaultValue = "test") String refer) {
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
    public ResponseEntity FFT_Text(@RequestParam(defaultValue = "test") String refer) {
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


    @GetMapping("/download")
    public ResponseEntity Download(@RequestParam(defaultValue = "test") String refer) {
        log.info("Download : refer = " + refer);
        String TargetFile = ProcessMsg(refer);

        if (TargetFile.equals("")) return SetError("Error Reading File");

        try {
            log.info(String.format("Download TargetFile : %s", TargetFile));
            File file = new File(TargetFile);

            InputStreamResource resource = new InputStreamResource(Files.newInputStream(file.toPath()));
            HttpHeaders downloadHeader = SetHeader();
            downloadHeader.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            return ResponseEntity.ok()
                    .headers(downloadHeader)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }


//    @GetMapping("/convert")
    public void Convert(@RequestParam(defaultValue = "test") String ref) {
//        localhost:8080/media/wave/?ref=test.mp3
    }


//    @GetMapping("/ffttest")
    public void ffttest(@RequestParam(defaultValue = "test") String ref) {

    }

}
