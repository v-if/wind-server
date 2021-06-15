package com.github.tkpark.wind;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Entity
@IdClass(WindForecastData.Key.class)
@Getter
@Setter
@NoArgsConstructor
public class WindForecastData implements Serializable {

    @Id
    @Column(name = "[id]")
    private String id;

    @Column(name = "[location1]")
    private String location1;

    @Column(name = "[location2]")
    private String location2;

    @Column(name = "[location3]")
    private String location3;

    @Column(name = "[base_date]")
    private String baseDate;

    @Column(name = "[base_time]")
    private String baseTime;

    @Column(name = "[nx]")
    private String nx;

    @Column(name = "[ny]")
    private String ny;

    @Id
    @Column(name = "[forecast_time]")
    private String forecastTime;

    @Column(name = "[longitude]")
    private String longitude;

    @Column(name = "[latitude]")
    private String latitude;

    @Column(name = "[distance]")
    private String distance;

    @Column(name = "[lgt]")
    private String lgt;

    @Column(name = "[pty]")
    private String pty;

    @Column(name = "[rn1]")
    private String rn1;

    @Column(name = "[sky]")
    private String sky;

    @Column(name = "[t1h]")
    private String t1h;

    @Column(name = "[reh]")
    private String reh;

    @Column(name = "[uuu]")
    private String uuu;

    @Column(name = "[vvv]")
    private String vvv;

    @Column(name = "[vec]")
    private String vec;

    @Column(name = "[wsd]")
    private String wsd;

    @Column(name = "[wd16]")
    private String wd16;

    @Column(name = "[create_date]")
    private String createDate;

    public static class Key implements Serializable {
        String id;
        String forecastTime;
    }

}
