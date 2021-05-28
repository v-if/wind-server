package com.github.tkpark.wind;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

@Entity
@Table(name = "road_master")
@IdClass(RoadMaster.Key.class)
@Getter
@Setter
@NoArgsConstructor
public class RoadMaster implements Serializable {

    @Id
    @Column(name = "[road]")
    private String road;

    @Column(name = "[road_nm]")
    private String roadNm;

    @Column(name = "[seq]")
    private int seq;

    @Column(name = "[image_file_name]")
    private String imageFileName;

    @Column(name = "[latitude]")
    private String latitude;

    @Column(name = "[longitude]")
    private String longitude;

    public RoadMaster(String road, String roadNm, int seq, String imageFileName, String latitude, String longitude) {
        checkNotNull(road, "road must be provided");

        this.road = road;
        this.roadNm = roadNm;
        this.seq = seq;
        this.imageFileName = imageFileName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static class Key implements Serializable {
        String road;
    }
}
