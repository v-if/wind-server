package com.github.tkpark.wind;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import static com.google.common.base.Preconditions.checkNotNull;

@Entity
@Table(name = "road_point")
@IdClass(RoadPoint.Key.class)
@Getter
@Setter
@NoArgsConstructor
public class RoadPoint implements Serializable {

    @Id
    @Column(name = "[road]")
    private String road;

    @Id
    @Column(name = "[road_point]")
    private String roadPoint;

    @Column(name = "[road_nm]")
    private String roadNm;

    @Column(name = "[nx]")
    private String nx;

    @Column(name = "[ny]")
    private String ny;

    @Column(name = "[request_yn]")
    private String requestYn;

    public RoadPoint(String road, String roadPoint) {
        this(road, roadPoint, null, null, null, null);
    }

    public RoadPoint(String road, String roadPoint, String roadNm, String nx, String ny, String requestYn) {
        checkNotNull(road, "road must be provided");
        checkNotNull(roadPoint, "roadPoint must be provided");

        this.road = road;
        this.roadPoint = roadPoint;
        this.roadNm = roadNm;
        this.nx = nx;
        this.ny = ny;
        this.requestYn = requestYn;
    }

    public static class Key implements Serializable {
        String road;
        String roadPoint;
    }
}
