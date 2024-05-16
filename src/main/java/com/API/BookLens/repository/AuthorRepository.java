package com.API.BookLens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.API.BookLens.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository <Author, Long>{
    
}