package com.maiphong.springbootmvc.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maiphong.springbootmvc.entities.Product;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByName(String name);

    double getStockById(UUID id);

    List<Product> findByCategoryIsNull();

    List<Product> findByCategoryName(String name);
}
