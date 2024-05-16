package com.API.BookLens.dto;

import java.util.List;

/**
 * Represents a Data Transfer Object (DTO) for a Book Result.
 *
 * This record encapsulates information about a book result, including the title of the book, a list of AuthorDTO objects representing authors,
 * a list of languages the book is available in, and the download count of the book.
 */
public record BookResultDTO(String title, List<AuthorDTO> authors, List<String> languages, int download_count) {}