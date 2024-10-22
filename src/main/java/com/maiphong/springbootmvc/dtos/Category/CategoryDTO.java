package com.maiphong.springbootmvc.dtos.Category;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private UUID id;

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is not empty")
    @Length(min = 5, max = 500, message = "3->500 characters")
    private String name;

    @NotNull(message = "Can not null")
    @Length(max = 500, message = "maximum 500 characters")
    private String description;

}