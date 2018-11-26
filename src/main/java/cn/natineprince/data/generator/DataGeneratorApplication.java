package cn.natineprince.data.generator;

import java.io.IOException;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class DataGeneratorApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(DataGeneratorApplication.class, args);
		
		ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
		if (!applicationArguments.containsOption("disable.auto.start.home.page")) {
			try {
				if (applicationArguments.containsOption("server.port")) {
					Runtime.getRuntime().exec("cmd /c start http://localhost:"
							+ applicationArguments.getOptionValues("server.port").get(0));
				} else {
					Runtime.getRuntime().exec("cmd /c start http://localhost");
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		
	}
}
