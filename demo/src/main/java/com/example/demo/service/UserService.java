package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Lấy tất cả người dùng
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Lưu người dùng
    public void saveUser(User user) {
        userRepository.save(user);
    }

    // Lấy người dùng theo ID
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    // Xóa người dùng theo ID
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    // Lấy tất cả vai trò
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Lưu vai trò
    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    // Lấy vai trò theo ID
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }
}
