package com.nuos.stakeyka.doorphone.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Util {
    public static LocalDate parseDate(String contractDate) {
        LocalDate date = null;
        try {
            if ( !contractDate.isEmpty() ) {
                date = LocalDate.parse(contractDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            return date;
        } catch (Exception ignored) { return null; }
    }

    public static LocalDate parseDateOrNow(String contractDate) {
        LocalDate date = Util.parseDate(contractDate);
        if ( date==null ) {
            date = LocalDate.now();
        }
        return date;
    }

}
