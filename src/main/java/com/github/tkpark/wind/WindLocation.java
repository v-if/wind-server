package com.github.tkpark.wind;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

@Entity
@Table(name = "wind_location")
@IdClass(WindLocation.Key.class)
@Getter
@Setter
@NoArgsConstructor
public class WindLocation implements Serializable {

    @Id
    @Column(name = "[id]")
    private String id;

    @Column(name = "[location1]")
    private String location1;

    @Column(name = "[location2]")
    private String location2;

    @Column(name = "[location3]")
    private String location3;

    @Column(name = "[nx]")
    private String nx;

    @Column(name = "[ny]")
    private String ny;

    @Column(name = "[longitude]")
    private String longitude;

    @Column(name = "[latitude]")
    private String latitude;

    public WindLocation(String id, String nx, String ny, String longitude, String latitude) {
        this(id, null, null, null, nx, ny, longitude, latitude);
    }

    public WindLocation(String id, String location1, String location2, String location3, String nx, String ny, String longitude, String latitude) {
        checkNotNull(id, "id must be provided");

        this.id = id;
        this.location1 = location1;
        this.location2 = location2;
        this.location3 = location3;
        this.nx = nx;
        this.ny = ny;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public static class Key implements Serializable {
        String id;
    }
}
