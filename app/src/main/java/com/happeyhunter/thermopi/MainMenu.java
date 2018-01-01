package com.happeyhunter.thermopi;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.happeyhunter.thermopi.data.thermopi.BoostData;
import com.happeyhunter.thermopi.data.thermopi.CurrentStatusData;
import com.happeyhunter.thermopi.data.thermopi.CurrentTemperatureData;
import com.happeyhunter.thermopi.data.thermopi.TargetTemperatureData;
import com.happeyhunter.thermopi.rest.RESTUtils;
import com.happeyhunter.thermopi.utils.FormatHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        addListeners();

        updateCurrentStatus();
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

        // Settings Listener
    }

    private void updateCurrentStatus() {
        RESTUtils.getCurrentStatus(new Callback<CurrentStatusData>() {
            @Override
            public void onResponse(Call<CurrentStatusData> call, Response<CurrentStatusData> response) {
                if(response.isSuccessful()) {

                    CurrentStatusData status = response.body();

                    // Heating status
                    ImageView heatingStatusView = findViewById(R.id.activeImage);

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
                    currentTempView.setText(FormatHelper.formatTemperature(getApplicationContext(), status.getCurrentTemperature()));

                    // Target Temperature
                    TextView targetTempView = findViewById(R.id.targetTempView);
                    targetTempView.setText(FormatHelper.formatTemperature(getApplicationContext(), status.getTargetTemperature()));

                    SwipeRefreshLayout swipeLayout = findViewById(R.id.swiperefresh);
                    swipeLayout.setRefreshing(false);

                } else {
                    onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<CurrentStatusData> call, Throwable t) {
                // Don't actually do anything, or maybe reset, notification???
                failedToGetData();
            }
        });
    }

    private void failedToGetData() {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.failedToRetrieveData), duration);
        toast.show();

        SwipeRefreshLayout swipeLayout = findViewById(R.id.swiperefresh);
        swipeLayout.setRefreshing(false);
    }

    private void updateBoostSetting(View aView) {
        BoostData update = new BoostData();
        ToggleButton boostButton = (ToggleButton) aView;

        update.setEnabled(boostButton.isChecked());

        RESTUtils.updateBoostSetting(new Callback<BoostData>() {

            @Override
            public void onResponse(Call<BoostData> call, Response<BoostData> response) {
                if(response.isSuccessful()) {
                    ImageView boostStatusView = findViewById(R.id.boostImage);

                    if (response.body().getEnabled()) {
                        boostStatusView.setImageResource(R.mipmap.boost_on);
                    } else {
                        boostStatusView.setImageResource(R.mipmap.boost_off);
                    }

                    // Notification for 1 hour or maybe until X or disabled
                } else {
                    onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<BoostData> call, Throwable t) {
                ToggleButton boostButton = findViewById(R.id.boostButton);
                boostButton.toggle();
                // maybe a message or something
            }
        }, update);
    }

    private void goToScheduleManager() {
        Intent intent = new Intent(this, ScheduleManager.class);
        startActivity(intent);
    }

    private void goToTemperatureControl() {
        Intent intent = new Intent(this, TargetTemperatureControl.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        updateCurrentStatus();
        super.onResume();
    }
}
