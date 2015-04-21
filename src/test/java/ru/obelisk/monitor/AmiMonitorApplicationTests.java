package ru.obelisk.monitor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.obelisk.monitor.AmiMonitorApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AmiMonitorApplication.class)
@WebAppConfiguration
public class AmiMonitorApplicationTests {

	@Test
	public void contextLoads() {
	}

}
