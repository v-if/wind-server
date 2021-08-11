package com.github.tkpark.wind;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.github.tkpark.utils.ApiUtils.ApiResult;
import static com.github.tkpark.utils.ApiUtils.success;
import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("api/wind")
public class WindRestController {

    private final WindService windService;

    public WindRestController(WindService windService) {
        this.windService = windService;
    }

    @GetMapping(path = "road")
    public ApiResult<List<RoadMasterDto>> road() {
        log.info("========= api/wind/road =========");

        return success(windService.findAllRoadMaster().stream()
                .map(RoadMasterDto::new)
                .collect(toList())
        );
    }

    // old
    @GetMapping(path = "forecast/{latitude}/{longitude}")
    public ApiResult<List<WindForecastDataDto>> forecast(@PathVariable String latitude, @PathVariable String longitude) {
        log.info("========= api/wind/forecast/{}/{} =========", latitude, longitude);

        return success(windService.findAllWindForecastDataDistance(latitude, longitude).stream()
                .map(WindForecastDataDto::new)
                .collect(toList())
        );
    }

    // new
    @GetMapping(path = "forecast/{latitude}/{longitude}/{zoomlevel}")
    public ApiResult<List<WindForecastDataDto>> forecast(@PathVariable String latitude, @PathVariable String longitude, @PathVariable String zoomlevel) {
        log.info("========= api/wind/forecast/{}/{}/{} =========", latitude, longitude, zoomlevel);

        List<WindForecastData> result = new ArrayList<>();

        // Zoom Level 추가
        // Zoom Level 14 이상인 경우 Zoom = 'C'
        // Zoom Level 12, 13 인 경우 Zoom = 'B'
        // Zoom Level 7, 8, 9, 10, 11 인 경우 Zoom = 'A'
        // 그외에는 화면에 마커 보여주지 않음
        int level = Integer.parseInt(zoomlevel);
        if(level >= 14) {
            result = windService.findAllWindForecastDataDistance(latitude, longitude);
        } else if(level >= 12) {
            result = windService.findAllWindForecastDataDistanceZoom(latitude, longitude, "B");
        } else if(level >= 7) {
            result = windService.findAllWindForecastDataDistanceZoom(latitude, longitude, "A");
        } else {
            result = windService.findAllWindForecastDataDistanceZoom("", "", "");
        }

        return success(result.stream()
                .map(WindForecastDataDto::new)
                .collect(toList())
        );
    }



}