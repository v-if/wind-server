package com.github.tkpark.scheduling;

import com.github.tkpark.utils.DateTimeUtils;
import com.github.tkpark.wind.WindService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
@Slf4j
public class Scheduler {

    @Autowired
    WindService windService;

    /**
     * 동네예보 초단기예보
     */
    @Scheduled(cron = "0 0 4 * * *") // 새벽 4시에 하루에 호출되어 데이터 삭제
    public void windDeleteOldData() {
        log.info("Scheduler.windDeleteOldData() ");

        String baseDate = DateTimeUtils.getPreviousDay();
        log.info("baseDate:{}", baseDate);

        int resultCnt = windService.deleteOldData(baseDate);
        log.info("deleteOldData resultCnt:{}", resultCnt);
    }

    /**
     * 동네예보 초단기실황
     */
    //@Scheduled(cron = "10 0/10 * * * *") // 30분에 한번씩 호출
    public void windData() {
        log.info("Scheduler.windData() ");

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");

        String date = dateFormat.format(now);
        String time = "";
        String hour = hourFormat.format(now);
        String minute = minuteFormat.format(now);
        log.info("now.date:{}, now.hour:{}, now.minute:{}", date, hour, minute);

        int h = Integer.parseInt(hour);
        int m = Integer.parseInt(minute);
        if(m <= 30) {
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
        windService.windData(date, time);
    }

    /**
     * 동네예보 초단기예보
     */
    @Scheduled(cron = "10 5/10 * * * *") // 10분에 한번씩 호출
    public void windForecast() {
        log.info("Scheduler.windForecast() ");

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");

        String date = dateFormat.format(now);
        String time = "";
        String hour = hourFormat.format(now);
        String minute = minuteFormat.format(now);
        String locGroup = "";

        if(minute.equals("05")) {
            locGroup = "A";
        } else if(minute.equals("15")) {
            locGroup = "B";
        } else if(minute.equals("25")) {
            locGroup = "C";
        } else if(minute.equals("35")) {
            locGroup = "D";
        } else if(minute.equals("45")) {
            locGroup = "E";
        } else if(minute.equals("55")) {
            locGroup = "F";
        }

        log.info("now.date:{}, now.hour:{}, now.minute:{}, locGroup:{}", date, hour, minute, locGroup);

        int h = Integer.parseInt(hour);
        int m = Integer.parseInt(minute);
        if(m <= 35) {
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
        windService.windAllForecast(date, time, locGroup);
    }
}
