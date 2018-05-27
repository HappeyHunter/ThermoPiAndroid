package com.happeyhunter.thermopi.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.happeyhunter.thermopi.R;
import com.happeyhunter.thermopi.data.thermopi.HolidayData;
import com.happeyhunter.thermopi.rest.RESTUtils;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddHoliday extends ThermoPiActivity {

    private LocalDate startDate = null;
    private LocalDate endDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_holiday);
        startDate.dayOfWeek();
        initialiseListeners();
    }

    private void initialiseListeners() {

        Button confirmButton = findViewById(R.id.confirmAddHoliday);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHoliday();
            }
        });

        final EditText startDateText = findViewById(R.id.startDateInput);

        final DatePickerDialog.OnDateSetListener startDateDialogue = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                startDate = new LocalDate(year, monthOfYear, dayOfMonth);
                updateStartDate();
            }
        };

        startDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddHoliday.this, startDateDialogue, startDate.getYear(),
                        startDate.getMonthOfYear(),
                        startDate.getDayOfMonth()).show();
            }
        });

        final EditText endDateText = findViewById(R.id.endDateInput);

        final DatePickerDialog.OnDateSetListener endDateDialogue = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                endDate = new LocalDate(year, monthOfYear, dayOfMonth);
                updateEndDate();
            }
        };

        endDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddHoliday.this, endDateDialogue, endDate.getYear(),
                        endDate.getMonthOfYear(),
                        endDate.getDayOfMonth()).show();
            }
        });
    }

    private void updateStartDate() {
        EditText startDateText = findViewById(R.id.startDateInput);

        String dateString = DateTimeFormat.mediumDate().print(startDate);
        startDateText.setText(dateString);
    }

    private void updateEndDate() {
        EditText endDateText = findViewById(R.id.endDateInput);

        String dateString = DateTimeFormat.mediumDate().print(endDate);
        endDateText.setText(dateString);
    }

    private void addHoliday() {
        HolidayData newHoliday = new HolidayData();

        EditText startDateInput = findViewById(R.id.startDateInput);
        EditText endDateInput = findViewById(R.id.endDateInput);

        newHoliday.setStartDate(LocalDate.parse(startDateInput.getText().toString()));
        newHoliday.setEndDate(LocalDate.parse(endDateInput.getText().toString()));

        RESTUtils.addHoliday(this,
                new Callback<HolidayData>() {
            @Override
            public void onResponse(@NonNull Call<HolidayData> call, @NonNull Response<HolidayData> response) {
                if(response.isSuccessful()) {
                    addResultToast(Boolean.TRUE);
                    finish();
                } else if(isTokenError(response.code())) {
                    invalidToken();
                } else {
                    onFailure(call, new Exception());
                }
            }

            @Override
            public void onFailure(@NonNull Call<HolidayData> call, @NonNull Throwable t) {
                // Failure toast
                addResultToast(Boolean.FALSE);
            }
        }, newHoliday);

    }

    private void addResultToast(@NonNull Boolean success) {
        String message;
        if(success) {
            message = getResources().getString(R.string.holidayAddSuccess);
        } else {
            message = getResources().getString(R.string.holidayAddFailed);
        }

        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
