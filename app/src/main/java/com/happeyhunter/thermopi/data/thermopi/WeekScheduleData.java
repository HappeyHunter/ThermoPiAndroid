package com.happeyhunter.thermopi.data.thermopi;

import java.util.HashMap;

/**
 * Created by david on 31/12/17.
 */

public class WeekScheduleData {

    private Integer month;
    private HashMap<String, DayScheduleData> days;

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public HashMap<String, DayScheduleData> getDays() {
        return days;
    }

    public void setDays(HashMap<String, DayScheduleData> days) {
        this.days = days;
    }
}
