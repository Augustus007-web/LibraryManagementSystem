package com.example.librarysystem.repository;

import com.example.librarysystem.entity.BorrowRecord;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByUserOrderByBorrowDateDesc(User user);
    Page<BorrowRecord> findByUserOrderByBorrowDateDesc(User user, Pageable pageable);
    List<BorrowRecord> findByBook(Book book);
    Optional<BorrowRecord> findByUserAndBookAndStatusIn(User user, Book book, List<String> statuses);
    List<BorrowRecord> findByStatus(String status);
    Page<BorrowRecord> findAllByOrderByBorrowDateDesc(Pageable pageable);
    boolean existsByBookAndStatusIn(Book book, List<String> statuses);
    boolean existsByUserAndStatusIn(User user, List<String> statuses); // 新增：检查用户是否有未归还的书籍
}