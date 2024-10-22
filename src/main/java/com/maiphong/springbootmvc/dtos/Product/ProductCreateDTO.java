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
public class ProductCreateDTO {
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is not empty")
    @Length(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    private String name;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be greater than or equal to 0")
    private double unitPrice;

    @NotNull(message = "Stock is required")
    @PositiveOrZero(message = "Stock must be greater than or equal to 0")
    private int unitStock;

    @Length(max = 255, message = "Image must be less than 255 characters")
    private String image;

    @NotNull(message = "Category is required")
    private UUID categoryId;

}
