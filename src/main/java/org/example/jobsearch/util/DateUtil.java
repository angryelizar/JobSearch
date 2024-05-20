package org.example.jobsearch.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtil {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static String getFormattedLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(dateTimeFormatter);
    }

    public static String getFormattedLocalDate(LocalDateTime localDateTime) {
        return localDateTime.format(dateFormatter);
    }
}
