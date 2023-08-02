package com.github.hanielcota.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtils {
    public static String formatTime(long value) {
        if (value <= 0) return "0s";

        long minutes = TimeUnit.SECONDS.toMinutes(value);
        long seconds = value - TimeUnit.MINUTES.toSeconds(minutes);

        StringBuilder formattedTime = new StringBuilder();

        if (minutes > 0) {
            formattedTime.append(minutes).append("m");
        }
        if (seconds > 0) {
            if (!formattedTime.isEmpty()) {
                formattedTime.append(" ");
            }
            formattedTime.append(seconds).append("s");
        }

        return formattedTime.toString();
    }

}
