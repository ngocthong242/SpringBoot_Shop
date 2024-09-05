package com.example.demo.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        int status = (Integer) request.getAttribute("javax.servlet.error.status_code");

        if (status == 403) {
            return "error/403"; // Trả về trang lỗi 403 tùy chỉnh
        }

        return "error/error"; // Trả về trang lỗi chung
    }
}
