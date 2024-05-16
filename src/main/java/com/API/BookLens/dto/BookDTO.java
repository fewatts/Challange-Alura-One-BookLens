package com.API.BookLens.dto;

import java.util.List;

/**
 * Represents a Data Transfer Object (DTO) for a Book.
 *
 * This record encapsulates information about a book, including the count of books, URLs for navigating through pages of books,
 * and a list of BookResultDTO objects representing individual books.
 */
public record BookDTO(int count, String next, String previous, List<BookResultDTO> results) {}