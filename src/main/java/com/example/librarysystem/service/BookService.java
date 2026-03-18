package com.example.librarysystem.service;

import com.example.librarysystem.entity.Book; // 直接使用 Book 实体
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface BookService {
    Page<Book> findAll(Pageable pageable);
    Optional<Book> findById(Long id);
    Book save(Book book) throws Exception; // 参数类型改为 Book
    Book update(Long id, Book bookDetails) throws Exception; // 参数类型改为 Book
    void deleteById(Long id) throws Exception;
    Optional<Book> findByIsbn(String isbn);
    Page<Book> searchBooks(String keyword, Long categoryId, Pageable pageable);
}
