package com.maiphong.springbootmvc.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.maiphong.springbootmvc.dtos.Product.ProductCreateDTO;
import com.maiphong.springbootmvc.dtos.Product.ProductDTO;

public interface ProductService {
    List<ProductDTO> findAll();

    ProductDTO findById(UUID id);

    ProductDTO create(ProductCreateDTO productDTO);

    ProductDTO update(UUID id, ProductDTO productDTO);

    void delete(UUID id);

    List<ProductDTO> search(String keyword);

    Page<ProductDTO> search(String keyword, Pageable pageable);

    Page<ProductDTO> search(String keyword, String categoryName, Pageable pageable);

}
