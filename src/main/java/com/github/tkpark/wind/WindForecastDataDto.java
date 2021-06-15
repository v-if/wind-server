package com.github.tkpark.wind;

import lombok.Data;

import static org.springframework.beans.BeanUtils.copyProperties;

@Data
public class WindForecastDataDto {

    private String id;

    private String location1;

    private String location2;

    private String location3;

    private String baseDate;

    private String baseTime;

    private String nx;

    private String ny;

    private String forecastTime;

    private String longitude;

    private String latitude;

    private String distance;

    private String lgt;

    private String pty;

    private String rn1;

    private String sky;

    private String t1h;

    private String reh;

    private String uuu;

    private String vec;

    private String vvv;

    private String wsd;

    private String wd16;

    private String createDate;

    public WindForecastDataDto(WindForecastData source) {
        copyProperties(source, this);
    }

}