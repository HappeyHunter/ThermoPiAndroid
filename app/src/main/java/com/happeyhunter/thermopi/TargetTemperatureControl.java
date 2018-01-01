package com.happeyhunter.thermopi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.happeyhunter.thermopi.data.thermopi.TargetTemperatureData;
import com.happeyhunter.thermopi.rest.RESTUtils;
import com.happeyhunter.thermopi.utils.FormatHelper;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TargetTemperatureControl extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_temperature_control);

        NumberPicker tempPicker = findViewById(R.id.temperaturePicker);

        tempPicker.setMinValue(0);
        tempPicker.setMaxValue(50);
        tempPicker.setWrapSelectorWheel(false);

        updateTargetTemperature();
    }

    protected void updateTargetTemperature() {
        RESTUtils.getTargetTemperature(new Callback<TargetTemperatureData>() {
            @Override
            public void onResponse(Call<TargetTemperatureData> call, Response<TargetTemperatureData> response) {
                if(response.isSuccessful()) {
                    NumberPicker tempPicker = findViewById(R.id.temperaturePicker);
                    tempPicker.setValue(response.body().getTemperature().intValue());
                } else {
                    onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<TargetTemperatureData> call, Throwable t) {
                // Don't actually do anything, or maybe reset, notification??? go back??
                failedToGetData();
            }
        });
    }

    private void failedToGetData() {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.failedToRetrieveData), duration);
        toast.show();
    }

    private void failedToUpdateData() {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.failedToUpdateData), duration);
        toast.show();
    }

    public void submitTargetTemperatureUpdate(View aView) {
        TargetTemperatureData updatedTargetTemperature = new TargetTemperatureData();

        NumberPicker tempPicker = findViewById(R.id.temperaturePicker);
        tempPicker.getValue();

        updatedTargetTemperature.setTemperature(Double.valueOf(tempPicker.getValue()));

        RESTUtils.updateTargetTemperature(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    finish();
                } else {
                    onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Notification or something
                failedToUpdateData();
            }
        }, updatedTargetTemperature);
    }
}
