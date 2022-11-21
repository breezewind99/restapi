package com.cnettech.restapi.common;

import com.cnettech.restapi.model.RecordRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Objects;

@Slf4j
public class UDPClient {
    public static void SendMsg(String Target_IP, int Target_Port, String Mode, RecordRequest jsonObj) throws Exception {
            String msg;
            /*
            Linux용
                헤더 |날짜	|내선번호 |상담원ID |상담원명|전화번호     |IN/OUT     |UUID|데이터1|데이터2|데이터3|데이터4|데이터5|데이터6|데이터7|데이터8|데이터9|데이터10
                REC3|       |19906  |1111    |       |010985804   |          |2   |3     |4     |5     |6     |7     |8     |9     |10    |11    |12    |13|14|15|
            */
            msg = String.format("%s| |%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|",
                    Mode,
                    (Objects.isNull(jsonObj.DN)     ? " " : jsonObj.DN),
                    (Objects.isNull(jsonObj.USERID) ? " " : jsonObj.USERID),
                    (Objects.isNull(jsonObj.USERNM) ? " " : jsonObj.USERNM),
                    (Objects.isNull(jsonObj.PHONE)  ? " " : jsonObj.PHONE),
                    (Objects.isNull(jsonObj.INOUT)  ? " " : jsonObj.INOUT),
                    (Objects.isNull(jsonObj.KEY)    ? " " : jsonObj.KEY),
                    (Objects.isNull(jsonObj.DATA1)  ? " " : jsonObj.DATA1.equals("")  ? " " : jsonObj.DATA1),
                    (Objects.isNull(jsonObj.DATA2)  ? " " : jsonObj.DATA2.equals("")  ? " " : jsonObj.DATA2),
                    (Objects.isNull(jsonObj.DATA3)  ? " " : jsonObj.DATA3.equals("")  ? " " : jsonObj.DATA3),
                    (Objects.isNull(jsonObj.DATA4)  ? " " : jsonObj.DATA4.equals("")  ? " " : jsonObj.DATA4),
                    (Objects.isNull(jsonObj.DATA5)  ? " " : jsonObj.DATA5.equals("")  ? " " : jsonObj.DATA5),
                    (Objects.isNull(jsonObj.DATA6)  ? " " : jsonObj.DATA6.equals("")  ? " " : jsonObj.DATA6),
                    (Objects.isNull(jsonObj.DATA7)  ? " " : jsonObj.DATA7.equals("")  ? " " : jsonObj.DATA7),
                    (Objects.isNull(jsonObj.DATA8)  ? " " : jsonObj.DATA8.equals("")  ? " " : jsonObj.DATA8),
                    (Objects.isNull(jsonObj.DATA9)  ? " " : jsonObj.DATA9.equals("")  ? " " : jsonObj.DATA9),
                    (Objects.isNull(jsonObj.DATA10) ? " " : jsonObj.DATA10.equals("") ? " " : jsonObj.DATA10)
            );
            if (Objects.isNull(jsonObj.DN))
            {
                log.warn("[Send Fail] Server IP :" + Target_IP + ", Port : " + Target_Port);
                log.warn("[Send Fail] Send Msg : " + msg);
                log.warn("[Send Fail] 전송 불가 내선번호 필수값이 없음");
                throw new Exception("내선번호 필수값이 없음");
            }

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
    }
}
