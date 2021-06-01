package com.github.tkpark.scheduling;

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

    @Scheduled(cron = "30 0/10 * * * *") // 10분에 한번씩 호출
    //@Scheduled(cron = "30 0/30 * * * *") // 30분에 한번씩 호출
    public void save() {
        log.info("Scheduler.save() ");

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
        windService.save(date, time);
    }
}
