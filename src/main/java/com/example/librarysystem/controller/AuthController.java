package com.example.librarysystem.controller;

import com.example.librarysystem.dto.UserRegistrationDto;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, HttpSession session) { // 添加 HttpSession
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/"; // 如果已登录，直接跳转到首页
        }
        // 从 redirectAttributes 中获取可能的 error 和 logout 参数
        if (model.containsAttribute("error")) { // Thymeleaf 会自动处理 flash attributes
            model.addAttribute("errorMessage", "用户名或密码错误！");
        }
        if (model.containsAttribute("logout")) {
            model.addAttribute("successMessage", "您已成功退出登录。");
        }
        return "auth/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        Optional<User> userOptional = userService.login(username, password);
        if (userOptional.isPresent()) {
            User loggedInUser = userOptional.get();
            session.setAttribute("loggedInUser", loggedInUser);
            redirectAttributes.addFlashAttribute("successMessage", "登录成功！");
            if ("ADMIN".equals(loggedInUser.getRole())) {
                return "redirect:/admin/dashboard";//管理员跳转后台
            }
            return "redirect:/";//普通用户跳转首页
        } else {
            // 使用 RedirectAttributes 将错误消息传递到 GET /login
            redirectAttributes.addFlashAttribute("error", "true");
            // model.addAttribute("errorMessage", "用户名或密码错误"); // 这行在 POST 后重定向时无效
            return "redirect:/login"; // 重定向到登录页以显示错误
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDto") UserRegistrationDto userDto,
                               BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        try {
            // 确保注册时角色被正确处理，例如默认为 "USER"
            if (userDto.getRole() == null || userDto.getRole().isEmpty()) {
                userDto.setRole("USER");
            } else {
                userDto.setRole(userDto.getRole().toUpperCase());
            }
            userService.registerNewUser(userDto);
            redirectAttributes.addFlashAttribute("successMessage", "注册成功！请登录。");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("logout", "true"); // 用于在登录页显示登出成功消息
        return "redirect:/login";
    }
}