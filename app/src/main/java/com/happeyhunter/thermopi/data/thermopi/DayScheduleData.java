package com.happeyhunter.thermopi.data.thermopi;

import java.util.HashMap;

/**
 * Created by david on 31/12/17.
 */

public class DayScheduleData {

    private HashMap<String, HourScheduleData> hours;

    public HashMap<String, HourScheduleData> getHours() {
        return hours;
    }

    public void setHours(HashMap<String, HourScheduleData> hourMap) {
        this.hours = hourMap;
    }
}
