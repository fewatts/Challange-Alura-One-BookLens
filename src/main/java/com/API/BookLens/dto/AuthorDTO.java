package com.API.BookLens.dto;


/**
 * Represents a Data Transfer Object (DTO) for an Author.
 *
 * This record encapsulates information about an author, including their name, birth year, and death year.
 */
public record AuthorDTO(String name, Integer birth_year, Integer death_year) {}