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
@Table(name = "wind_data")
@IdClass(Wind.Key.class)
@Getter
@Setter
@NoArgsConstructor
public class Wind implements Serializable {

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

    @Column(name = "[create]")
    private String create;

    @Column(name = "[create_date]")
    private LocalDateTime createDate;

    public Wind(String baseDate, String baseTime, String nx, String ny, String create) {
        this(baseDate, baseTime, nx, ny, "0", "0", "0", "0", "0", "0", "0", "0", "", create, null);
    }

    public Wind(String baseDate, String baseTime, String nx, String ny, String pty, String reh, String rn1, String t1h, String uuu, String vec, String vvv, String wsd, String wd16, String create, LocalDateTime createDate) {
        checkNotNull(baseDate, "baseDate must be provided");
        checkNotNull(baseTime, "baseTime must be provided");
        checkArgument(isNotEmpty(nx), "nx must be provided");
        checkArgument(isNotEmpty(ny), "ny must be provided");
        checkNotNull(create, "create must be provided");

        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.nx = nx;
        this.ny = ny;
        this.pty = pty;
        this.reh = reh;
        this.rn1 = rn1;
        this.t1h = t1h;
        this.uuu = uuu;
        this.vec = vec;
        this.vvv = vvv;
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
    }
}
