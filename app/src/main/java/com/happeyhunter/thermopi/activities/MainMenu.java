package com.happeyhunter.thermopi.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.happeyhunter.thermopi.R;
import com.happeyhunter.thermopi.data.thermopi.BoostData;
import com.happeyhunter.thermopi.data.thermopi.CurrentStatusData;
import com.happeyhunter.thermopi.rest.RESTUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenu extends ThermoPiActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        addListeners();
    }

    private void addListeners() {
        // Refresh Listener

        SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.swiperefresh);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        updateCurrentStatus();
                    }
                }
        );

        View mainMenuInclude = findViewById(R.id.mainMenuInclude);

        // Schedule Listener
        View scheduleButton = mainMenuInclude.findViewById(R.id.scheduleButton);

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToScheduleManager();
            }
        });

        // Temperature Listener
        View tempButton = mainMenuInclude.findViewById(R.id.tempButton);

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTemperatureControl();
            }
        });

        // Boost Listener
        View boostButton = mainMenuInclude.findViewById(R.id.boostButton);

        boostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBoostSetting(view);
            }
        });

        // Holiday Listener
        View holidayButton = mainMenuInclude.findViewById(R.id.holidayButton);

        holidayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHolidayManager();
            }
        });

        // Settings Listener
        View settingsButton = mainMenuInclude.findViewById(R.id.settingsButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettings();
            }
        });
    }


    private void updateCurrentStatus() {
        RESTUtils.getCurrentStatus(this,
                new Callback<CurrentStatusData>() {
            @Override
            public void onResponse(@NonNull Call<CurrentStatusData> call, @NonNull Response<CurrentStatusData> response) {
                if(response.isSuccessful()) {

                    ImageView heatingStatusView = findViewById(R.id.activeImage);
                    CurrentStatusData status = response.body();

                    if (status.getHeatingEnabled()) {
                        heatingStatusView.setImageResource(R.mipmap.flame_on);
                    } else {
                        heatingStatusView.setImageResource(R.mipmap.flame_off);
                    }

                    // Boost status
                    ImageView boostStatusView = findViewById(R.id.boostImage);
                    ToggleButton boostButton = findViewById(R.id.boostButton);

                    if (status.getBoostEnabled()) {
                        boostStatusView.setImageResource(R.mipmap.boost_on);
                        boostButton.setChecked(true);
                    } else {
                        boostStatusView.setImageResource(R.mipmap.boost_off);
                        boostButton.setChecked(false);
                    }

                    // Current Temperature
                    TextView currentTempView = findViewById(R.id.currentTempView);
                    currentTempView.setText(getString(R.string.formattedTemperature, status.getCurrentTemperature()));

                    // Target Temperature
                    TextView targetTempView = findViewById(R.id.targetTempView);
                    targetTempView.setText(getString(R.string.formattedTemperature, status.getTargetTemperature()));
                } else if(isTokenError(response.code())) {
                    invalidToken();
                } else {
                    onFailure(call, new Exception());
                }

                SwipeRefreshLayout swipeLayout = findViewById(R.id.swiperefresh);
                swipeLayout.setRefreshing(false);

                ProgressBar scheduleProgressBar = findViewById(R.id.mainMenuProgressBar);
                scheduleProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<CurrentStatusData> call, @NonNull Throwable t) {
                // Don't actually do anything, or maybe reset, notification???
                failedToGetData();

                ProgressBar scheduleProgressBar = findViewById(R.id.mainMenuProgressBar);
                scheduleProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void failedToGetData() {
        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.failedToRetrieveData), Toast.LENGTH_SHORT);
        toast.show();

        SwipeRefreshLayout swipeLayout = findViewById(R.id.swiperefresh);
        swipeLayout.setRefreshing(false);
    }

    public void updateBoostSetting(View aView) {
        BoostData update = new BoostData();
        ToggleButton boostButton = (ToggleButton) aView;

        update.setEnabled(boostButton.isChecked());

        RESTUtils.updateBoostSetting(this,
                new Callback<BoostData>() {

            @Override
            public void onResponse(@NonNull Call<BoostData> call, @NonNull Response<BoostData> response) {
                if(response.isSuccessful()) {
                    ImageView boostStatusView = findViewById(R.id.boostImage);

                    BoostData boostStatus = response.body();

                    if (boostStatus.getEnabled()) {
                        boostStatusView.setImageResource(R.mipmap.boost_on);
                        boostEnabledToast(boostStatus.getEndDate());
                    } else {
                        boostStatusView.setImageResource(R.mipmap.boost_off);
                        boostDisabledToast();
                    }

                    // Notification for 1 hour or maybe until X or disabled
                } else if(isTokenError(response.code())) {
                    invalidToken();
                } else {
                    onFailure(call, new Exception());
                }
            }

            @Override
            public void onFailure(@NonNull Call<BoostData> call, @NonNull Throwable t) {
                ToggleButton boostButton = findViewById(R.id.boostButton);
                boostButton.toggle();
                // maybe a message or something
                boostUpdateFailedToast();
            }
        }, update);
    }

    private void boostUpdateFailedToast() {
        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.failedToUpdateData), Toast.LENGTH_SHORT);
        toast.show();
    }

    private void boostDisabledToast() {
        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.boostDisabled), Toast.LENGTH_SHORT);
        toast.show();
    }

    private void boostEnabledToast(DateTime endDate) {
        String timeString = DateTimeFormat.shortTime().withZone(DateTimeZone.getDefault()).print(endDate);

        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.boostEnabled, timeString), Toast.LENGTH_SHORT);
        toast.show();
    }

    private void goToScheduleManager() {
        Intent intent = new Intent(this, ScheduleManager.class);
        startActivity(intent);
    }

    private void goToTemperatureControl() {
        Intent intent = new Intent(this, TargetTemperatureControl.class);
        startActivity(intent);
    }

    private void goToHolidayManager() {
        Intent intent = new Intent(this, HolidayManager.class);
        startActivity(intent);
    }

    private void goToSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        ProgressBar scheduleProgressBar = findViewById(R.id.mainMenuProgressBar);
        scheduleProgressBar.setVisibility(View.VISIBLE);

        updateCurrentStatus();
        super.onResume();
    }
}
