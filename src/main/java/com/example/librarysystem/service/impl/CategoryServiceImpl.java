package com.example.librarysystem.service.impl;

import com.example.librarysystem.dto.CategoryDto;
import com.example.librarysystem.entity.Category;
import com.example.librarysystem.repository.BookRepository; // 需要 BookRepository 来检查关联书籍
import com.example.librarysystem.repository.CategoryRepository;
import com.example.librarysystem.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository; // 注入 BookRepository

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    @Transactional
    public Category save(CategoryDto categoryDto) throws Exception {
        Optional<Category> existingCategory = categoryRepository.findByName(categoryDto.getName());
        if (existingCategory.isPresent()) {
            throw new Exception("类别名称 '" + categoryDto.getName() + "' 已存在。");
        }
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        // createdAt 和 updatedAt 会通过 @PrePersist 自动设置
        try {
            return categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new Exception("保存类别失败，可能由于数据冲突：" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Category update(Long id, CategoryDto categoryDto) throws Exception {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("未找到ID为 " + id + " 的类别"));

        Optional<Category> existingCategoryWithName = categoryRepository.findByName(categoryDto.getName());
        if (existingCategoryWithName.isPresent() && !existingCategoryWithName.get().getId().equals(id)) {
            throw new Exception("类别名称 '" + categoryDto.getName() + "' 已被其他类别使用。");
        }

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setUpdatedAt(LocalDateTime.now()); // @PreUpdate 也会处理，但显式设置也无妨
        try {
            return categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new Exception("更新类别失败，可能由于数据冲突：" + e.getMessage(), e);
        }
    }

    @Override
    public boolean hasAssociatedBooks(Long categoryId) {
        // 检查是否有任何书籍属于这个类别ID
        // BookRepository 需要一个方法如 countByCategoryId(Long categoryId) 或 findFirstByCategoryId(Long categoryId)
        // 这里我们用一个简单的方式，如果BookRepository有 findByCategory(Category category)
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isPresent()){
            // 假设 BookRepository 有一个方法 Page<Book> findByCategory(Category category, Pageable pageable);
            // 或者 List<Book> findByCategory(Category category);
            // 为了简单，我们直接使用 category.getBooks()，但这依赖于 LAZY loading 和事务上下文
            // 更安全的方式是直接查询 BookRepository
            return bookRepository.existsByCategory(category.get()); // 需要在 BookRepository 添加 existsByCategory
        }
        return false;
    }


    @Override
    @Transactional
    public void deleteById(Long id) throws Exception {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("未找到ID为 " + id + " 的类别，无法删除。"));

        // 在 BookRepository 中添加一个方法: boolean existsByCategoryId(Long categoryId);
        // 或者使用 category.getBooks().isEmpty() 但要注意懒加载问题
        if (bookRepository.existsByCategory(category)) { // 假设 BookRepository 有此方法
            throw new Exception("无法删除类别 '" + category.getName() + "'，因为它下面仍有关联的图书。");
        }
        try {
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception("删除类别失败：" + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }
}