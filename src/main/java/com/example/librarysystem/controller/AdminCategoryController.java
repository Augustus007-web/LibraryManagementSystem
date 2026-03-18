package com.example.librarysystem.controller;

import com.example.librarysystem.dto.CategoryDto;
import com.example.librarysystem.entity.Category;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @Autowired
    public AdminCategoryController(CategoryService categoryService) {
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
    public String listCategories(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session, redirectAttributes)) {
            return (session.getAttribute("loggedInUser") == null) ? "redirect:/login" : "redirect:/";
        }
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("pageTitle", "图书类别管理");
        return "admin/categories_list";
    }

    @GetMapping("/add")
    public String showAddCategoryForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session, redirectAttributes)) {
            return (session.getAttribute("loggedInUser") == null) ? "redirect:/login" : "redirect:/";
        }
        model.addAttribute("categoryDto", new CategoryDto());
        model.addAttribute("pageTitle", "添加新类别");
        return "admin/category_form";
    }

    @GetMapping("/edit/{id}")
    public String showEditCategoryForm(@PathVariable("id") Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session, redirectAttributes)) {
            return (session.getAttribute("loggedInUser") == null) ? "redirect:/login" : "redirect:/";
        }
        Optional<Category> categoryOptional = categoryService.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            // 将实体转换为DTO以便表单绑定
            CategoryDto categoryDto = new CategoryDto(category.getId(), category.getName(), category.getDescription());
            model.addAttribute("categoryDto", categoryDto);
            model.addAttribute("pageTitle", "编辑类别");
            return "admin/category_form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "未找到ID为 " + id + " 的类别。");
            return "redirect:/admin/categories";
        }
    }

    @PostMapping("/save")
    public String saveCategory(@Valid @ModelAttribute("categoryDto") CategoryDto categoryDto,
                               BindingResult result, Model model, // 添加 Model
                               HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session, redirectAttributes)) {
            return (session.getAttribute("loggedInUser") == null) ? "redirect:/login" : "redirect:/";
        }

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", categoryDto.getId() == null ? "添加新类别" : "编辑类别");
            // 如果校验失败，需要重新填充模型数据，例如类别列表（如果表单需要）
            return "admin/category_form";
        }

        try {
            if (categoryDto.getId() == null) { // 新增
                categoryService.save(categoryDto);
                redirectAttributes.addFlashAttribute("successMessage", "类别添加成功！");
            } else { // 编辑
                categoryService.update(categoryDto.getId(), categoryDto);
                redirectAttributes.addFlashAttribute("successMessage", "类别更新成功！");
            }
        } catch (Exception e) {
            // 如果保存/更新过程中抛出业务异常 (如名称重复)
            model.addAttribute("errorMessage", "操作失败: " + e.getMessage());
            model.addAttribute("pageTitle", categoryDto.getId() == null ? "添加新类别" : "编辑类别");
            // 确保 categoryDto 对象仍然在模型中，以便回显
            // model.addAttribute("categoryDto", categoryDto); // @ModelAttribute 应该会自动处理
            return "admin/category_form";
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session, redirectAttributes)) {
            return (session.getAttribute("loggedInUser") == null) ? "redirect:/login" : "redirect:/";
        }
        try {
            categoryService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "类别删除成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "删除失败: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }
}