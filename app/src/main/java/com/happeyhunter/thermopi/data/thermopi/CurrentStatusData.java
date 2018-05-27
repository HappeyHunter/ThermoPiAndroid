package com.happeyhunter.thermopi.data.thermopi;

/**
 * Created by david on 19/12/17.
 */

public class CurrentStatusData {

    private Boolean boostEnabled;
    private Boolean heatingEnabled;
    private Double currentTemperature;
    private Double targetTemperature;

    public Boolean getBoostEnabled() {
        return boostEnabled;
    }

    public void setBoostEnabled(Boolean boostEnabled) {
        this.boostEnabled = boostEnabled;
    }

    public Double getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(Double currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public Double getTargetTemperature() {
        return targetTemperature;
    }

    public void setTargetTemperature(Double targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    public Boolean getHeatingEnabled() {
        return heatingEnabled;
    }

    public void setHeatingEnabled(Boolean heatingEnabled) {
        this.heatingEnabled = heatingEnabled;
    }
}
