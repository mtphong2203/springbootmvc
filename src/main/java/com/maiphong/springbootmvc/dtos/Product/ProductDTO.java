package com.maiphong.springbootmvc.dtos.Product;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import com.maiphong.springbootmvc.dtos.Category.CategoryDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private UUID id;

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is not empty")
    @Length(min = 3, max = 50)
    private String name;

    @NotNull(message = "Can not null")
    @PositiveOrZero(message = "Price must be greater than or equal to 0")
    private double unitPrice;

    @NotNull(message = "Can not null")
    @PositiveOrZero(message = "Price must be greater than or equal to 0")
    private int unitStock;

    @Length(max = 255, message = "max is 255 characters")
    private String image;

    private UUID categoryId;

    @NotNull(message = "Not null")
    private CategoryDTO category;

}