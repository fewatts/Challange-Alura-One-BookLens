package com.API.BookLens.main;

import java.util.Scanner;

import com.API.BookLens.dto.AuthorDTO;
import com.API.BookLens.dto.BookDTO;
import com.API.BookLens.model.Author;
import com.API.BookLens.model.Book;
import com.API.BookLens.repository.AuthorRepository;
import com.API.BookLens.repository.BookRepository;
import com.API.BookLens.service.GetAPIData;
import com.API.BookLens.service.JsonConverter;

/**
 * The Main class is responsible for managing the main menu and invoking
 * appropriate actions based on user input.
 */
public class Main {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    private Scanner scan = new Scanner(System.in);

    public Main(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Displays the main menu and handles user input to perform various actions.
     */
    public void menu() {

        while (true) {

            int option = -1;

            System.out.println("""
                    -----------------------------------------
                        Choose your number option:
                        1- Search book by title
                        2- List registered books
                        3- List registered authors
                        4- List alive authors of certain year
                        5- List books with a certain language
                        0- Close application
                    -----------------------------------------
                    """);

            try {
                option = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException error) {
                System.out.println("Only numbers are allowed in this menu!");
                System.out.println(error);
                System.out.println("-----------------------------------------");
            }

            switch (option) {

                case 1 -> searchBookByTitle();
                case 2 -> listRegisteredBooks();
                case 3 -> listRegisteredAuthors();
                case 4 -> listAliveAuthorsOfCertainYear();
                case 5 -> listBooksWithACertainLanguage();
                case 0 -> {
                    System.out.println("Closing application...");
                    return;
                }
                default -> System.out.println("Invalid option!");
            }

        }

    }

    /**
     * Searches for a book by title.
     * 
     * Prompts the user to input the title of the book to search for. It then sends
     * a request to the Gutendex API to retrieve information
     * about the book. If a book with the given title is found, it is saved to the
     * database. Only the first matching book is saved.
     * 
     * If no books are found with the given title, a message is printed indicating
     * that no books were found.
     */
    private void searchBookByTitle() {
        System.out.println("Type the book name:");
        String bookName = scan.nextLine();
        String bookInURL = bookName.replace(" ", "%20");

        try {
            String json = GetAPIData.getBookData("https://gutendex.com/books/?search=" + bookInURL);
            BookDTO bookDTO = JsonConverter.fromJson(json, BookDTO.class);
            boolean found = false;

            for (var result : bookDTO.results()) {
                AuthorDTO authorDTO = result.authors().get(0);
                Author author = new Author(authorDTO.name(), authorDTO.birth_year(), authorDTO.death_year());
                Book book = new Book(result.title(), author,
                        result.languages().isEmpty() ? "Unknown" : result.languages().get(0),
                        result.download_count());
                authorRepository.save(author);
                bookRepository.save(book);
                System.out.println(book.toString());
                found = true;
                break;
            }

            if (!found) {
                System.out.println("No books found with the given title.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        
    }

    private void listRegisteredBooks() {
        System.out.println("Unimplemented method 'listRegisteredBooks'");
    }

    private void listRegisteredAuthors() {
        System.out.println("Unimplemented method 'listRegisteredAuthors'");
    }

    private void listAliveAuthorsOfCertainYear() {
        System.out.println("Unimplemented method 'listAliveAuthorsOfCertainYear'");
    }

    private void listBooksWithACertainLanguage() {
        System.out.println("Unimplemented method 'listBooksWithACertainLanguage'");
    }

}