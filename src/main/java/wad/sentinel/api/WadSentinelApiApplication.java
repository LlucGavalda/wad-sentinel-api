package wad.sentinel.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import wad.sentinel.security.Constants;


@SpringBootApplication
public class WadSentinelApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WadSentinelApiApplication.class, args);
	}

		/**
	 * CORS configuration
	 * 
	 * @return
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {

			/**
			 * Adds CORS mappings based upon parameters
			 */
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping(Constants.CORS_MAPPING)
						.allowedOrigins(Constants.CORS_ORIGINS)
						.allowedMethods(Constants.CORS_METHODS)
						.exposedHeaders(Constants.CORS_EXPOSED_HEADERS);
			}
		};
	}

}
