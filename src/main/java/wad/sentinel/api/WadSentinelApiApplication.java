package wad.sentinel.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"wad.sentinel.*"})
public class WadSentinelApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WadSentinelApiApplication.class, args);
	}

}
