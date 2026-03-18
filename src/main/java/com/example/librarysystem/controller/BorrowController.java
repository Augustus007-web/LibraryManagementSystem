package com.example.librarysystem.controller;

import com.example.librarysystem.entity.BorrowRecord;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.service.BorrowService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*; // 需要 PostMapping, PathVariable
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import java.util.List; // 使用 Page 后可以不用 List

@Controller
public class BorrowController {

    private final BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    private User getLoggedInUser(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }

    @PostMapping("/borrow/{bookId}")
    public String borrowBookAction(@PathVariable("bookId") Long bookId,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        User loggedInUser = getLoggedInUser(session);
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "请先登录后再借阅图书。");
            return "redirect:/login";
        }

        try {
            borrowService.borrowBook(loggedInUser.getId(), bookId);
            redirectAttributes.addFlashAttribute("successMessage", "图书借阅成功！请在30天内归还。");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "借阅失败：" + e.getMessage());
        }
        // 重定向回图书列表页或用户借阅页，这里我们重定向回图书搜索/列表页
        // 可以考虑在URL中保留之前的搜索参数，但这会使逻辑复杂化，暂时简化
        return "redirect:/books/search";
    }

    @PostMapping("/return/{borrowRecordId}")
    public String returnBookAction(@PathVariable("borrowRecordId") Long borrowRecordId,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        User loggedInUser = getLoggedInUser(session);
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "请先登录。");
            return "redirect:/login";
        }

        try {
            borrowService.returnBook(borrowRecordId, loggedInUser.getId());
            redirectAttributes.addFlashAttribute("successMessage", "图书归还成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "还书失败：" + e.getMessage());
        }
        return "redirect:/my-borrows"; // 重定向到我的借阅页面
    }

    @GetMapping("/my-borrows")
    public String listMyBorrows(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size, // 每页显示5条
                                Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = getLoggedInUser(session);
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "请先登录查看借阅记录。");
            return "redirect:/login";
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("borrowDate").descending());
        Page<BorrowRecord> borrowPage = borrowService.findBorrowRecordsByUser(loggedInUser, pageable);

        model.addAttribute("borrowPage", borrowPage);
        model.addAttribute("pageTitle", "我的借阅记录");
        // session中的username和userRole应该由导航栏片段直接使用
        return "user/my_borrows_list";
    }
}