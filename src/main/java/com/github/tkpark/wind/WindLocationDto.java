package com.github.tkpark.wind;

import lombok.Data;

import static org.springframework.beans.BeanUtils.copyProperties;

@Data
public class WindLocationDto {

    private String id;

    private String location1;

    private String location2;

    private String location3;

    private String nx;

    private String ny;

    private String longitude;

    private String latitude;

    public WindLocationDto(WindLocation source) {
        copyProperties(source, this);
    }
}
