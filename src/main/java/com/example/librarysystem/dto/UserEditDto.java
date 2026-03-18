package com.example.librarysystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEditDto {
    private Long id;

    // 用户名通常不允许管理员修改，所以这里不包含 username
    // 如果需要显示，可以在表单中只读显示

    @NotBlank(message = "姓名不能为空")
    @Size(max = 100, message = "姓名长度不能超过100字符")
    private String name;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100字符")
    private String email; // 假设邮箱可以为空

    @Size(max = 20, message = "电话号码长度不能超过20字符")
    private String phone;

    @NotBlank(message = "角色不能为空")
    private String role; // "USER" 或 "ADMIN"
}
