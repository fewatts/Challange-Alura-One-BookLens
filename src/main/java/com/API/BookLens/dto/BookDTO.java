package com.API.BookLens.dto;

import java.util.List;

public record BookDTO(int count, String next, String previous, List<BookResultDTO> results) {}

