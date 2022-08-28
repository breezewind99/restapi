package com.cnettech.restapi.controller;

import com.cnet.CnetCrypto.CNCrypto;
import com.cnettech.restapi.model.ReturnValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/encryption")
public class Encryption {

    @GetMapping(value = "/encrypt", produces = {"text/plain"})
    public String encryptstring(@RequestParam(defaultValue = "Normal String") String refer) {
        try {
            CNCrypto aes = new CNCrypto("AES","!@CNET#$");
            String ReturnValue = aes.Encrypt(refer);
            return ReturnValue;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value ="/decrypt", produces = {"text/plain"})
    public String decryptstring(@RequestParam(defaultValue = "Encryption String") String refer) {
        try {
            CNCrypto aes = new CNCrypto("AES","!@CNET#$");
            String ReturnValue = aes.Decrypt(refer);
            return ReturnValue;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
