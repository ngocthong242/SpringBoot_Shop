package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.entity.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Inject passwordEncoder


    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user_list";
    }
    @GetMapping("/admin/users/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        return "add_user";
    }

    @PostMapping("/admin/users/add")
    public String addUser(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("email") String email,
                          @RequestParam("roleId") Long roleId,
                          Model model) {
        // Kiểm tra xem username đã tồn tại chưa
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username already exists!");
            model.addAttribute("roles", roleRepository.findAll());
            return "add_user";
        }

        // Tạo người dùng mới
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password)); // Mã hóa mật khẩu
        newUser.setEmail(email);

        // Gán vai trò cho người dùng
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role != null) {
            newUser.setRole(role);
        } else {
            model.addAttribute("error", "Role not found");
            return "add_user";
        }

        userRepository.save(newUser); // Lưu người dùng mới vào cơ sở dữ liệu
        return "redirect:/admin/users";
    }
    @GetMapping("/admin/users/info/{id}")
    public String viewUserInfo(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user_info";
        } else {
            model.addAttribute("error", "User not found");
            return "error";
        }
    }

    @GetMapping("/admin/users/edit-role/{id}")
    public String showEditRoleForm(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("roles", roleRepository.findAll());
            return "edit_role";
        } else {
            model.addAttribute("error", "User not found");
            return "error";
        }
    }

    // Cập nhật vai trò người dùng
    @PostMapping("/admin/users/edit-role")
    public String updateRole(@RequestParam("id") Long userId, @RequestParam("roleId") Long roleId, Model model) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Role newRole = roleRepository.findById(roleId).orElse(null);
            if (newRole != null) {
                user.setRole(newRole);
                userRepository.save(user); // Lưu lại người dùng với vai trò mới
            } else {
                model.addAttribute("error", "Role not found");
                return "error";
            }
            return "redirect:/admin/users"; // Quay về danh sách người dùng sau khi cập nhật
        } else {
            model.addAttribute("error", "User not found");
            return "error";
        }
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }
}
