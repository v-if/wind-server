package com.github.tkpark.wind;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(path = "forecast/{latitude}/{longitude}")
    public ApiResult<List<WindForecastDataDto>> forecast(@PathVariable String latitude, @PathVariable String longitude) {
        log.info("========= api/wind/forecast/{}/{} =========", latitude, longitude);

        return success(windService.findAllWindForecastDataDistance(latitude, longitude).stream()
                .map(WindForecastDataDto::new)
                .collect(toList())
        );
    }

}