package com.maiphong.springbootmvc.controllers;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.maiphong.springbootmvc.dtos.Category.CategoryCreateDTO;
import com.maiphong.springbootmvc.dtos.Category.CategoryDTO;
import com.maiphong.springbootmvc.services.CategoryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String index(@RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "2") int size,
            Model model) {
        var pageable = PageRequest.of(page, size);

        pageable = order.equals("asc") ? PageRequest.of(page, size, Sort.by(sortBy).ascending())
                : PageRequest.of(page, size, Sort.by(sortBy).descending());

        var categories = categoryService.search(keyword, pageable);

        model.addAttribute("categories", categories);
        int totalPages = categories.getTotalPages();
        Long totalElements = categories.getTotalElements();

        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("order", order);
        model.addAttribute("pageSizes", new Integer[] { 1, 2, 3, 5, 10, 20 });
        return "categories/home";
    }

    @GetMapping("/create")
    public String create(Model model) {
        var categoryDTO = new CategoryDTO();
        model.addAttribute("categoryDTO", categoryDTO);
        return "categories/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute @Valid CategoryCreateDTO categoryCreateDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "categories/create";
        }
        categoryService.create(categoryCreateDTO);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        var categoryDTO = categoryService.findById(id);
        model.addAttribute("categoryDTO", categoryDTO);
        return "categories/edit";
    }

    @PostMapping("edit/{id}")
    public String edit(@PathVariable UUID id, @ModelAttribute CategoryDTO categoryDTO) {
        categoryService.update(id, categoryDTO);
        return "redirect:/categories";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable UUID id) {
        categoryService.delete(id);
        return "redirect:/categories";
    }

}
