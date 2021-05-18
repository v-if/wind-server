package com.github.tkpark.wind;

import lombok.Data;

import static org.springframework.beans.BeanUtils.copyProperties;

@Data
public class RoadPointDto {

    private String road;

    private String roadPoint;

    private String roadNm;

    private String nx;

    private String ny;

    public RoadPointDto(RoadPoint source) {
        copyProperties(source, this);
    }
}
