package com.example.librarysystem.controller;

import com.example.librarysystem.dto.UserEditDto;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.service.UserService;
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

import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
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
    public String listUsers(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(defaultValue = "id,asc") String[] sort,
                            Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session, redirectAttributes)) {
            return (session.getAttribute("loggedInUser") == null) ? "redirect:/login" : "redirect:/";
        }

        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1].toLowerCase() : "asc";
        Sort.Direction direction = "desc".equals(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<User> userPage = userService.findAllUsers(pageable);
        model.addAttribute("userPage", userPage);
        model.addAttribute("pageTitle", "用户管理");
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDirection);
        model.addAttribute("reverseSortDir", "asc".equals(sortDirection) ? "desc" : "asc");
        return "admin/users_list";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session, redirectAttributes)) {
            return (session.getAttribute("loggedInUser") == null) ? "redirect:/login" : "redirect:/";
        }
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserEditDto userEditDto = new UserEditDto(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole());
            model.addAttribute("userEditDto", userEditDto);
            model.addAttribute("username", user.getUsername()); // 用于在表单中只读显示用户名
            model.addAttribute("pageTitle", "编辑用户 - " + user.getUsername());
            return "admin/user_form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "未找到ID为 " + id + " 的用户。");
            return "redirect:/admin/users";
        }
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("userEditDto") UserEditDto userEditDto,
                             BindingResult result, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session, redirectAttributes)) {
            return (session.getAttribute("loggedInUser") == null) ? "redirect:/login" : "redirect:/";
        }

        User userToDisplayUsername = userService.findById(id).orElse(null); // 用于获取用户名以回显
        if (userToDisplayUsername == null) { // 双重检查，理论上 showEditUserForm 已处理
            redirectAttributes.addFlashAttribute("errorMessage", "尝试编辑的用户不存在。");
            return "redirect:/admin/users";
        }
        model.addAttribute("username", userToDisplayUsername.getUsername());


        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "编辑用户 - " + userToDisplayUsername.getUsername());
            // userEditDto 已经通过 @ModelAttribute 添加回模型了
            return "admin/user_form";
        }

        try {
            userService.updateUserAsAdmin(id, userEditDto);
            redirectAttributes.addFlashAttribute("successMessage", "用户 " + userToDisplayUsername.getUsername() + " 信息更新成功！");
            return "redirect:/admin/users";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "更新失败: " + e.getMessage());
            model.addAttribute("pageTitle", "编辑用户 - " + userToDisplayUsername.getUsername());
            // model.addAttribute("userEditDto", userEditDto); // 确保回显
            return "admin/user_form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session, redirectAttributes)) {
            return (session.getAttribute("loggedInUser") == null) ? "redirect:/login" : "redirect:/";
        }

        // 防止管理员删除自己
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "不能删除当前登录的管理员账户！");
            return "redirect:/admin/users";
        }

        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "用户删除成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "删除失败: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}