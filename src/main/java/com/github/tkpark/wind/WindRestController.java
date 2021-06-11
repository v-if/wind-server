package com.github.tkpark.wind;

import com.github.tkpark.utils.DateTimeUtils;
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

    /*
    @GetMapping(path = "{baseDate}")
    public ApiResult<WindDto> findById(@PathVariable String baseDate) {
        log.info("========= api/wind/{} =========", baseDate);
        return success(
                windService.findById(baseDate, "", "", "")
                        .map(WindDto::new)
                        .orElseThrow(() -> new NotFoundException("Could not found wind for " + baseDate))
        );
    }
    */

    @GetMapping
    public ApiResult<List<WindLocationDataDto>> findAll() {
        log.info("========= api/wind =========");

        return success(windService.findAllWindLocationData().stream()
                .map(WindLocationDataDto::new)
                .collect(toList())
        );
    }

    @GetMapping(path = "road")
    public ApiResult<List<RoadMasterDto>> road() {
        log.info("========= api/wind/road =========");

        return success(windService.findAllRoadMaster().stream()
                .map(RoadMasterDto::new)
                .collect(toList())
        );
    }

    @GetMapping(path = "data/{latitude}/{longitude}")
    public ApiResult<List<WindLocationDataDto>> data(@PathVariable String latitude, @PathVariable String longitude) {
        log.info("========= api/wind/data/{}/{} =========", latitude, longitude);

        return success(windService.findAllWindLocationDataDistance(latitude, longitude).stream()
                .map(WindLocationDataDto::new)
                .collect(toList())
        );
    }

    @GetMapping(path = "road/{code}")
    public ApiResult<WindDataDto> roadData(@PathVariable String code) {
        log.info("========= api/wind/road/{} =========", code);

        String date = DateTimeUtils.getDate();
        String time = DateTimeUtils.getTime();

        List<RoadPoint> roadPoint = windService.findByRoadPointList(code);
        List<Wind> wind = new ArrayList<>();
        for(RoadPoint road : roadPoint) {
            String nx = road.getNx();
            String ny = road.getNy();
            log.info("road:{}, nx:{}, ny:{}", road.getRoad(), nx, ny);

            Wind windItem = windService.findByWindData(date, time, nx, ny);
            if(windItem != null) {
                wind.add(windItem);
            }
        }

        WindDataDto res = new WindDataDto();
        res.setWind(wind.stream()
                .map(WindDto::new)
                .collect(toList()));

        res.setRoadPoint(roadPoint.stream()
                .map(RoadPointDto::new)
                .collect(toList()));

        return success(res);
    }

}