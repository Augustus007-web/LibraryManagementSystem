package com.example.librarysystem.dto;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Data
public class UserRegistrationDto {
    @NotEmpty(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3到50之间")
    private String username;

    @NotEmpty(message = "密码不能为空")
    @Size(min = 6, message = "密码长度至少为6位")
    private String password;

    private String name;

    @Email(message = "邮箱格式不正确") // 对于可选字段，如果为空则不校验格式，除非配合 @NotEmpty
    private String email;

    private String phone;

    private String role;
}