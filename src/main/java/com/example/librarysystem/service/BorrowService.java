package com.example.librarysystem.service;

import com.example.librarysystem.entity.BorrowRecord;
import com.example.librarysystem.entity.User;
// import com.example.librarysystem.entity.Book; // Book is not directly used in method signatures here
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional; // <--- 添加对 Optional 的导入

public interface BorrowService {
    BorrowRecord borrowBook(Long userId, Long bookId) throws Exception;
    BorrowRecord returnBook(Long borrowRecordId, Long userId) throws Exception;
    List<BorrowRecord> findBorrowRecordsByUser(User user);
    Page<BorrowRecord> findBorrowRecordsByUser(User user, Pageable pageable);
    Page<BorrowRecord> findAllBorrowRecords(Pageable pageable);
    Optional<BorrowRecord> findById(Long id); // 确认返回类型是 Optional<BorrowRecord>
}
