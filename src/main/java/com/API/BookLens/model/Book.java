package com.API.BookLens.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    private Author author;

    private String language;

    private Long numberOfDownloads;

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

    @Override
    public String toString() {
        String authorName = (author != null) ? author.getName() : "Unknown";
        return "-----------------------------------------\n" +
                "                   Book                  \n" +
                "Title: " + title + "\n" +
                "Author: " + authorName + "\n" +
                "Language: " + language + "\n" +
                "Downloads: " + numberOfDownloads + "\n" +
                "-----------------------------------------";
    }

}