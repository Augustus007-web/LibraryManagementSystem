package com.example.librarysystem.service;

import com.example.librarysystem.dto.CategoryDto; // 使用 DTO
import com.example.librarysystem.entity.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Category save(CategoryDto categoryDto) throws Exception; // 修改为接收 DTO
    Category update(Long id, CategoryDto categoryDto) throws Exception; // 修改为接收 DTO
    void deleteById(Long id) throws Exception;
    Optional<Category> findByName(String name);
    boolean hasAssociatedBooks(Long categoryId); // 新增：检查类别下是否有书
}
