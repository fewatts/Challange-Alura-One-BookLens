package com.API.BookLens;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.API.BookLens.main.Main;

@SpringBootApplication
public class BookLensApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(BookLensApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Main main = new Main();
		main.menu();
	}

}