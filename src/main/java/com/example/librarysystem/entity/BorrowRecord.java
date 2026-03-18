package com.example.librarysystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "borrow_records")
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 借阅用户

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book; // 借阅图书

    @Column(name = "borrow_date")
    private LocalDateTime borrowDate; // 借阅日期

    @Column(name = "due_date")
    private LocalDateTime dueDate; // 应还日期

    @Column(name = "return_date")
    private LocalDateTime returnDate; // 实际归还日期

    @Column(nullable = false, length = 20)
    private String status = "BORROWED"; // 借阅状态: BORROWED, RETURNED, OVERDUE

    @Lob
    private String notes; // 备注

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (borrowDate == null) {
            borrowDate = LocalDateTime.now();
        }
        // 默认借阅期限为 30 天
        if (dueDate == null && borrowDate != null) {
            dueDate = borrowDate.plusDays(30);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
