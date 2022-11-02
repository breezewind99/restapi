package com.cnettech.restapi.controller;

import com.cnettech.restapi.common.UDPClient;
import com.cnettech.restapi.model.RecordRequest;
import com.cnettech.restapi.model.ReturnValue;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@RequestMapping("/record")
public class Process {
    
    @Value("${UDP.server}")
    private List<String> servers;

    @Value("${UDP.port_REC1:5021}")
    private int port_REC1;
    @Value("${UDP.port_REC2:5021}")
    private int port_REC2;
    @Value("${UDP.port_REC3:5021}")
    private int port_REC3;    
    @Value("${UDP.port_REC7:5021}")
    private int port_REC7;
    @Value("${UDP.port_REC8:5021}")
    private int port_REC8;
    @Value("${UDP.port_REC9:5021}")
    private int port_REC9;
    @Value("${UDP.port_IVR1:5021}")
    private int port_IVR1;
    @Value("${UDP.port_IVR2:5021}")
    private int port_IVR2;

    @Operation(summary = "전수 녹취 시작", description = "전수녹취를 시작합니다.")
    @PutMapping(value = "/record/start", consumes = {"application/json"}, produces = {"application/json"})
    public ReturnValue RecordStart(@RequestBody RecordRequest jsonObj) {
        ReturnValue returnValue = new ReturnValue();
        try {
            for (String serverip : servers) {
                UDPClient.SendMsg(serverip, port_REC1,"REC1", jsonObj);
            }
            returnValue.Error_Code = 0;
            returnValue.Error_Description = "";
        } catch (Exception e) {
            returnValue.Error_Code = -1;
            returnValue.Error_Description = e.getMessage();
        }
        return returnValue;
    }


    @Operation(summary = "전수녹취 종료", description = "전수녹취를 종료 합니다.")
    @PutMapping(value = "/record/stop", consumes = {"application/json"}, produces = {"application/json"})
    public ReturnValue RecordStop(@RequestBody RecordRequest jsonObj) {
        ReturnValue returnValue = new ReturnValue();
        try {
            for (String serverip : servers) {
                UDPClient.SendMsg(serverip, port_REC2, "REC2", jsonObj);
            }
            returnValue.Error_Code = 0;
            returnValue.Error_Description = "";
        } catch (Exception e) {
            returnValue.Error_Code = -1;
            returnValue.Error_Description = e.getMessage();
        }
        return returnValue;
    }

    @Operation(summary = "녹취 후처리", description = "녹취 후처리 내용을 저장합니다.")
    @PutMapping(value = "/record/post", consumes = {"application/json"}, produces = {"application/json"})
    public ReturnValue RecordPost(@RequestBody RecordRequest jsonObj) {
        ReturnValue returnValue = new ReturnValue();
        try {
            for (String serverip : servers) {
                UDPClient.SendMsg(serverip, port_REC3,"REC3", jsonObj);
            }
            returnValue.Error_Code = 0;
            returnValue.Error_Description = "";
        } catch (Exception e) {
            returnValue.Error_Code = -1;
            returnValue.Error_Description = e.getMessage();
        }
        return returnValue;

    }

    @Operation(summary = "선택녹취 시작", description = "선택 녹취를 시작합니다.")
    @PutMapping(value = "/select/start", consumes = {"application/json"}, produces = {"application/json"})
    public ReturnValue RecordSelStart(@RequestBody RecordRequest jsonObj) {
        ReturnValue returnValue = new ReturnValue();
        try {
            for (String serverip : servers) {
                UDPClient.SendMsg(serverip, port_REC7, "REC7", jsonObj);
            }

            returnValue.Error_Code = 0;
            returnValue.Error_Description = "";
        } catch (Exception e) {
            returnValue.Error_Code = -1;
            returnValue.Error_Description = e.getMessage();
        }
        return returnValue;
    }


    @Operation(summary = "선택녹취 종료", description = "선택녹취를 종료합니다.")
    @PutMapping(value = "/select/stop", consumes = {"application/json"}, produces = {"application/json"})
    public ReturnValue RecordSelStop(@RequestBody RecordRequest jsonObj) {
        ReturnValue returnValue = new ReturnValue();
        try {
            for (String serverip : servers) {
                UDPClient.SendMsg(serverip, port_REC8, "REC8", jsonObj);
            }

            returnValue.Error_Code = 0;
            returnValue.Error_Description = "";
        } catch (Exception e) {
            returnValue.Error_Code = -1;
            returnValue.Error_Description = e.getMessage();
        }
        return returnValue;
    }

    @Operation(summary = "선택녹취 취소", description = "선택녹취를 취소합니다.")
    @PutMapping(value = "/select/cancel", consumes = {"application/json"}, produces = {"application/json"})
    public ReturnValue RecordSelCancel(@RequestBody RecordRequest jsonObj) {
        ReturnValue returnValue = new ReturnValue();
        try {
            for (String serverip : servers) {
                UDPClient.SendMsg(serverip, port_REC9,"REC9", jsonObj);
            }
            returnValue.Error_Code = 0;
            returnValue.Error_Description = "";
            return returnValue;
        } catch (Exception e) {
            returnValue.Error_Code = -1;
            returnValue.Error_Description = e.getMessage();
            return returnValue;
        }
    }

    @Operation(summary = "IVR 녹취 시작", description = "IVR녹취를 시작합니다.")
    @PutMapping(value = "/ivr/start", consumes = {"application/json"}, produces = {"application/json"})
    public ReturnValue RecordIvrStart(@RequestBody RecordRequest jsonObj) {

        ReturnValue returnValue = new ReturnValue();
        try {
            for (String serverip : servers) {
                UDPClient.SendMsg(serverip, port_IVR1,"IVR1", jsonObj);
            }

            returnValue.Error_Code = 0;
            returnValue.Error_Description = "";
            return returnValue;

        } catch (Exception e) {
            returnValue.Error_Code = -1;
            returnValue.Error_Description = e.getMessage();
            return returnValue;
        }

    }

    @Operation(summary = "IVR 녹취 종료", description = "IVR녹취를 종료합니다.")
    @PutMapping(value = "/ivr/stop", consumes = {"application/json"}, produces = {"application/json"})
    public ReturnValue RecordIvrStop(@RequestBody RecordRequest jsonObj) {
        ReturnValue returnValue = new ReturnValue();
        try {
            for (String serverip : servers) {
                UDPClient.SendMsg(serverip, port_IVR2,"IVR2", jsonObj);
            }

            returnValue.Error_Code = 0;
            returnValue.Error_Description = "";
            return returnValue;
        } catch (Exception e) {
            returnValue.Error_Code = -1;
            returnValue.Error_Description = e.getMessage();
            return returnValue;
        }
    }
}
