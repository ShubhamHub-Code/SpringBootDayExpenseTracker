package com.fullstack.dto;

import lombok.Data;

@Data
public class CategoryRequest {

    private String name;

    private String description;

    private long userID;
}
