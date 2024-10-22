package com.maiphong.springbootmvc.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.maiphong.springbootmvc.dtos.Product.ProductCreateDTO;
import com.maiphong.springbootmvc.dtos.Product.ProductDTO;
import com.maiphong.springbootmvc.services.CategoryService;
import com.maiphong.springbootmvc.services.ProductService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String home(@RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "categoryName", required = false) String categoryName,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "2") int size,
            Model model) {
        var pageable = PageRequest.of(page, size);
        var products = productService.search(keyword, categoryName, pageable);

        int totalPages = products.getTotalPages();
        Long totalElements = products.getTotalElements();

        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("pageLimit", 2);
        model.addAttribute("pageSizes", new Integer[] { 1, 2, 3, 5, 10, 20 });
        var categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("categoryName", categoryName);
        return "products/home";
    }

    @GetMapping("/create")
    public String create(Model model) {
        var product = new ProductCreateDTO();
        model.addAttribute("productCreateDTO", product);

        var categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "products/create";
    }

    @PostMapping("/create")
    public String create(
            @ModelAttribute @Valid ProductCreateDTO productCreateDTO,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            if (productCreateDTO.getCategoryId() == null) {
                bindingResult.rejectValue("categoryId", "category", "Category is required");
            }
            var categories = categoryService.findAll();
            model.addAttribute("categories", categories);
            return "products/create";
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                byte[] bytes = imageFile.getBytes();
                Path path = Paths.get("src/main/resources/static/images/products/" + imageFile.getOriginalFilename());
                Files.write(path, bytes);
                productCreateDTO.setImage("/images/products/" + imageFile.getOriginalFilename());
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("message", "Failed to upload image");
                var categories = categoryService.findAll();
                model.addAttribute("categories", categories);

                bindingResult.rejectValue("image", "image", "Failed to upload image");
                return "products/create";
            }
        }

        productService.create(productCreateDTO);

        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        var productDTO = productService.findById(id);
        model.addAttribute("productDTO", productDTO);

        var categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "products/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, @ModelAttribute ProductDTO productDTO,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile, BindingResult bindingResult,
            Model model) {

        var oldProduct = productService.findById(id);

        // Case 1: User does not select a new image
        if (imageFile.getOriginalFilename().isEmpty()) {
            productDTO.setImage(oldProduct.getImage());
        } else {
            // Case 2: User selects a new image
            if (imageFile.getOriginalFilename() != null && !imageFile.getOriginalFilename().isEmpty()) {
                try {
                    byte[] bytes = imageFile.getBytes();
                    Path path = Paths
                            .get("src/main/resources/static/images/products/" + imageFile.getOriginalFilename());
                    Files.write(path, bytes);
                    productDTO.setImage("/images/products/" + imageFile.getOriginalFilename());
                } catch (Exception e) {
                    e.printStackTrace();
                    model.addAttribute("message", "Failed to upload image");
                    var categories = categoryService.findAll();
                    model.addAttribute("categories", categories);
                    bindingResult.rejectValue("image", "image", "Failed to upload image");
                    return "products/create";
                }
            }
        }

        productService.update(id, productDTO);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        productService.delete(id);
        return "redirect:/products";
    }

}
