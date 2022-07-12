package com.ceiba.library.utils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * @author: Bladimir Minga
 * @version: 06/07/2022
 */
public class ManageDate {
    private ManageDate() {
        throw new IllegalStateException("Utility class");
    }

    public static LocalDateTime addDaysFromToday(int days) {
        LocalDateTime dateTime = LocalDateTime.now();
        int addedDays = 0;
        while (addedDays < days) {
            dateTime = dateTime.plusDays(1);
            if (!(dateTime.getDayOfWeek() == DayOfWeek.SATURDAY
                    || dateTime.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                addedDays++;
            }
        }
        return dateTime;
    }
}
