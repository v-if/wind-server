package com.github.tkpark.image;

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

    @Column(name = "[image_file_name]")
    private String imageFileName;

    public RoadMaster(String road, String imageFileName) {
        checkNotNull(road, "road must be provided");

        this.road = road;
        this.imageFileName = imageFileName;
    }

    public static class Key implements Serializable {
        String road;
    }
}
