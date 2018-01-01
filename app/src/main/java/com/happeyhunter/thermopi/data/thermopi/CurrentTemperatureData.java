package com.happeyhunter.thermopi.data.thermopi;

/**
 * Created by david on 18/12/17.
 */

public class CurrentTemperatureData {

    private Double temperature;
    private Double humidity;
    private Long readingDate;

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Long getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(Long readingDate) {
        this.readingDate = readingDate;
    }
}
