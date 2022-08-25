package com.cnettech.restapi.common;

import com.cnettech.restapi.model.RecordRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
@Slf4j
public class UDPClient {
    public static void SendMsg(String Target_IP, int Target_Port, String Mode, RecordRequest jsonObj) {
        try {

            /* Debug
            log.info(jsonObj.DN);
            log.info(jsonObj.USERID);
            log.info(jsonObj.USERNM);
            log.info(jsonObj.PHONE);
            log.info(jsonObj.KEY);
            log.info(jsonObj.DATA1);
            log.info(jsonObj.DATA2);
            log.info(jsonObj.DATA3);
            log.info(jsonObj.DATA4);
            log.info(jsonObj.DATA5);
            log.info(jsonObj.DATA6);
            log.info(jsonObj.DATA7);
            log.info(jsonObj.DATA8);
            log.info(jsonObj.DATA9);
            log.info(jsonObj.DATA10);
             */
            String msg;
            msg = String.format("%s| |%s|%s|%s|%s|%s| | | |%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|", Mode, jsonObj.DN, jsonObj.USERID, jsonObj.USERNM, jsonObj.PHONE, jsonObj.PHONE, jsonObj.KEY,
            (jsonObj.DATA1.equals("") ? " " : jsonObj.DATA1),
            (jsonObj.DATA2.equals("") ? " " : jsonObj.DATA2),
            (jsonObj.DATA3.equals("") ? " " : jsonObj.DATA3),
            (jsonObj.DATA4.equals("") ? " " : jsonObj.DATA4),
            (jsonObj.DATA5.equals("") ? " " : jsonObj.DATA5),
            (jsonObj.DATA6.equals("") ? " " : jsonObj.DATA6),
            (jsonObj.DATA7.equals("") ? " " : jsonObj.DATA7),
            (jsonObj.DATA8.equals("") ? " " : jsonObj.DATA8),
            (jsonObj.DATA9.equals("") ? " " : jsonObj.DATA9),
            (jsonObj.DATA10.equals("") ? " " : jsonObj.DATA10)
            );

            log.info("[Send Start] Server IP :" + Target_IP + ", Port : " + Target_Port);
            // 전송할 수 있는 UDP 소켓 생성
            DatagramSocket dsoc = new DatagramSocket();
            
            // 받을 곳의 주소 생성
            InetAddress ia = InetAddress.getByName(Target_IP);
            
            // 전송할 데이터 생성
            DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.getBytes().length, ia, Target_Port);
            dsoc.send(dp);
            dsoc.close();

            log.info("[Send Success] Send Msg : " + msg);
        } catch (Exception e) {
            log.info(e.getMessage());
            log.warn("[Send fail] Server IP :" + Target_IP + ", Send Msg : " + jsonObj);
        }
    }
}
