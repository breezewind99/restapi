package com.cnettech.restapi.common;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cnettech.restapi.model.RecordRequest;

public class UDPClient {
    private final static Logger logger = LogManager.getLogger(UDPClient.class);
    public static void SendMsg(String Target_IP, int Target_Port, String Mode, RecordRequest jsonObj) {
        try {

            /* Debug
            System.out.println(jsonObj.DN);
            System.out.println(jsonObj.USERID);
            System.out.println(jsonObj.USERNM);
            System.out.println(jsonObj.PHONE);
            System.out.println(jsonObj.KEY);
            System.out.println(jsonObj.DATA1);
            System.out.println(jsonObj.DATA2);
            System.out.println(jsonObj.DATA3);
            System.out.println(jsonObj.DATA4);
            System.out.println(jsonObj.DATA5);
            System.out.println(jsonObj.DATA6);
            System.out.println(jsonObj.DATA7);
            System.out.println(jsonObj.DATA8);
            System.out.println(jsonObj.DATA9);
            System.out.println(jsonObj.DATA10);
             */
            String msg = "";
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

            logger.info("[Send Start] Server IP :" + Target_IP + ", Port : " + Target_Port);            
            // 전송할 수 있는 UDP 소켓 생성
            DatagramSocket dsoc = new DatagramSocket();
            
            // 받을 곳의 주소 생성
            InetAddress ia = InetAddress.getByName(Target_IP);
            
            // 전송할 데이터 생성
            DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.getBytes().length, ia, Target_Port);
            dsoc.send(dp);
            dsoc.close();

            logger.info("[Send Success] Send Msg : " + msg);            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.warn("[Send fail] Server IP :" + Target_IP + ", Send Msg : " + jsonObj);         
        }
    }
}
