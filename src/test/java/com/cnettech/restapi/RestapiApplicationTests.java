package com.cnettech.restapi;

import com.cnettech.restapi.common.MP3;
import com.cnettech.restapi.common.WaveProcess;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class RestapiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void readtest() {
//		WaveProcess waveProcess = new WaveProcess("test");
//		//waveProcess.CheckRiff("E:\\Project_Java\\recorddata\\20220820\\02\\20220820024707_19906.tx");
//		//waveProcess.CheckRiff("E:\\Project_Java\\recorddata\\20220820\\02\\20220820024707_19906.wav");
//		waveProcess.KeyValue("20220820024707_19906.wav");
//		waveProcess.KeyValue("20210101010101_19906.wav");
		log.info("Convert Start");
		MP3 mp3 = new MP3();
		try {
			mp3.ConvertWaveToMp3("d:\\test.pcm.wav","d:\\test.mp3");
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println(e.printStackTrace());

		}
		log.info("Convert End");

	}
}
