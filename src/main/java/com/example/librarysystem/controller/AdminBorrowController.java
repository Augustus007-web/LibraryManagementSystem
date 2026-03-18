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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/borrows")
public class AdminBorrowController {

    private final BorrowService borrowService;

    @Autowired
    public AdminBorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
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
    public String listAllBorrows(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "borrowDate,desc") String[] sort,
                                 Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session, redirectAttributes)) {
            return (session.getAttribute("loggedInUser") == null) ? "redirect:/login" : "redirect:/";
        }

        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1].toLowerCase() : "desc";
        Sort.Direction direction = "asc".equals(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<BorrowRecord> borrowPage = borrowService.findAllBorrowRecords(pageable);

        model.addAttribute("borrowPage", borrowPage);
        model.addAttribute("pageTitle", "所有借阅记录");
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDirection);
        model.addAttribute("reverseSortDir", "asc".equals(sortDirection) ? "desc" : "asc");
        return "admin/borrows_list";
    }
}