package com.cnettech.restapi;

import com.cnettech.restapi.common.WaveProcess;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestapiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void readtest() {
		WaveProcess waveProcess = new WaveProcess("test");
		waveProcess.CheckRiff("E:\\Project_Java\\recorddata\\20220820\\02\\20220820024707_19906.tx");
		waveProcess.CheckRiff("E:\\Project_Java\\recorddata\\20220820\\02\\20220820024707_19906.wav");
		waveProcess.KeyValue("20220820024707_19906.wav");
		waveProcess.KeyValue("20210101010101_19906.wav");
	}
}
