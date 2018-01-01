package com.happeyhunter.thermopi.rest;

import com.happeyhunter.thermopi.data.thermopi.BoostData;
import com.happeyhunter.thermopi.data.thermopi.CurrentStatusData;
import com.happeyhunter.thermopi.data.thermopi.CurrentTemperatureData;
import com.happeyhunter.thermopi.data.thermopi.DayScheduleData;
import com.happeyhunter.thermopi.data.thermopi.TargetTemperatureData;
import com.happeyhunter.thermopi.data.thermopi.WeekScheduleData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by david on 18/12/17.
 */

public class RESTUtils {

    private static String BASE_URL = "http://thermopi.dromree.com:8080/";

    private static Retrofit RETROFIT = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(JacksonConverterFactory.create()).build();

    private static ThermoPiService THERMOPI_SERVICE = RETROFIT.create(ThermoPiService.class);

    public static void getCurrentTemperature(Callback<CurrentTemperatureData> aCallback) {
        Call<CurrentTemperatureData> currentTemperatureCall = THERMOPI_SERVICE.getCurrentTemperature();
        currentTemperatureCall.enqueue(aCallback);
    }

    public static void updateTargetTemperature(Callback<ResponseBody> aCallback, TargetTemperatureData updatedTemperature) {
        Call<ResponseBody> targetTemperatureUpdateCall = THERMOPI_SERVICE.updateTargetTemperature(updatedTemperature);
        targetTemperatureUpdateCall.enqueue(aCallback);
    }

    public static void getTargetTemperature(Callback<TargetTemperatureData> aCallback) {
        Call<TargetTemperatureData> targetTemperatureCall = THERMOPI_SERVICE.getTargetTemperature();
        targetTemperatureCall.enqueue(aCallback);
    }

    public static void getCurrentStatus(Callback<CurrentStatusData> aCallback) {
        Call<CurrentStatusData> currentStatusCall = THERMOPI_SERVICE.getCurrentStatus();
        currentStatusCall.enqueue(aCallback);
    }

    public static void updateBoostSetting(Callback<BoostData> aCallback, BoostData aBoostUpdate) {
        Call<BoostData> boostUpdateCall = THERMOPI_SERVICE.updateBoostSetting(aBoostUpdate);
        boostUpdateCall.enqueue(aCallback);
    }

    public static void getWeeklySchedule(Callback<WeekScheduleData> aCallback, int month) {
        Call<WeekScheduleData> weeklyScheduleCall = THERMOPI_SERVICE.getWeeklySchedule(month);
        weeklyScheduleCall.enqueue(aCallback);
    }

    public static void updateWeeklySchedule(Callback<ResponseBody> aCallback, int month, WeekScheduleData weekScheduleData) {
        Call<ResponseBody> weeklyScheduleUpdateCall = THERMOPI_SERVICE.updateWeeklySchedule(month, weekScheduleData);
        weeklyScheduleUpdateCall.enqueue(aCallback);
    }

    public static void updateDailySchedule(Callback<ResponseBody> aCallback, int month, String day, DayScheduleData dayScheduleData) {
        Call<ResponseBody> dailyScheduleUpdateCall = THERMOPI_SERVICE.updateDailySchedule(month, day, dayScheduleData);
        dailyScheduleUpdateCall.enqueue(aCallback);
    }
}
