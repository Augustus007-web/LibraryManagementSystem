package com.example.librarysystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Lob
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // CascadeType.PERSIST: 保存Category时，如果books集合中有新的Book对象，也会被持久化。
    // CascadeType.MERGE: 合并Category时，也会合并books集合中的Book对象。
    // 避免使用 CascadeType.ALL 或 REMOVE，除非你希望删除类别时也删除所有关联的书籍。
    // 通常，删除类别前应检查是否还有书籍属于该类别。
    @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Book> books;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // 方便Thymeleaf表单处理null id的情况
    @Override
    public String toString() {
        return id != null ? id.toString() : "";
    }
}