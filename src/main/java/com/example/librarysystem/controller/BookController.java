package com.example.librarysystem.controller;

import com.example.librarysystem.entity.Book;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.service.BookService;
import com.example.librarysystem.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books") // 类级别映射 /books
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;


    @Autowired
    public BookController(BookService bookService, CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
    }

    private boolean isLoggedIn(HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "请先登录后操作。");
            return false;
        }
        return true;
    }

    // 将 /books 和 /books/search 都映射到这个方法
    @GetMapping(value = {"", "/search"}) // 空路径 "" 对应 /books, "/search" 对应 /books/search
    public String searchBooks(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "title,asc") String[] sort,
                              @RequestParam(required = false) String keyword,
                              @RequestParam(required = false) Long categoryId,
                              Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isLoggedIn(session, redirectAttributes)) {
            return "redirect:/login";
        }

        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1].toLowerCase() : "asc";
        Sort.Direction direction = "desc".equals(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<Book> bookPage = bookService.searchBooks(keyword, categoryId, pageable);

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "图书查询与列表");
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDirection);
        model.addAttribute("reverseSortDir", "asc".equals(sortDirection) ? "desc" : "asc");
        // 用户名和角色信息会由导航栏片段从 session 中获取
        return "books/search_list"; // 指向 templates/books/search_list.html
    }
}
