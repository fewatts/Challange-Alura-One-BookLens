package com.API.BookLens.main;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.API.BookLens.dto.*;
import com.API.BookLens.model.*;
import com.API.BookLens.repository.*;
import com.API.BookLens.service.*;

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
            int option = 0;

            System.out.println("""
                    -----------------------------------------
                        Choose your number option:
                        1- Search book by title
                        2- List registered books
                        3- List registered authors
                        4- List alive authors of certain year
                        5- List books with a certain language
                        6- Close application
                    -----------------------------------------
                    """);

            try {
                option = scan.nextInt();
            } catch (NumberFormatException | InputMismatchException error) {
                System.out.println("Only numbers are allowed in this menu!");
                System.out.println(error);
                System.out.println("-----------------------------------------");
                scan.next();
                continue;
            }

            switch (option) {
                case 1 -> searchBookByTitle();
                case 2 -> listRegisteredBooks();
                case 3 -> listRegisteredAuthors();
                case 4 -> listAliveAuthorsOfCertainYear();
                case 5 -> listBooksWithACertainLanguage();
                case 6 -> {
                    System.out.println("Closing application...");
                    return;
                }
                default -> System.out.println("Invalid option!");
            }

        }

    }

    /**
     * Searches for a book by its title, fetches data from an external API,
     * processes the results, and saves the book information to the database if not
     * already present.
     */
    private void searchBookByTitle() { 

        if (scan.hasNextLine()) scan.nextLine();

        System.out.println("Type the book name:");
        String bookName = scan.nextLine();
        String bookInURL = formatBookNameForURL(bookName);

        try {
            String json = fetchBookDataFromAPI(bookInURL);
            BookDTO bookDTO = convertJsonToBookDTO(json);

            if (processBookResults(bookDTO)) {
                System.out.println("Book details processed and saved.");
            }else{
                System.out.println("-----------------------------------------");
                System.out.println("No books found.");
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Formats the book name to be URL-friendly by replacing spaces with '%20'.
     *
     * @param bookName the original book name
     * @return the formatted book name suitable for use in a URL
     */
    private String formatBookNameForURL(String bookName) {
        return bookName.replace(" ", "%20");
    }

    /**
     * Fetches book data from an external API using the given URL.
     *
     * @param bookInURL the formatted book name in URL form
     * @return the JSON response from the API as a string
     * @throws IOException if an I/O error occurs during the API request
     */
    private String fetchBookDataFromAPI(String bookInURL) throws IOException {
        return GetAPIData.getBookData("https://gutendex.com/books/?search=" + bookInURL);
    }

    /**
     * Converts a JSON string to a BookDTO object.
     *
     * @param json the JSON string representing book data
     * @return the BookDTO object parsed from the JSON string
     */
    private BookDTO convertJsonToBookDTO(String json) {
        return JsonConverter.fromJson(json, BookDTO.class);
    }

    /**
     * Processes the book results from the BookDTO object. If a book is found and
     * not already in the database,
     * it saves the book and author information.
     *
     * @param bookDTO the BookDTO object containing book results
     * @return true if a book was processed and found, false otherwise
     */
    private boolean processBookResults(BookDTO bookDTO) {
        boolean found = false;
        for (var result : bookDTO.results()) {
            Book book = createBookFromResult(result);
            if (bookRepository.findByTitle(book.getTitle()) != null) {
                System.out.println("-----------------------------------------");
                System.out.println("Book is already present in the database.");
                return false;
            } else if(book.getTitle() == null){
                return false;
            }else {
                saveBookAndAuthor(book);
            }
            System.out.println(book.toString());
            found = true;
            break;
        }
        return found;
    }

    /**
     * Creates a Book object from a BookResultDTO result.
     *
     * @param result the BookResultDTO object representing a book result
     * @return the Book object created from the result
     */
    private Book createBookFromResult(BookResultDTO result) {
        AuthorDTO authorDTO = result.authors().get(0);
        int birthYear = authorDTO.birth_year() != null ? authorDTO.birth_year() : 0;
        int deathYear = authorDTO.death_year() != null ? authorDTO.death_year() : 0;
        Author author = new Author(authorDTO.name(), birthYear, deathYear);
        return new Book(result.title(), author, result.languages().get(0), result.download_count());
    }

    /**
     * Saves the book and author information to the database. If the author already
     * exists,
     * it associates the book with the existing author.
     *
     * @param book the Book object to be saved
     */
    private void saveBookAndAuthor(Book book) {
        Author existingAuthor = authorRepository.findByName(book.getAuthor().getName());
        if (existingAuthor != null) {
            book.setAuthor(existingAuthor);
        } else {
            authorRepository.save(book.getAuthor());
        }
        bookRepository.save(book);
    }

    /**
     * Handles exceptions by printing the error message and stack trace.
     *
     * @param e the exception that occurred
     */
    private void handleException(Exception e) {
        System.out.println("-----------------------------------------");
        System.out.println("An error occurred: " + e.getMessage());
        e.printStackTrace();
    }

    /**
     * Lists all registered books and prints their information on the console.
     */
    private void listRegisteredBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            System.out.println("-----------------------------------------");
            System.out.println("Your database is empty");
        }
        books.forEach(b -> System.out.println(b.toString()));
    }

    /**
     * Lists all registered authors and prints their information on the console.
     */
    private void listRegisteredAuthors() {
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            System.out.println("-----------------------------------------");
            System.out.println("Your database is empty");
            return;
        }
        authors.forEach(a -> System.out.println(a.toString()));
    }

    /**
     * Lists all authors who were alive during a certain year and prints their
     * information on the console.
     * Prompts the user to input the year.
     */
    private void listAliveAuthorsOfCertainYear() {
        System.out.println("Type the year:");
        int year = scan.nextInt();

        List<Author> authors = authorRepository.findAll();

        List<Author> aliveAuthors = authors.stream()
                .filter(author -> author.getDateOfBirthYear() <= year && author.getDateOfDeathYear() >= year)
                .collect(Collectors.toList());

        if (aliveAuthors.isEmpty()) {
            System.out.println("-----------------------------------------");
            System.out.println("No living authors found for the year " + year);
            return;
        } else {
            aliveAuthors.forEach(a -> System.out.println(a.toString()));
        }
    }

    /**
     * Lists books with a certain language based on user input.
     * The method prompts the user to choose a language from a predefined list
     * and then filters the books to display only those in the chosen language.
     * If no books are found for the chosen language, it prints a message indicating
     * so.
     */
    private void listBooksWithACertainLanguage() {
        int languageOption = 0;
        System.out.println("""
                -----------------------------------------
                    Choose the language:
                    1- En (English)
                    2- Pt (Portuguese)
                    3- Fr (French)
                    4- Es (Spanish)
                -----------------------------------------
                    """);
        try {
            languageOption = scan.nextInt();
        } catch (NumberFormatException | InputMismatchException error) {
            System.out.println("Only numbers are allowed in this menu!");
            System.out.println(error);
            System.out.println("-----------------------------------------");
        }
        final String language;
        switch (languageOption) {
            case 1 -> language = "en";
            case 2 -> language = "pt";
            case 3 -> language = "fr";
            case 4 -> language = "es";
            default -> {
                System.out.println("Invalid option.");
                return;
            }
        }

        List<Book> books = bookRepository.findAll();
        List<Book> booksWithCertainLanguage = books.stream()
                .filter(book -> book.getLanguage().equalsIgnoreCase(language))
                .collect(Collectors.toList());
        if (booksWithCertainLanguage.isEmpty()) {
            System.out.println("-----------------------------------------");
            System.out.println("No books found in this language: " + language);
            return;
        } else {
            booksWithCertainLanguage.forEach(a -> System.out.println(a.toString()));
        }

    }

}