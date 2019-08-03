package com.company.api.todoservice.utils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static OffsetDateTime safeParseOffsetDateTime(String dateInString) {
        return dateInString != null ? OffsetDateTime.parse(dateInString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")) : null;
    }
}
