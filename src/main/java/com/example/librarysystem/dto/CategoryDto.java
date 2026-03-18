package com.example.librarysystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;

    @NotBlank(message = "类别名称不能为空")
    @Size(max = 100, message = "类别名称长度不能超过100个字符")
    private String name;

    @Size(max = 500, message = "描述信息长度不能超过500个字符") // 给描述也加上长度限制
    private String description;
}