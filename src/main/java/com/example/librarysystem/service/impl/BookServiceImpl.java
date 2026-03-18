package com.example.librarysystem.service.impl;

import com.example.librarysystem.entity.Book;
import com.example.librarysystem.entity.Category;
import com.example.librarysystem.repository.BookRepository;
import com.example.librarysystem.repository.BorrowRecordRepository;
import com.example.librarysystem.repository.CategoryRepository;
import com.example.librarysystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Join;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional
    public Book save(Book book) throws Exception { // 直接接收 Book 实体
        Optional<Book> existingBookWithIsbn = bookRepository.findByIsbn(book.getIsbn());
        if (existingBookWithIsbn.isPresent()) {
            throw new Exception("ISBN '" + book.getIsbn() + "' 已存在。");
        }

        // 处理类别: 假设 book 对象中的 category 已经设置了 id
        if (book.getCategory() != null && book.getCategory().getId() != null) {
            Category category = categoryRepository.findById(book.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("无效的类别ID: " + book.getCategory().getId()));
            book.setCategory(category); // 确保是持久化的 Category 对象
        } else {
            throw new Exception("图书必须选择一个有效的类别。");
        }

        book.setAvailableQuantity(book.getQuantity());//初始化可借数量
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        try {
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException e) {
            throw new Exception("保存图书失败，可能由于数据冲突：" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Book update(Long id, Book bookDetails) throws Exception { // 直接接收 Book 实体
        Book bookToUpdate = bookRepository.findById(id)
                .orElseThrow(() -> new Exception("未找到ID为 " + id + " 的图书"));

        Optional<Book> existingBookWithIsbn = bookRepository.findByIsbn(bookDetails.getIsbn());
        if (existingBookWithIsbn.isPresent() && !existingBookWithIsbn.get().getId().equals(id)) {
            throw new Exception("ISBN '" + bookDetails.getIsbn() + "' 已被其他图书使用。");
        }

        bookToUpdate.setIsbn(bookDetails.getIsbn());
        bookToUpdate.setTitle(bookDetails.getTitle());
        bookToUpdate.setAuthor(bookDetails.getAuthor());
        bookToUpdate.setPublisher(bookDetails.getPublisher());
        bookToUpdate.setPublicationDate(bookDetails.getPublicationDate());

        int quantityDifference = bookDetails.getQuantity() - bookToUpdate.getQuantity();
        bookToUpdate.setQuantity(bookDetails.getQuantity());
        int newAvailableQuantity = bookToUpdate.getAvailableQuantity() + quantityDifference;
        bookToUpdate.setAvailableQuantity(Math.max(0, Math.min(newAvailableQuantity, bookToUpdate.getQuantity())));

        bookToUpdate.setDescription(bookDetails.getDescription());
        bookToUpdate.setCoverImageUrl(bookDetails.getCoverImageUrl());

        if (bookDetails.getCategory() != null && bookDetails.getCategory().getId() != null) {
            Category category = categoryRepository.findById(bookDetails.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("无效的类别ID: " + bookDetails.getCategory().getId()));
            bookToUpdate.setCategory(category);
        } else {
            throw new Exception("图书必须选择一个有效的类别。");
        }
        bookToUpdate.setUpdatedAt(LocalDateTime.now());
        try {
            return bookRepository.save(bookToUpdate);
        } catch (DataIntegrityViolationException e) {
            throw new Exception("更新图书失败，可能由于数据冲突：" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws Exception {
        if (!bookRepository.existsById(id)) {
            throw new Exception("未找到ID为 " + id + " 的图书，无法删除。");
        }
        // 实际应用中应检查是否有未归还的借阅记录
        Book book = bookRepository.findById(id).get(); // Get the book to check associated borrows
        if (borrowRecordRepository.existsByBookAndStatusIn(book, Arrays.asList("BORROWED", "OVERDUE"))) {
            throw new Exception("无法删除图书 '" + book.getTitle() + "'，因为它尚有未归还的借阅记录。");
        }
        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception("删除图书失败：" + e.getMessage(), e);
        }
    }
    // 注入 BorrowRecordRepository 以便在删除图书前检查
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;


    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public Page<Book> searchBooks(String keyword, Long categoryId, Pageable pageable) {
        Specification<Book> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null && !keyword.trim().isEmpty()) {
                String pattern = "%" + keyword.toLowerCase().trim() + "%";
                //组合查询：书名 OR 作者 OR ISBN
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("isbn")), pattern)
                ));
            }
            if (categoryId != null && categoryId > 0) {
                Join<Book, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("id"), categoryId));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return bookRepository.findAll(spec, pageable);
    }
}