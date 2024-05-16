package com.API.BookLens.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents a Book entity.
 */
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @ManyToOne
    private Author author;

    private String language;

    private Long numberOfDownloads;

    public Book(){
        
    }

    /**
     * Constructs a new Book with the given attributes.
     *
     * @param title             The title of the book.
     * @param author            The author of the book.
     * @param language          The language of the book.
     * @param numberOfDownloads The number of downloads of the book.
     */
    public Book(String title, Author author, String language, int numberOfDownloads) {
        this.title = title;
        this.author = author;
        this.language = language;
        this.numberOfDownloads = (long) numberOfDownloads;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getNumberOfDownloads() {
        return numberOfDownloads;
    }

    public void setNumberOfDownloads(Long numberOfDownloads) {
        this.numberOfDownloads = numberOfDownloads;
    }

    /**
     * Returns the string representation of the Book object.
     *
     * @return The string representation of the Book object.
     */
    @Override
    public String toString() {
        return "-----------------------------------------\n" +
                "                   Book                  \n" +
                "Title: " + title + "\n" +
                "Author: " + author.getName() + "\n" +
                "Language: " + language + "\n" +
                "Downloads: " + numberOfDownloads + "\n" +
                "-----------------------------------------";
    }

}