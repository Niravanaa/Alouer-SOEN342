package com.alouer.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.alouer.enums.DayOfWeek;

public class BackendUtils {
    public static String convertDaysToSchedule(List<DayOfWeek> days) {
        return days.stream()
                .map(DayOfWeek::name)
                .collect(Collectors.joining(","));
    }

    public static Integer convertTimeToMinutes(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    public static Set<DayOfWeek> convertScheduleToDays(String schedule) {
        Set<DayOfWeek> days = new HashSet<>();
        String[] validDays = { "M", "Tu", "W", "Th", "F", "Sa", "Su" };
        DayOfWeek[] dayEnums = {
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        };

        String[] parts = schedule.split("-");
        for (String part : parts) {
            int index = Arrays.asList(validDays).indexOf(part);
            if (index != -1) {
                days.add(dayEnums[index]);
            }
        }
        return days;
    }

    public static Set<DayOfWeek> parseSchedule(String schedule) {
        List<DayOfWeek> dayOfWeeks = new ArrayList<>();
        String[] days = schedule.split("-");
        for (String day : days) {
            switch (day) {
                case "M":
                    dayOfWeeks.add(DayOfWeek.MONDAY);
                    break;
                case "Tu":
                    dayOfWeeks.add(DayOfWeek.TUESDAY);
                    break;
                case "W":
                    dayOfWeeks.add(DayOfWeek.WEDNESDAY);
                    break;
                case "Th":
                    dayOfWeeks.add(DayOfWeek.THURSDAY);
                    break;
                case "F":
                    dayOfWeeks.add(DayOfWeek.FRIDAY);
                    break;
                case "Sa":
                    dayOfWeeks.add(DayOfWeek.SATURDAY);
                    break;
                case "Su":
                    dayOfWeeks.add(DayOfWeek.SUNDAY);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid day in schedule: " + day);
            }
        }
        return new LinkedHashSet<>(dayOfWeeks);
    }
}