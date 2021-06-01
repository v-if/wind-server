package com.github.tkpark.wind;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "vm_wind_location_data")
@Getter
@Setter
@NoArgsConstructor
public class WindLocationData implements Serializable {

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

    @Column(name = "[longitude]")
    private String longitude;

    @Column(name = "[latitude]")
    private String latitude;

    @Column(name = "[pty]")
    private String pty;

    @Column(name = "[reh]")
    private String reh;

    @Column(name = "[rn1]")
    private String rn1;

    @Column(name = "[t1h]")
    private String t1h;

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

}
