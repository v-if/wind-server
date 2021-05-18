package com.github.tkpark.wind;

import lombok.Data;
import java.util.List;

@Data
public class WindDataDto {

    List<WindDto> wind;

    List<RoadPointDto> roadPoint;

}
