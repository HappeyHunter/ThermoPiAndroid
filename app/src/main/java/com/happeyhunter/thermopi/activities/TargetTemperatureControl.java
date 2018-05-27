package com.happeyhunter.thermopi.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.happeyhunter.thermopi.R;
import com.happeyhunter.thermopi.data.thermopi.TargetTemperatureData;
import com.happeyhunter.thermopi.rest.RESTUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TargetTemperatureControl extends ThermoPiActivity {

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
        RESTUtils.getTargetTemperature(this,
                new Callback<TargetTemperatureData>() {
                    @Override
                    public void onResponse(@NonNull Call<TargetTemperatureData> call, @NonNull Response<TargetTemperatureData> response) {
                        if (response.isSuccessful()) {
                            NumberPicker tempPicker = findViewById(R.id.temperaturePicker);
                            tempPicker.setValue(response.body().getTemperature().intValue());
                        } else if(response.code() == 403) {
                            invalidToken();
                        } else {
                            onFailure(call, new Exception());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TargetTemperatureData> call, @NonNull Throwable t) {
                        // Don't actually do anything, or maybe reset, notification??? go back??
                        failedToGetData();
                    }
                });
    }

    private void updateSuccessfulToast() {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.targetTempUpdateSuccess), duration);
        toast.show();
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

        updatedTargetTemperature.setTemperature((double) tempPicker.getValue());

        RESTUtils.updateTargetTemperature(this,
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            updateSuccessfulToast();
                            finish();
                        } else if(isTokenError(response.code())) {
                            invalidToken();
                        } else {
                            onFailure(call, new Exception());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        // Notification or something
                        failedToUpdateData();
                    }
                }, updatedTargetTemperature);
    }
}
