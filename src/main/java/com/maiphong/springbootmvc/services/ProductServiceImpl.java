package com.maiphong.springbootmvc.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maiphong.springbootmvc.dtos.Category.CategoryDTO;
import com.maiphong.springbootmvc.dtos.Product.ProductCreateDTO;
import com.maiphong.springbootmvc.dtos.Product.ProductDTO;
import com.maiphong.springbootmvc.entities.Category;
import com.maiphong.springbootmvc.entities.Product;
import com.maiphong.springbootmvc.repositories.ProductRepository;

import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> findAll() {
        var products = productRepository.findAll();

        var productDtos = products.stream().map(product -> {
            var productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setUnitPrice(product.getUnitPrice());
            productDTO.setUnitStock(product.getUnitStock());
            productDTO.setImage(product.getImage());

            if (product.getCategory() != null) {
                productDTO.setCategoryId(product.getCategory().getId());
                var categoryDTO = new CategoryDTO();
                categoryDTO.setId(product.getCategory().getId());
                categoryDTO.setName(product.getCategory().getName());
                categoryDTO.setDescription(product.getCategory().getDescription());
                productDTO.setCategory(categoryDTO);
            }
            return productDTO;

        }).toList();
        return productDtos;
    }

    @Override
    public ProductDTO findById(UUID id) {
        var product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return null;
        }
        var productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setUnitPrice(product.getUnitPrice());
        productDTO.setUnitStock(product.getUnitStock());
        productDTO.setImage(product.getImage());

        if (product.getCategory() != null) {
            productDTO.setCategoryId(product.getCategory().getId());
            var categoryDTO = new CategoryDTO();
            categoryDTO.setId(product.getCategory().getId());
            categoryDTO.setName(product.getCategory().getName());
            categoryDTO.setDescription(product.getCategory().getDescription());
            productDTO.setCategory(categoryDTO);
        }

        return productDTO;

    }

    @Override
    public ProductDTO create(ProductCreateDTO productCreateDTO) {
        // Kiem tra productDTO null
        if (productCreateDTO == null) {
            throw new IllegalArgumentException("ProductDTO is required");
        }

        // Convert ProductDTO to Product
        var product = new Product();
        product.setName(productCreateDTO.getName());
        product.setUnitPrice(product.getUnitPrice());
        product.setUnitStock(product.getUnitStock());
        product.setImage(productCreateDTO.getImage());

        // Kiem tra xem category co duoc select hay khong
        // Neu co thi product co category va can set category cho product do
        if (productCreateDTO.getCategoryId() != null) {
            var category = new Category();
            category.setId(productCreateDTO.getCategoryId());
            product.setCategory(category);
        }

        // Save product
        product = productRepository.save(product);

        // Convert Product to ProductDTO
        var newProductDTO = new ProductDTO();
        newProductDTO.setId(product.getId());
        newProductDTO.setName(product.getName());
        newProductDTO.setUnitPrice(product.getUnitPrice());
        newProductDTO.setUnitStock(product.getUnitStock());
        newProductDTO.setImage(product.getImage());

        // Neu product co category thi set category id cho productDTO
        if (product.getCategory() != null) {
            newProductDTO.setCategoryId(product.getCategory().getId());
        }

        return newProductDTO;
    }

    @Override
    public ProductDTO update(UUID id, ProductDTO productDTO) {
        if (productDTO == null) {
            throw new IllegalArgumentException("Null");
        }

        var product = productRepository.findById(id).orElse(null);

        if (product == null) {
            throw new IllegalArgumentException("Not found");
        }

        product.setName(productDTO.getName());
        product.setUnitPrice(productDTO.getUnitPrice());
        product.setUnitStock(productDTO.getUnitStock());
        product.setImage(productDTO.getImage());
        if (productDTO.getCategoryId() != null) {
            var category = new Category();
            category.setId(productDTO.getCategoryId());
            product.setCategory(category);
        }

        product = productRepository.save(product);

        var productDTOConvert = new ProductDTO();
        productDTOConvert.setId(product.getId());
        productDTOConvert.setName(product.getName());
        productDTOConvert.setUnitPrice(product.getUnitPrice());
        productDTOConvert.setUnitStock(product.getUnitStock());
        productDTOConvert.setImage(product.getImage());
        if (product.getCategory() != null) {
            productDTOConvert.setCategoryId(product.getCategory().getId());
        }

        return productDTOConvert;

    }

    @Override
    public void delete(UUID id) {
        var product = productRepository.findById(id).orElse(null);

        if (product == null) {
            throw new IllegalArgumentException("Not found");
        }
        productRepository.delete(product);
    }

    @Override
    public List<ProductDTO> search(String keyword) {
        Specification<Product> specification = (root, query, criteriaBuilder) -> {
            if (keyword == null) {
                return null;
            }

            // WHERE name LIKE %keyword% OR description LIKE %keyword%
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
        };

        var products = productRepository.findAll(specification);

        var productDTOs = products.stream().map(product -> {
            var productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setUnitPrice(product.getUnitPrice());
            productDTO.setUnitStock(product.getUnitStock());
            productDTO.setImage(product.getImage());

            if (product.getCategory() != null) {
                productDTO.setCategoryId(product.getCategory().getId());
                var categoriesDTO = new CategoryDTO();
                categoriesDTO.setId(product.getCategory().getId());
                categoriesDTO.setName(product.getCategory().getName());
                categoriesDTO.setDescription(product.getCategory().getDescription());

                productDTO.setCategory(categoriesDTO);

            }

            return productDTO;
        }).toList();

        return productDTOs;
    }

    @Override
    public Page<ProductDTO> search(String keyword, Pageable pageable) {
        Specification<Product> specification = (root, query, criteriaBuilder) -> {
            if (keyword == null) {
                return null;
            }

            // WHERE name LIKE %keyword% OR description LIKE %keyword%
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
        };

        Page<Product> products = productRepository.findAll(specification, pageable);

        Page<ProductDTO> productDTOs = products.map(product -> {
            var productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setUnitPrice(product.getUnitPrice());
            productDTO.setUnitStock(product.getUnitStock());
            productDTO.setImage(product.getImage());

            if (product.getCategory() != null) {
                productDTO.setCategoryId(product.getCategory().getId());
                var categoriesDTO = new CategoryDTO();
                categoriesDTO.setId(product.getCategory().getId());
                categoriesDTO.setName(product.getCategory().getName());
                categoriesDTO.setDescription(product.getCategory().getDescription());

                productDTO.setCategory(categoriesDTO);

            }

            return productDTO;
        });

        return productDTOs;
    }

    @Override
    public Page<ProductDTO> search(String keyword, String categoryName, Pageable pageable) {
        Specification<Product> specification = (root, query, criteriaBuilder) -> {
            if (keyword == null) {
                return null;
            }

            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                    "%" + keyword.toLowerCase() + "%");

            if (categoryName == null || categoryName.isBlank()) {
                return namePredicate;

            }

            // WHERE description LIKE %keyword%
            Predicate categoryNamePredicate = criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("category").get("name")),
                    categoryName.toLowerCase());

            // WHERE (name LIKE %keyword% OR description LIKE %keyword%) AND category.name =
            // categoryName
            return criteriaBuilder.and(namePredicate, categoryNamePredicate);
        };

        Page<Product> products = productRepository.findAll(specification, pageable);

        Page<ProductDTO> productDTOs = products.map(product -> {
            var productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setUnitPrice(product.getUnitPrice());
            productDTO.setUnitStock(product.getUnitStock());
            productDTO.setImage(product.getImage());

            if (product.getCategory() != null) {
                productDTO.setCategoryId(product.getCategory().getId());
                var categoriesDTO = new CategoryDTO();
                categoriesDTO.setId(product.getCategory().getId());
                categoriesDTO.setName(product.getCategory().getName());
                categoriesDTO.setDescription(product.getCategory().getDescription());

                productDTO.setCategory(categoriesDTO);

            }

            return productDTO;
        });

        return productDTOs;
    }

}
