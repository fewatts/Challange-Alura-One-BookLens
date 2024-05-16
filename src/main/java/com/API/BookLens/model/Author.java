package com.API.BookLens.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Represents an Author entity.
 */
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private int dateOfBirthYear;

    private int dateOfDeathYear;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> books = new ArrayList<>();

    /**
     * Constructs a new Author with the given attributes.
     *
     * @param name            The name of the author.
     * @param dateOfBirthYear The birth year of the author.
     * @param dateOfDeathYear The death year of the author.
     */
    public Author(String name, int dateOfBirthYear, int dateOfDeathYear) {
        this.name = name;
        this.dateOfBirthYear = dateOfBirthYear;
        this.dateOfDeathYear = dateOfDeathYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDateOfBirthYear() {
        return dateOfBirthYear;
    }

    public void setDateOfBirthYear(int dateOfBirthYear) {
        this.dateOfBirthYear = dateOfBirthYear;
    }

    public int getDateOfDeathYear() {
        return dateOfDeathYear;
    }

    public void setDateOfDeathYear(int dateOfDeathYear) {
        this.dateOfDeathYear = dateOfDeathYear;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    /**
     * Returns the string representation of the Author object.
     *
     * @return The string representation of the Author object.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("-----------------------------------------\n");
        stringBuilder.append("                  Author                 \n");
        stringBuilder.append("Name: ").append(name).append("\n");
        stringBuilder.append("Birth year: ").append(dateOfBirthYear).append("\n");
        stringBuilder.append("Death year: ").append(dateOfDeathYear).append("\n");
        stringBuilder.append("Books: [");
        for (int i = 0; i < books.size(); i++) {
            stringBuilder.append(books.get(i).getTitle());
            if (i < books.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]\n");
        stringBuilder.append("-----------------------------------------");
        return stringBuilder.toString();
    }

}