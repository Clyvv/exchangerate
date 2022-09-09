package com.mercedesbenz.exchangerate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClientRequest;

import java.time.Duration;

@EnableScheduling
@SpringBootApplication
public class ExchangerateApplication {

	@Value("${fixer-api.baseurl}")
	private String externalExchangeRateApiBaseUrl;

	@Value("${fixer-api.api-key}")
	private String externalExchangeRateApiKey;

	public static void main(String[] args) {
		SpringApplication.run(ExchangerateApplication.class, args);
	}

	@Bean
	public WebClient getWebClient(){
		return WebClient.builder()
				.baseUrl(externalExchangeRateApiBaseUrl)
				.defaultHeader("apiKey", externalExchangeRateApiKey)
				.build();
	}
}
