package com.happeyhunter.thermopi.data.thermopi;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.joda.time.LocalDate;

/**
 * Created by david on 13/01/18.
 */

public class HolidayData {

    private String holidayID;
    private LocalDate startDateDate;
    private LocalDate endDateDate;
    @JsonIgnore
    private String description;

    public String getHolidayID() {
        return holidayID;
    }

    public void setHolidayID(String holidayID) {
        this.holidayID = holidayID;
    }

    public LocalDate getStartDate() {
        return startDateDate;
    }

    public void setStartDate(LocalDate startDateDate) {
        this.startDateDate = startDateDate;
    }

    public LocalDate getEndDate() {
        return endDateDate;
    }

    public void setEndDate(LocalDate endDateDate) {
        this.endDateDate = endDateDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return description;
    }
}
