package com.maiphong.springbootmvc.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.maiphong.springbootmvc.dtos.Category.CategoryCreateDTO;
import com.maiphong.springbootmvc.dtos.Category.CategoryDTO;

public interface CategoryService {
    List<CategoryDTO> findAll();

    CategoryDTO findById(UUID id);

    CategoryDTO create(CategoryCreateDTO categoryDTO);

    CategoryDTO update(UUID id, CategoryDTO categoryDTO);

    void delete(UUID id);

    List<CategoryDTO> search(String keyword);

    Page<CategoryDTO> search(String keyword, Pageable pageable);
}
