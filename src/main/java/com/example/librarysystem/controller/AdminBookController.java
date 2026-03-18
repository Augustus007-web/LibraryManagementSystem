package com.example.librarysystem.controller;

import com.example.librarysystem.entity.Book; // 直接使用 Book 实体
import com.example.librarysystem.entity.Category;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.service.BookService;
import com.example.librarysystem.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    private final BookService bookService;
    private final CategoryService categoryService;

    @Autowired
    public AdminBookController(BookService bookService, CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
    }

    private boolean isAdmin(HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "请先登录。");
            return false;
        }
        if (!"ADMIN".equals(loggedInUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "您没有权限访问此页面。");
            return false;
        }
        return true;
    }

    @GetMapping
    public String listBooks(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(defaultValue = "id,asc") String[] sort,
                            @RequestParam(required = false) String keyword,
                            Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session, redirectAttributes)) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            return loggedInUser == null ? "redirect:/login" : "redirect:/";
        }

        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1].toLowerCase() : "asc";
        Sort.Direction direction = "desc".equals(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<Book> bookPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            bookPage = bookService.searchBooks(keyword, null, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            bookPage = bookService.findAll(pageable);
        }

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("pageTitle", "图书信息管理");
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDirection);
        model.addAttribute("reverseSortDir", "asc".equals(sortDirection) ? "desc" : "asc");
        return "admin/books_list";
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session, redirectAttributes)) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            return loggedInUser == null ? "redirect:/login" : "redirect:/";
        }
        model.addAttribute("pageTitle", "添加新图书");
        Book newBook = new Book();
        newBook.setCategory(new Category()); // 初始化 category 防止 Thymeleaf 访问 null.category.id
        model.addAttribute("book", newBook);
        model.addAttribute("categories", categoryService.findAll());
        return "admin/book_form";
    }

    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable("id") Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session, redirectAttributes)) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            return loggedInUser == null ? "redirect:/login" : "redirect:/";
        }
        Optional<Book> bookOptional = bookService.findById(id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if (book.getCategory() == null) {
                book.setCategory(new Category()); // 初始化一个空的Category对象
            }
            model.addAttribute("book", book);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("pageTitle", "编辑图书");
            return "admin/book_form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "未找到ID为 " + id + " 的图书。");
            return "redirect:/admin/books";
        }
    }

    @PostMapping("/save")
    public String saveBook(@Valid @ModelAttribute("book") Book book,
                           BindingResult result,
                           // @RequestParam("category.id") Long categoryId, // 直接从 book.category.id 获取
                           Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session, redirectAttributes)) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            return loggedInUser == null ? "redirect:/login" : "redirect:/";
        }

        // 从绑定的 book 对象中获取 categoryId
        Long categoryId = (book.getCategory() != null) ? book.getCategory().getId() : null;

        if (categoryId == null) {
            result.rejectValue("category", "notNull", "必须选择一个类别。");
        } else {
            Optional<Category> categoryOptional = categoryService.findById(categoryId);
            if (categoryOptional.isPresent()) {
                book.setCategory(categoryOptional.get()); // 设置完整的 Category 对象
            } else {
                result.rejectValue("category", "invalid", "选择的类别无效。");
            }
        }

        // ISBN 唯一性检查 (如果使用 BookDto 会更方便在 DTO 或 Service 层处理)
        try {
            if (book.getId() == null) { // 新增
                // ISBN 唯一性检查已在 service.save 中
            } else { // 编辑
                Optional<Book> existingBookWithIsbn = bookService.findByIsbn(book.getIsbn());
                if (existingBookWithIsbn.isPresent() && !existingBookWithIsbn.get().getId().equals(book.getId())) {
                    result.rejectValue("isbn", "exists", "该ISBN已被其他图书使用。");
                }
            }
        } catch (Exception e) {
            result.rejectValue("isbn", "error", e.getMessage());
        }


        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("pageTitle", book.getId() == null ? "添加新图书" : "编辑图书");
            return "admin/book_form";
        }

        try {
            if (book.getId() == null) {
                bookService.save(book);
                redirectAttributes.addFlashAttribute("successMessage", "图书添加成功！");
            } else {
                bookService.update(book.getId(), book);
                redirectAttributes.addFlashAttribute("successMessage", "图书更新成功！");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "操作失败: " + e.getMessage());
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("pageTitle", book.getId() == null ? "添加新图书" : "编辑图书");
            // 确保 book 对象回传，以便表单能正确显示之前输入的值
            if(book.getCategory() == null && categoryId != null) { // 如果因为错误导致 category 未设置，尝试恢复
                categoryService.findById(categoryId).ifPresent(book::setCategory);
            } else if (book.getCategory() == null) {
                book.setCategory(new Category()); // 避免 null.id
            }
            model.addAttribute("book", book);
            return "admin/book_form";
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session, redirectAttributes)) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            return loggedInUser == null ? "redirect:/login" : "redirect:/";
        }
        try {
            bookService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "图书删除成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "删除失败: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }
}