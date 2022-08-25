package com.cnettech.restapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnettech.restapi.common.UDPClient;
import com.cnettech.restapi.model.RecordRequest;
import com.cnettech.restapi.model.ReturnValue;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/record")
public class Process {
    
    @Value("${UDP.server}")
    private List<String> servers;

    @Value("${UDP.port_REC3}")
    private int port_REC3;    
    @Value("${UDP.port_REC7}")
    private int port_REC7;    
    @Value("${UDP.port_REC8}")
    private int port_REC8;        

    @Operation(summary = "선택녹취 시작", description = "선택녹취를 시작합니다.")
    @PutMapping(value = "/start", consumes = {"application/json"}, produces = {"application/json"})
    public ReturnValue RecordStart(@RequestBody RecordRequest jsonObj) {
        


        // http://녹취서버IP/interface/rec_part_proc.jsp?REC=REC7&DN=1000&USERID=HSI12345&USERNM=홍길동&PHONE=01012345678&KEY=202205091415232008&DATA1=&DATA2=&DATA3=&DATA4=&DATA5=&DATA6=&DATA7=&DATA10=20220509141912222052781
        // try {
        //     log.info(jsonObj.DN);
        //     log.info(jsonObj.USERID);
        //     log.info(jsonObj.USERNM);
        //     log.info(jsonObj.PHONE);
        //     log.info(jsonObj.KEY);
        //     log.info(jsonObj.DATA1);
        //     log.info(jsonObj.DATA2);
        //     log.info(jsonObj.DATA3);
        //     log.info(jsonObj.DATA4);
        //     log.info(jsonObj.DATA5);
        //     log.info(jsonObj.DATA6);
        //     log.info(jsonObj.DATA7);
        //     log.info(jsonObj.DATA8);
        //     log.info(jsonObj.DATA9);
        //     log.info(jsonObj.DATA10);
            
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        for (String serverip : servers) {
            UDPClient.SendMsg(serverip,port_REC7,"REC7",jsonObj);
        }

        ReturnValue returnValue = new ReturnValue();
        returnValue.Error_Code = 0;
        returnValue.Error_Description = "";
        return returnValue;
    }

    @Operation(summary = "선택녹취 종료", description = "선택녹취를 종료합니다.")
    @PutMapping(value = "/stop", consumes = {"application/json"}, produces = {"application/json"})
    public ReturnValue RecordStop(@RequestBody RecordRequest jsonObj) {
        // try {
        //     log.info(jsonObj.DN);
        //     log.info(jsonObj.USERID);
        //     log.info(jsonObj.USERNM);
        //     log.info(jsonObj.PHONE);
        //     log.info(jsonObj.KEY);
        //     log.info(jsonObj.DATA1);
        //     log.info(jsonObj.DATA2);
        //     log.info(jsonObj.DATA3);
        //     log.info(jsonObj.DATA4);
        //     log.info(jsonObj.DATA5);
        //     log.info(jsonObj.DATA6);
        //     log.info(jsonObj.DATA7);
        //     log.info(jsonObj.DATA8);
        //     log.info(jsonObj.DATA9);
        //     log.info(jsonObj.DATA10);
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        for (String serverip : servers) {
            UDPClient.SendMsg(serverip,port_REC8,"REC8",jsonObj);
        }
        
        ReturnValue returnValue = new ReturnValue();
        returnValue.Error_Code = 0;
        returnValue.Error_Description = "";
        return returnValue;
    }

    @Operation(summary = "녹취 후처리", description = "후처리 내용을 저장합니다.")
    @PutMapping(value = "/post", consumes = {"application/json"}, produces = {"application/json"})
    public ReturnValue RecordPost(@RequestBody RecordRequest jsonObj) {
        // try {
        //     log.info(jsonObj.DN);
        //     log.info(jsonObj.USERID);
        //     log.info(jsonObj.USERNM);
        //     log.info(jsonObj.PHONE);
        //     log.info(jsonObj.KEY);
        //     log.info(jsonObj.DATA1);
        //     log.info(jsonObj.DATA2);
        //     log.info(jsonObj.DATA3);
        //     log.info(jsonObj.DATA4);
        //     log.info(jsonObj.DATA5);
        //     log.info(jsonObj.DATA6);
        //     log.info(jsonObj.DATA7);
        //     log.info(jsonObj.DATA8);
        //     log.info(jsonObj.DATA9);
        //     log.info(jsonObj.DATA10);
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        for (String serverip : servers) {
            UDPClient.SendMsg(serverip,port_REC3,"REC3",jsonObj);
        }
        
        ReturnValue returnValue = new ReturnValue();
        returnValue.Error_Code = 0;
        returnValue.Error_Description = "";
        return returnValue;
    }

}
