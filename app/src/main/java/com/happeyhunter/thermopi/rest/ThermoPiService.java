package com.happeyhunter.thermopi.rest;

import com.happeyhunter.thermopi.data.thermopi.BoostData;
import com.happeyhunter.thermopi.data.thermopi.CurrentStatusData;
import com.happeyhunter.thermopi.data.thermopi.CurrentTemperatureData;
import com.happeyhunter.thermopi.data.thermopi.DayScheduleData;
import com.happeyhunter.thermopi.data.thermopi.HolidayData;
import com.happeyhunter.thermopi.data.thermopi.TargetTemperatureData;
import com.happeyhunter.thermopi.data.thermopi.WeekScheduleData;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by david on 18/12/17.
 */

public interface ThermoPiService {

    @GET("ThermoPi/CurrentTemperature")
    Call<CurrentTemperatureData> getCurrentTemperature();

    @GET("ThermoPi/TargetTemperature")
    Call<TargetTemperatureData> getTargetTemperature();

    @GET("ThermoPi/CurrentStatus")
    Call<CurrentStatusData> getCurrentStatus();

    @GET("ThermoPi/Holidays")
    Call<List<HolidayData>> getHolidays();

    @GET("ThermoPi/WeeklySchedule/{month}")
    Call<WeekScheduleData> getWeeklySchedule(@Path("month") int month);

    @PUT("ThermoPi/TargetTemperature")
    Call<ResponseBody> updateTargetTemperature(@Body TargetTemperatureData aTargetTemperatureData);

    @PUT("ThermoPi/WeeklySchedule/{month}")
    Call<ResponseBody> updateWeeklySchedule(@Path("month") int month, @Body WeekScheduleData weekScheduleData);

    @PUT("ThermoPi/WeeklySchedule/{month}/{day}")
    Call<ResponseBody> updateDailySchedule(@Path("month") int month, @Path("day") String day, @Body DayScheduleData dayScheduleData);

    @POST("ThermoPi/Boost")
    Call<BoostData> updateBoostSetting(@Body BoostData aBoostData);

    @DELETE("ThermoPi/Holidays/{holidayId}")
    Call<ResponseBody> deleteHoliday(@Path("holidayId") String holidayId);

    @POST("ThermoPi/Holidays")
    Call<HolidayData> addHoliday(@Body HolidayData newHoliday);
}
