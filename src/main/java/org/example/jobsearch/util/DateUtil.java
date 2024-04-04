package org.example.jobsearch.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class DateUtil {
    public static String getFormattedLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("d MMMM, yyyy, HH:mm", new Locale("ru", "ru")));
    }

    public static String getFormattedLocalDate(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("d MMMM, yyyy", new Locale("ru", "ru")));
    }
}
