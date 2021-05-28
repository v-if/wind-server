package com.github.tkpark.wind;

import lombok.Data;

import static org.springframework.beans.BeanUtils.copyProperties;

@Data
public class RoadMasterDto {

    private String road;

    private String roadNm;

    private int seq;

    private String imageFileName;

    private String latitude;

    private String longitude;

    public RoadMasterDto(RoadMaster source) {
        copyProperties(source, this);
    }
}
