package com.example.demo.startup;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Kiểm tra xem vai trò Admin đã tồn tại chưa
        if (roleRepository.findByRoleName("ROLE_ADMIN") == null) {
            Role adminRole = new Role();
            adminRole.setRoleName("ROLE_ADMIN"); // Sử dụng đúng phương thức setRoleName
            roleRepository.save(adminRole);
        }

        // Kiểm tra xem tài khoản Admin đã tồn tại chưa
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin")); // Mã hóa mật khẩu
            admin.setEmail("admin@example.com");
            admin.setRole(roleRepository.findByRoleName("ROLE_ADMIN"));
            userRepository.save(admin);
        }
    }
}
