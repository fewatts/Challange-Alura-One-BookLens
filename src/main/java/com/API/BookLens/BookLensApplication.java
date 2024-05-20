package com.API.BookLens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.API.BookLens.main.Main;
import com.API.BookLens.repository.AuthorRepository;
import com.API.BookLens.repository.BookRepository;

@SpringBootApplication
public class BookLensApplication implements CommandLineRunner {
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private BookRepository bookRepository;

	/**
	 * The main method to start the BookLens application.
	 * 
	 * @param args Command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(BookLensApplication.class, args);
	}

	/**
	 * Executes the main logic of the application upon startup.
	 * It creates an instance of the Main class and invokes its menu method.
	 * 
	 * @param args Command-line arguments passed to the application
	 * @throws Exception If an error occurs during execution
	 */
	@Override
	public void run(String... args) throws Exception {
		Main main = new Main(authorRepository, bookRepository);
		main.menu();
	}
	
}