package com.happeyhunter.thermopi;

import com.happeyhunter.thermopi.data.thermopi.WeekScheduleData;
import com.happeyhunter.thermopi.rest.ThermoPiService;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private static String BASE_URL = "http://thermopi.dromree.com:8080/";

    private static Retrofit RETROFIT = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(JacksonConverterFactory.create()).build();

    private static ThermoPiService THERMOPI_SERVICE = RETROFIT.create(ThermoPiService.class);

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void weeklyScheduleTest() throws IOException {
        WeekScheduleData data = null;

        Call<WeekScheduleData> weeklyScheduleCall = THERMOPI_SERVICE.getWeeklySchedule(5);
        Response<WeekScheduleData> response = weeklyScheduleCall.execute();

        WeekScheduleData result = response.body();

        Boolean enabled = result.getDays().get("monday").getHours().get("0").getQuarters().get("0").getEnabled();

        assert(enabled);
    }
}