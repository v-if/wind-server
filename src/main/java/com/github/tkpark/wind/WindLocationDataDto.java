package com.github.tkpark.wind;

import lombok.Data;

import static org.springframework.beans.BeanUtils.copyProperties;

@Data
public class WindLocationDataDto {

    private String id;

    private String location1;

    private String location2;

    private String location3;

    private String baseDate;

    private String baseTime;

    private String nx;

    private String ny;

    private String longitude;

    private String latitude;

    private String pty;

    private String reh;

    private String rn1;

    private String t1h;

    private String uuu;

    private String vec;

    private String vvv;

    private String wsd;

    private String wd16;

    public WindLocationDataDto(WindLocationData source) {
        copyProperties(source, this);
    }

}