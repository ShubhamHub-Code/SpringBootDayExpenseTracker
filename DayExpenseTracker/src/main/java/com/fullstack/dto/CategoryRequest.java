package com.fullstack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {

    @Schema(description = "Name of the category", example = "Electronics", required = true)
    @NotBlank(message = "Category name is mandatory")
    private String name;

    @Schema(description = "Optional description of the category", example = "Devices and gadgets")
    private String description;
}
