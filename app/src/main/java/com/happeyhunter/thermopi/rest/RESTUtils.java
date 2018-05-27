package com.happeyhunter.thermopi.rest;

import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.happeyhunter.thermopi.R;
import com.happeyhunter.thermopi.data.thermopi.BoostData;
import com.happeyhunter.thermopi.data.thermopi.CurrentStatusData;
import com.happeyhunter.thermopi.data.thermopi.DayScheduleData;
import com.happeyhunter.thermopi.data.thermopi.HolidayData;
import com.happeyhunter.thermopi.data.thermopi.TargetTemperatureData;
import com.happeyhunter.thermopi.data.thermopi.WeekScheduleData;
import com.happeyhunter.thermopi.exceptions.ServerNotValidException;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by david on 18/12/17.
 */

public class RESTUtils {

    private static OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

    private static ThermoPiService THERMOPI_SERVICE;

    public static void resetService() {
        THERMOPI_SERVICE = null;
    }

    private static ThermoPiService getService(Context aContext) throws ServerNotValidException {
        if (THERMOPI_SERVICE == null) {
            THERMOPI_SERVICE = createService(aContext);
        }

        return THERMOPI_SERVICE;
    }

    private static void invalidServerToast(Context aContext) {
        Toast toast = Toast.makeText(aContext.getApplicationContext(), aContext.getString(R.string.serverNotValid), Toast.LENGTH_SHORT);
        toast.show();
    }

    private static ThermoPiService createService(Context aContext) throws ServerNotValidException {
        String baseURL = PreferenceManager.getDefaultSharedPreferences(aContext).getString(aContext.getString(R.string.thermopi_server), "");
        String authToken = PreferenceManager.getDefaultSharedPreferences(aContext).getString(aContext.getString(R.string.thermopi_auth_token), "");

        Retrofit.Builder retrofitBuilder;

        if (baseURL.length() == 0) {
            throw new ServerNotValidException();
        }

        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.registerModule(new JodaModule());

            retrofitBuilder = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(JacksonConverterFactory.create());
        } catch (IllegalArgumentException iae) {
            throw new ServerNotValidException();
        }

        AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);

        httpClientBuilder.interceptors().clear();
        httpClientBuilder.addInterceptor(interceptor);

        retrofitBuilder.client(httpClientBuilder.readTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).build());

        return retrofitBuilder.build().create(ThermoPiService.class);
    }

    public static void updateTargetTemperature(Context aContext, Callback<ResponseBody> aCallback, TargetTemperatureData updatedTemperature) {
        try {
            Call<ResponseBody> targetTemperatureUpdateCall = getService(aContext).updateTargetTemperature(updatedTemperature);
            targetTemperatureUpdateCall.enqueue(aCallback);
        } catch (ServerNotValidException snse) {
            invalidServerToast(aContext);
        }
    }

    public static void getTargetTemperature(Context aContext, Callback<TargetTemperatureData> aCallback) {
        try {
            Call<TargetTemperatureData> targetTemperatureCall = getService(aContext).getTargetTemperature();
            targetTemperatureCall.enqueue(aCallback);
        } catch (ServerNotValidException snse) {
            invalidServerToast(aContext);
        }
    }

    public static void getCurrentStatus(Context aContext, Callback<CurrentStatusData> aCallback) {
        try {
            Call<CurrentStatusData> currentStatusCall = getService(aContext).getCurrentStatus();
            currentStatusCall.enqueue(aCallback);
        } catch (ServerNotValidException snse) {
            invalidServerToast(aContext);
        }
    }

    public static void updateBoostSetting(Context aContext, Callback<BoostData> aCallback, BoostData aBoostUpdate) {
        try {
            Call<BoostData> boostUpdateCall = getService(aContext).updateBoostSetting(aBoostUpdate);
            boostUpdateCall.enqueue(aCallback);
        } catch (ServerNotValidException snse) {
            invalidServerToast(aContext);
        }
    }

    public static void getWeeklySchedule(Context aContext, Callback<WeekScheduleData> aCallback, int month) {
        try {
            Call<WeekScheduleData> weeklyScheduleCall = getService(aContext).getWeeklySchedule(month);
            weeklyScheduleCall.enqueue(aCallback);
        } catch (ServerNotValidException snse) {
            invalidServerToast(aContext);
        }
    }

    public static void updateWeeklySchedule(Context aContext, Callback<ResponseBody> aCallback, int month, WeekScheduleData weekScheduleData) {
        try {
            Call<ResponseBody> weeklyScheduleUpdateCall = getService(aContext).updateWeeklySchedule(month, weekScheduleData);
            weeklyScheduleUpdateCall.enqueue(aCallback);
        } catch (ServerNotValidException snse) {
            invalidServerToast(aContext);
        }
    }

    public static void updateDailySchedule(Context aContext, Callback<ResponseBody> aCallback, int month, String day, DayScheduleData dayScheduleData) {
        try {
            Call<ResponseBody> dailyScheduleUpdateCall = getService(aContext).updateDailySchedule(month, day, dayScheduleData);
            dailyScheduleUpdateCall.enqueue(aCallback);
        } catch (ServerNotValidException snse) {
            invalidServerToast(aContext);
        }
    }

    public static void getHolidays(Context aContext, Callback<List<HolidayData>> aCallback) {
        try {
            Call<List<HolidayData>> getHolidayCall = getService(aContext).getHolidays();
            getHolidayCall.enqueue(aCallback);
        } catch (ServerNotValidException snse) {
            invalidServerToast(aContext);
        }
    }

    public static void deleteHoliday(Context aContext, Callback<ResponseBody> aCallback, String holidayId) {
        try {
            Call<ResponseBody> deleteHolidayCall = getService(aContext).deleteHoliday(holidayId);
            deleteHolidayCall.enqueue(aCallback);
        } catch (ServerNotValidException snse) {
            invalidServerToast(aContext);
        }
    }

    public static void addHoliday(Context aContext, Callback<HolidayData> aCallback, HolidayData newHoliday) {
        try {
            Call<HolidayData> addHolidayCall = getService(aContext).addHoliday(newHoliday);
            addHolidayCall.enqueue(aCallback);
        } catch (ServerNotValidException snse) {
            invalidServerToast(aContext);
        }
    }
}
