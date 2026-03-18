package com.example.librarysystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn; // ISBN 书号

    @Column(nullable = false)
    private String title; // 书名

    @Column(nullable = false)
    private String author; // 作者

    private String publisher; // 出版社

    @Column(name = "publication_date")
    private LocalDate publicationDate; // 出版日期

    @ManyToOne(fetch = FetchType.EAGER) // 多本书对应一个类别
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private int quantity = 0; // 馆藏数量

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity = 0; // 可借阅数量

    @Lob
    private String description; // 图书简介

    @Column(name = "cover_image_url")
    private String coverImageUrl; // 封面图片URL

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (availableQuantity == 0 && quantity > 0) { // 初始化时同步可借数量
            availableQuantity = quantity;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
