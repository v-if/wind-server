package com.github.tkpark.wind;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity
@Table(name = "wind_forecast")
@IdClass(WindForecast.Key.class)
@Getter
@Setter
@NoArgsConstructor
public class WindForecast implements Serializable {

    @Id
    @Column(name = "[base_date]")
    private String baseDate;

    @Id
    @Column(name = "[base_time]")
    private String baseTime;

    @Id
    @Column(name = "[nx]")
    private String nx;

    @Id
    @Column(name = "[ny]")
    private String ny;

    @Id
    @Column(name = "[forecast_time]")
    private String forecastTime;

    @Column(name = "[LGT]")
    private String lgt;

    @Column(name = "[pty]")
    private String pty;

    @Column(name = "[rn1]")
    private String rn1;

    @Column(name = "[SKY]")
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

    @Column(name = "[create]")
    private String create;

    @Column(name = "[create_date]")
    private LocalDateTime createDate;

    public WindForecast(String baseDate, String baseTime, String nx, String ny, String forecastTime, String create) {
        this(baseDate, baseTime, nx, ny, forecastTime, "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "", create, null);
    }

    public WindForecast(String baseDate, String baseTime, String nx, String ny, String forecastTime, String lgt, String pty, String rn1, String sky, String t1h, String reh, String uuu, String vec, String vvv, String wsd, String wd16, String create, LocalDateTime createDate) {
        checkNotNull(baseDate, "baseDate must be provided");
        checkNotNull(baseTime, "baseTime must be provided");
        checkArgument(isNotEmpty(nx), "nx must be provided");
        checkArgument(isNotEmpty(ny), "ny must be provided");
        checkArgument(isNotEmpty(forecastTime), "forecastTime must be provided");
        checkNotNull(create, "create must be provided");

        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.nx = nx;
        this.ny = ny;
        this.forecastTime = forecastTime;
        this.lgt = lgt;
        this.pty = pty;
        this.rn1 = rn1;
        this.sky = sky;
        this.t1h = t1h;
        this.reh = reh;
        this.uuu = uuu;
        this.vvv = vvv;
        this.vec = vec;
        this.wsd = wsd;
        this.wd16 = wd16;
        this.create = create;
        this.createDate = defaultIfNull(createDate, now());
    }

    public static class Key implements Serializable {
        String baseDate;
        String baseTime;
        String nx;
        String ny;
        String forecastTime;
    }
}
