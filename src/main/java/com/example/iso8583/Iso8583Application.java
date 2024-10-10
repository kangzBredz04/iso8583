package com.example.iso8583;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Iso8583Application {

	public static String getTransactionTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmm");
		return LocalDateTime.now().format(formatter);
	}

	public static void main(String[] args) {
		SpringApplication.run(Iso8583Application.class, args);
		System.out.println("Transaction Time: " + getTransactionTime());
	}

}
