package com.maiphong.springbootmvc.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maiphong.springbootmvc.dtos.Category.CategoryCreateDTO;
import com.maiphong.springbootmvc.dtos.Category.CategoryDTO;
import com.maiphong.springbootmvc.entities.Category;
import com.maiphong.springbootmvc.repositories.CategoryRepository;

import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDTO> findAll() {
        var categories = categoryRepository.findAll();

        var categoryDtos = categories.stream().map(category -> {
            var categoryDto = new CategoryDTO();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            categoryDto.setDescription(category.getDescription());
            return categoryDto;
        }).toList();
        return categoryDtos;
    }

    @Override
    public CategoryDTO findById(UUID id) {
        var category = categoryRepository.findById(id).orElse(null);

        if (category == null) {
            return null;
        }
        var categoryDto = new CategoryDTO();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        return categoryDto;
    }

    @Override
    public CategoryDTO create(CategoryCreateDTO categoryCreateDTO) {
        if (categoryCreateDTO == null) {
            throw new IllegalArgumentException("Null");

        }
        var existCategory = categoryRepository.findByName(categoryCreateDTO.getName());

        if (existCategory != null) {
            throw new IllegalArgumentException("Already Exist!");
        }

        var category = new Category();
        category.setName(categoryCreateDTO.getName());
        category.setDescription(categoryCreateDTO.getDescription());

        category = categoryRepository.save(category);

        var updateCategoryDto = new CategoryDTO();
        updateCategoryDto.setId(category.getId());
        updateCategoryDto.setName(category.getName());
        updateCategoryDto.setDescription(category.getDescription());
        return updateCategoryDto;
    }

    @Override
    public CategoryDTO update(UUID id, CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            throw new IllegalArgumentException("Null");

        }
        var existCategory = categoryRepository.findByName(categoryDTO.getName());

        if (existCategory != null) {
            throw new IllegalArgumentException("Already Exist!");
        }

        var category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new IllegalArgumentException("Not found!");
        }

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        category = categoryRepository.save(category);

        var updateCategoryDto = new CategoryDTO();
        updateCategoryDto.setId(category.getId());
        updateCategoryDto.setName(category.getName());
        updateCategoryDto.setDescription(category.getDescription());
        return updateCategoryDto;
    }

    @Override
    public void delete(UUID id) {
        var category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new IllegalArgumentException("Not found!");
        }
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDTO> search(String keyword) {
        // Find category by keyword
        Specification<Category> specification = (root, query, criteriaBuilder) -> {
            // Neu keyword null thi tra ve null
            if (keyword == null) {
                return null;
            }

            // Neu keyword khong null
            // WHERE LOWER(name) LIKE %keyword%
            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                    "%" + keyword.toLowerCase() + "%");

            // WHERE LOWER(description) LIKE %keyword%
            Predicate desPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                    "%" + keyword.toLowerCase() + "%");

            // WHERE LOWER(name) LIKE %keyword% OR LOWER(description) LIKE %keyword%
            return criteriaBuilder.or(namePredicate, desPredicate);
        };

        var categories = categoryRepository.findAll(specification);

        // Covert List<Category> to List<CategoryDTO>
        var categoryDTOs = categories.stream().map(category -> {
            var categoryDTO = new CategoryDTO();
            categoryDTO.setId(category.getId());
            categoryDTO.setName(category.getName());
            categoryDTO.setDescription(category.getDescription());
            return categoryDTO;
        }).toList();

        return categoryDTOs;
    }

    @Override
    public Page<CategoryDTO> search(String keyword, Pageable pageable) {
        Specification<Category> specification = (root, query, criteriaBuilder) -> {
            if (keyword == null) {
                return null;
            }

            // WHERE name LIKE %keyword% OR description LIKE %keyword%
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                            "%" + keyword.toLowerCase() + "%"));
        };

        Page<Category> categories = categoryRepository.findAll(specification, pageable);

        Page<CategoryDTO> categoryDTOs = categories.map(category -> {
            var categoryDTO = new CategoryDTO();
            categoryDTO.setId(category.getId());
            categoryDTO.setName(category.getName());
            categoryDTO.setDescription(category.getDescription());

            return categoryDTO;
        });

        return categoryDTOs;
    }

}
