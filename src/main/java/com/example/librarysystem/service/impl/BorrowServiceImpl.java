package com.example.librarysystem.service.impl;

import com.example.librarysystem.entity.Book;
import com.example.librarysystem.entity.BorrowRecord;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.repository.BookRepository;
import com.example.librarysystem.repository.BorrowRecordRepository;
import com.example.librarysystem.repository.UserRepository;
import com.example.librarysystem.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public BorrowServiceImpl(BorrowRecordRepository borrowRecordRepository,
                             BookRepository bookRepository,
                             UserRepository userRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public BorrowRecord borrowBook(Long userId, Long bookId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("用户不存在！"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new Exception("图书不存在！"));

        if (book.getAvailableQuantity() <= 0) {
            throw new Exception("《" + book.getTitle() + "》已无库存可借！");
        }

        Optional<BorrowRecord> existingBorrow = borrowRecordRepository
                .findByUserAndBookAndStatusIn(user, book, Arrays.asList("BORROWED", "OVERDUE"));
        if (existingBorrow.isPresent()) {
            throw new Exception("您已借阅《" + book.getTitle() + "》且尚未归还！");
        }

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);
        //创建记录
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setUser(user);
        borrowRecord.setBook(book);
        borrowRecord.setBorrowDate(LocalDateTime.now());
        borrowRecord.setStatus("BORROWED");

        return borrowRecordRepository.save(borrowRecord);
    }

    @Override
    @Transactional
    public BorrowRecord returnBook(Long borrowRecordId, Long userId) throws Exception {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new Exception("借阅记录不存在！"));

        if (!borrowRecord.getUser().getId().equals(userId)) {
            throw new Exception("您无权操作此借阅记录！");
        }

        if ("RETURNED".equals(borrowRecord.getStatus())) {
            throw new Exception("这本书已经归还过了！");
        }

        Book book = borrowRecord.getBook();
        if (book == null) {
            throw new Exception("关联的图书信息丢失！");
        }
        //恢复库存
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepository.save(book);
        //更新状态
        borrowRecord.setStatus("RETURNED");
        borrowRecord.setReturnDate(LocalDateTime.now());

        return borrowRecordRepository.save(borrowRecord);
    }

    @Override
    public List<BorrowRecord> findBorrowRecordsByUser(User user) {
        return borrowRecordRepository.findByUserOrderByBorrowDateDesc(user);
    }

    @Override
    public Page<BorrowRecord> findBorrowRecordsByUser(User user, Pageable pageable) {
        return borrowRecordRepository.findByUserOrderByBorrowDateDesc(user, pageable);
    }


    @Override
    public Page<BorrowRecord> findAllBorrowRecords(Pageable pageable) {
        return borrowRecordRepository.findAllByOrderByBorrowDateDesc(pageable);
    }

    @Override
    public Optional<BorrowRecord> findById(Long id) { // 确认返回类型是 Optional<BorrowRecord>
        return borrowRecordRepository.findById(id);
    }
}