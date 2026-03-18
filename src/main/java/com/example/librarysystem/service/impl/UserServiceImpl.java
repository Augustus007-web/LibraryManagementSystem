package com.example.librarysystem.service.impl;

import com.example.librarysystem.dto.UserEditDto;
import com.example.librarysystem.dto.UserRegistrationDto;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.repository.BorrowRecordRepository; // 引入 BorrowRecordRepository
import com.example.librarysystem.repository.UserRepository;
import com.example.librarysystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 引入 Transactional

import java.time.LocalDateTime;
import java.util.Arrays; // 引入 Arrays
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRecordRepository; // 注入

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BorrowRecordRepository borrowRecordRepository) {
        this.userRepository = userRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Override
    @Transactional
    public User registerNewUser(UserRegistrationDto registrationDto) throws Exception {
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new Exception("用户名已存在: " + registrationDto.getUsername());
        }
        if (registrationDto.getEmail() != null && !registrationDto.getEmail().isEmpty() && userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new Exception("邮箱已注册: " + registrationDto.getEmail());
        }

        User newUser = new User();
        newUser.setUsername(registrationDto.getUsername());
        newUser.setPassword(registrationDto.getPassword()); // 明文密码
        newUser.setName(registrationDto.getName());
        newUser.setEmail(registrationDto.getEmail());
        newUser.setPhone(registrationDto.getPhone());

        String role = registrationDto.getRole();
        if (role == null || (!role.equalsIgnoreCase("USER") && !role.equalsIgnoreCase("ADMIN"))) {
            newUser.setRole("USER");
        } else {
            newUser.setRole(role.toUpperCase());
        }
        // createdAt 和 updatedAt 由 @PrePersist 处理
        return userRepository.save(newUser);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Page<User> findAllUsers(Pageable pageable) { // 修改为返回 Page
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> login(String username, String rawPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(rawPassword)) { // 明文比较
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public User updateUserAsAdmin(Long id, UserEditDto userEditDto) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("未找到ID为 " + id + " 的用户。"));

        // 检查邮箱唯一性 (如果邮箱被修改了)
        if (userEditDto.getEmail() != null && !userEditDto.getEmail().equalsIgnoreCase(user.getEmail())) {
            if (userRepository.existsByEmail(userEditDto.getEmail())) {
                throw new Exception("邮箱 '" + userEditDto.getEmail() + "' 已被其他用户注册。");
            }
            user.setEmail(userEditDto.getEmail());
        } else if (userEditDto.getEmail() == null || userEditDto.getEmail().isEmpty()) {
            user.setEmail(null); // 允许清空邮箱
        }


        user.setName(userEditDto.getName());
        user.setPhone(userEditDto.getPhone());

        String newRole = userEditDto.getRole().toUpperCase();
        if (!"USER".equals(newRole) && !"ADMIN".equals(newRole)) {
            throw new Exception("无效的用户角色：" + userEditDto.getRole());
        }
        // 防止管理员把自己改成普通用户后无法操作 (或者添加其他逻辑，比如至少保留一个ADMIN)
        if (user.getRole().equals("ADMIN") && id.equals(1L) && newRole.equals("USER")) { // 假设ID为1的是超级管理员
            // throw new Exception("不能将主要的管理员账户角色修改为普通用户。");
            // 或者允许，但要有警告
        }
        user.setRole(newRole);
        user.setUpdatedAt(LocalDateTime.now()); // @PreUpdate 也会处理
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("未找到ID为 " + id + " 的用户，无法删除。"));

        // 检查用户是否有未归还的借阅记录
        if (borrowRecordRepository.existsByUserAndStatusIn(user, Arrays.asList("BORROWED", "OVERDUE"))) {
            throw new Exception("无法删除用户 '" + user.getUsername() + "'，该用户尚有未归还的图书。");
        }

        // 如果是系统内最后一个管理员，可能需要阻止删除
        if ("ADMIN".equals(user.getRole())) {
            List<User> admins = userRepository.findAll().stream().filter(u -> "ADMIN".equals(u.getRole())).toList();
            if (admins.size() == 1 && admins.get(0).getId().equals(id)) {
                throw new Exception("无法删除系统中最后一个管理员账户。");
            }
        }


        userRepository.deleteById(id);
    }
}
