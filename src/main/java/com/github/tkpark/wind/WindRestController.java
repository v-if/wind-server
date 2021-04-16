package com.github.tkpark.wind;

import com.github.tkpark.errors.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    public ApiResult<List<WindDto>> findAll() {
        log.info("========= api/wind =========");

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");

        String date = dateFormat.format(now);
        String time = "";
        String hour = hourFormat.format(now);
        String minute = minuteFormat.format(now);
        log.info("date:{}, hour:{}, minute:{}", date, hour, minute);

        int h = Integer.parseInt(hour);
        int m = Integer.parseInt(minute);
        if(m <= 40) {
            h = h - 1;
            if(h < 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(now);
                c.add(Calendar.DATE, -1);
                Date before = c.getTime();
                date = dateFormat.format(before);
                h = 23;
            }
        }
        time = String.format("%02d", h) + "00";
        //windService.save(date, time);

        return success(windService.findAllByBaseDateAndBaseTime(date, time).stream()
                .map(WindDto::new)
                .collect(toList())
        );
    }

}