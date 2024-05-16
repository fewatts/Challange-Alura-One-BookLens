package com.API.BookLens.main;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
                int deathYear = authorDTO.death_year() != null ? authorDTO.death_year() : 0;

                Author author = new Author(authorDTO.name(), authorDTO.birth_year(), deathYear);
                Book book = new Book(result.title(), author, result.languages().get(0), result.download_count());
                Book existingBook = bookRepository.findByTitle(book.getTitle());

                if (!(existingBook == null)) {
                    System.out.println("-----------------------------------------");
                    System.out.println("Book is already present in the database.");
                    return;
                } else {
                    Author existingAuthor = authorRepository.findByName(author.getName());
                    if (!(existingAuthor == null)) {
                        book.setAuthor(existingAuthor);
                    } else {
                        authorRepository.save(author);
                    }
                    bookRepository.save(book);
                }

                System.out.println(book.toString());
                found = true;
                break;
            }

            if (!found) {
                System.out.println("-----------------------------------------");
                System.out.println("No books found with the given title.");
            }

        } catch (Exception e) {
            System.out.println("-----------------------------------------");
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

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
        int option = 0;
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
            option = Integer.parseInt(scan.nextLine());
        } catch (NumberFormatException error) {
            System.out.println("Only numbers are allowed in this menu!");
            System.out.println(error);
            System.out.println("-----------------------------------------");
        }
        final String language;
        switch (option) {
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