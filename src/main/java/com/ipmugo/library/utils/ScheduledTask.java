package com.ipmugo.library.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    @Scheduled(cron = "* * * 6 * *")
    public void getHarvest() {

    }

}
