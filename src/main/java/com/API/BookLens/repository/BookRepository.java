package com.API.BookLens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.API.BookLens.model.Book;

/**
 * This interface represents a repository for managing Book entities.
 * It extends JpaRepository, providing the basic CRUD operations for Book entities.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {}