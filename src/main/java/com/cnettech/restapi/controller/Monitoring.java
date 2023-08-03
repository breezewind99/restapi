package com.cnettech.restapi.controller;

import com.cnettech.restapi.model.MonitoringRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/monitoring")
public class Monitoring {

    @Autowired
    DataSource dataSource;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Autowired
    SqlSession sqlSession;

    @Value("${MONITORING.LIMIT.CPU}")
    private int monitoring_limit_cpu;
    @Value("${MONITORING.LIMIT.MEM}")
    private int monitoring_limit_mem;
    @Value("${MONITORING.LIMIT.HDD}")
    private int monitoring_limit_hdd;
    @PostMapping (value = "/check", consumes = {"application/json"}, produces = {"application/json"})
    public JSONObject check(@RequestBody MonitoringRequest jsonObj) {
        try {
//            System.out.println(jsonObj.SystemCode);
//            System.out.println(jsonObj.SystemName);
//            System.out.println(jsonObj.CPU);
//            System.out.println(jsonObj.MEM);
//            System.out.println(jsonObj.HDD);
//            System.out.println(jsonObj.PROCESS);

            updateMonitoring(jsonObj.SystemCode, String.valueOf(jsonObj.CPU), String.valueOf(jsonObj.MEM), jsonObj.HDD, jsonObj.PROCESS);

            String jsonText = "";
            if (!jsonObj.SystemCode.equals("")) {
                jsonText =  "{" +
                        "\"RESPONSE\": {" +
                        "\"HEADER\": {" +
                        "\"RESULT_CODE\":\"00\"," +
                        "\"RESULT\":\"S\"," +
                        "\"RESULT_MESSAGE\":\"서비스가 정상적으로 처리되었습니다.\"" +
                        "}" +
                        "}}";
            } else {
                jsonText =  "{" +
                        "\"RESPONSE\": {" +
                        "\"HEADER\": {" +
                        "\"RESULT_CODE\":\"00\"," +
                        "\"RESULT\":\"S\"," +
                        "\"RESULT_MESSAGE\":\"서비스가 정상적으로 처리되었습니다.\"" +
                        "}" +
                        "}}";
            }

            JSONParser parser = new JSONParser();
            JSONObject ReturnValue = null;
            try {
                ReturnValue = (JSONObject)parser.parse(jsonText);
            } catch (ParseException e) {
                System.out.println("변환에 실패");
                e.printStackTrace();
            }
            return ReturnValue;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateMonitoring(String SystemCode, String CPU, String MEM, String HDD, String PROCESS) {
        int temp_cpu = Integer.parseInt(CPU);
        int temp_mem = Integer.parseInt(MEM);
        String[] temp_hdds = HDD.split(",");
        String[] temp_processes = PROCESS.split(",");

        String temp_Not_Process = "";
        String temp_Not_Hdd = "";
        String temp_Error_Code = "";

        Map<String, Object> argMap = new HashMap<String, Object>();
        argMap.put("systemcode", SystemCode);
        argMap.put("cpu", CPU);
        argMap.put("mem", MEM);
        argMap.put("hdd", HDD);
        argMap.put("process", PROCESS);
        int tt = sqlSession.update("monitoring.updateMonitoring", argMap);

        for (String process : temp_processes) {
            if (process.charAt(0) == 'X') {
                if (! temp_Error_Code.contains("301")) {
                    temp_Error_Code += (temp_Error_Code.equals("") ? "" : ",") + "301";
                }
                temp_Not_Process += (temp_Not_Process.equals("") ? "" : ",") + process.substring(1);
            }
        }

        for (String hdd : temp_hdds) {
            String[] temp_hdds_detail = hdd.split(":");
            if (Integer.parseInt(temp_hdds_detail[1]) > monitoring_limit_hdd) {
                if (! temp_Error_Code.contains("302")) {
                    temp_Error_Code += (temp_Error_Code.equals("") ? "" : ",") + "302";
                }
                temp_Not_Hdd += (temp_Not_Hdd.equals("") ? "" : ",") + temp_hdds_detail[0];
            }
        }

        if (temp_cpu > monitoring_limit_cpu) {
            temp_Error_Code += (temp_Error_Code.equals("") ? "" : ",") + "303";
//            System.out.println("CPU Limit");
        }

        if (temp_mem > monitoring_limit_mem) {
            temp_Error_Code += (temp_Error_Code.equals("") ? "" : ",") + "304";
//            System.out.println("MEM Limit");
        }

        String[] Error_Codes = temp_Error_Code.split(",");
        for (String Error_Code : Error_Codes) {
            argMap.clear();
            argMap.put("Mon_System", SystemCode);
            argMap.put("Mon_Alram", Error_Code);
            if (Error_Code.equals("301") ) {
                argMap.put("Mon_Process", temp_Not_Process);
//                System.out.println("Mon_System : " + SystemCode + ", Mon_Alram : " + Error_Code + ", Mon_Process : " + temp_Not_Process);
            } else if (Error_Code.equals("302")) {
                argMap.put("Mon_Process", temp_Not_Hdd);
//                System.out.println("Mon_System : " + SystemCode + ", Mon_Alram : " + Error_Code + ", Mon_Process : " + temp_Not_Hdd);
            } else {
//                System.out.println("Mon_System : " + SystemCode + ", Mon_Alram : " + Error_Code);
            }
            sqlSession.insert("monitoring.insertMonitoring", argMap);
        }
    }

}
