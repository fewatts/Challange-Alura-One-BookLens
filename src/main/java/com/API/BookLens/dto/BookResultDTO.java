package com.API.BookLens.dto;

import java.util.List;

public record BookResultDTO(String title, List<AuthorDTO> authors, List<String> languages, int download_count) {}