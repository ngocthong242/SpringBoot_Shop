package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "category_list";
    }

    @GetMapping("/add")
    public String showAddCategoryForm(Model model) {
        return "add_category";
    }

    @PostMapping("/add")
    public String addCategory(@RequestParam("categoryName") String categoryName,
                              @RequestParam("description") String description,
                              Model model) {

        Category newCategory = new Category();
        newCategory.setCategoryName(categoryName);
        newCategory.setDescription(description);

        categoryService.saveCategory(newCategory);
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditCategoryForm(@PathVariable("id") Long id, Model model) {
        Optional<Category> categoryOptional = categoryService.getCategoryById(id);
        if (categoryOptional.isPresent()) {
            model.addAttribute("category", categoryOptional.get());
            return "update_category";
        } else {
            model.addAttribute("error", "Category not found");
            return "error";
        }
    }

    @PostMapping("/edit")
    public String updateCategory(@RequestParam("categoryId") Long categoryId,
                                 @RequestParam("categoryName") String categoryName,
                                 @RequestParam("description") String description,
                                 Model model) {

        Optional<Category> categoryOptional = categoryService.getCategoryById(categoryId);
        if (categoryOptional.isPresent()) {
            Category existingCategory = categoryOptional.get();
            existingCategory.setCategoryName(categoryName);
            existingCategory.setDescription(description);

            categoryService.saveCategory(existingCategory);
            return "redirect:/admin/categories";
        } else {
            model.addAttribute("error", "Category not found");
            return "error";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/admin/categories";
    }
}
