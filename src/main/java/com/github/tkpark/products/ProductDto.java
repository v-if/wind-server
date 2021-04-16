package com.github.tkpark.products;

import lombok.Data;

import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;

@Data
public class ProductDto {

    private Long seq;

    private String name;

    private String details;

    private int reviewCount;

    private LocalDateTime createAt;

    public ProductDto(Product source) {
        copyProperties(source, this);

        this.details = source.getDetails().orElse(null);
    }

}