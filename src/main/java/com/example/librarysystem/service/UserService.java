package com.example.librarysystem.service;

import com.example.librarysystem.dto.UserEditDto; // 新增 DTO
import com.example.librarysystem.dto.UserRegistrationDto;
import com.example.librarysystem.entity.User;
import org.springframework.data.domain.Page; // 引入 Page
import org.springframework.data.domain.Pageable; // 引入 Pageable
import java.util.Optional;
// import java.util.List; // 改为 Page

public interface UserService {
    User registerNewUser(UserRegistrationDto registrationDto) throws Exception;
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    // List<User> findAllUsers(); // 修改为返回 Page
    Page<User> findAllUsers(Pageable pageable); // 修改为返回 Page
    Optional<User> login(String username, String password);
    User updateUserAsAdmin(Long id, UserEditDto userEditDto) throws Exception; // 新增
    void deleteUser(Long id) throws Exception; // 新增
}
