package com.fullstack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResp {

    @Schema(description = "Unique category ID", example = "11")
    private Long id;

    @Schema(description = "Name of the category", example = "Electronics")
    private String name;

    @Schema(description = "Optional description of the category", example = "Devices and gadgets")
    private String description;
}
