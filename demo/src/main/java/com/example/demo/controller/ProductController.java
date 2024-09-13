package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.Category;
import com.example.demo.service.ProductService;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService; // Thêm CategoryService

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product_list";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", productService.getAllCategories()); // Đảm bảo truyền đúng danh sách Category
        return "add_product";
    }


    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Product product, @RequestParam("categoryId") Long categoryId, Model model) {
        Optional<Category> categoryOptional = productService.getCategoryById(categoryId);
        if (categoryOptional.isPresent()) {
            product.setCategory(categoryOptional.get());
            productService.saveProduct(product);
            return "redirect:/admin/products";
        } else {
            model.addAttribute("error", "Category not found");
            return "error";
        }
    }



    @GetMapping("/info/{id}")
    public String viewProductInfo(@PathVariable("id") Long id, Model model) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            return "product_info";
        } else {
            model.addAttribute("error", "Product not found");
            return "error";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, Model model) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "update_product";
        } else {
            model.addAttribute("error", "Product not found");
            return "error";
        }
    }

    @PostMapping("/edit")
    public String updateProduct(@RequestParam("productId") Long productId,
                                @RequestParam("productName") String productName,
                                @RequestParam("description") String description,
                                @RequestParam("price") Double price,
                                @RequestParam("stockQuantity") Integer stockQuantity,
                                @RequestParam("categoryId") Long categoryId,
                                Model model) {

        Optional<Product> productOptional = productService.getProductById(productId);
        if (productOptional.isPresent()) {
            Product existingProduct = productOptional.get();
            existingProduct.setProductName(productName);
            existingProduct.setDescription(description);
            existingProduct.setPrice(price);
            existingProduct.setStockQuantity(stockQuantity);

            Optional<Category> categoryOptional = categoryService.getCategoryById(categoryId);
            if (categoryOptional.isPresent()) {
                existingProduct.setCategory(categoryOptional.get());
                productService.saveProduct(existingProduct);
                return "redirect:/admin/products";
            } else {
                model.addAttribute("error", "Category not found");
                return "update_product";
            }
        } else {
            model.addAttribute("error", "Product not found");
            return "error";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return "redirect:/admin/products";
    }
}
