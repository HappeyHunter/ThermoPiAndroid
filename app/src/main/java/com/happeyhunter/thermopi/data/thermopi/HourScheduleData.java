package com.happeyhunter.thermopi.data.thermopi;

import java.util.HashMap;

/**
 * Created by david on 31/12/17.
 */

public class HourScheduleData {

    private HashMap<String, QuarterScheduleData> quarters;

    public HashMap<String, QuarterScheduleData> getQuarters() {
        return quarters;
    }

    public void setQuarters(HashMap<String, QuarterScheduleData> quarterMap) {
        this.quarters = quarterMap;
    }
}
