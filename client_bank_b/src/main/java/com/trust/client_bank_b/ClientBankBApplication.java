package com.trust.client_bank_b;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ClientBankBApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientBankBApplication.class, args);
	}

}
