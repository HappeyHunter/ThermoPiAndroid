package com.happeyhunter.thermopi.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.happeyhunter.thermopi.R;
import com.happeyhunter.thermopi.data.thermopi.HolidayData;
import com.happeyhunter.thermopi.rest.RESTUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HolidayManager extends ThermoPiActivity {

    private List<HolidayData> holidayList;
    private ArrayAdapter<HolidayData> holidayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_manager);

        initialiseList();
    }

    private void initialiseList() {
        ListView holidayListView = findViewById(R.id.holidayListView);
        holidayList = new ArrayList<>();

        registerForContextMenu(holidayListView);

        holidayAdapter = new ArrayAdapter<>(this,R.layout.holiday_list_item, holidayList);
        holidayListView.setAdapter(holidayAdapter);
    }

    @Override
    protected void onResume() {
        populateHolidayList();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.holiday_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_holiday:
                goToAddHoliday();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateHolidayList() {
        RESTUtils.getHolidays(this,
                new Callback<List<HolidayData>>() {
            @Override
            public void onResponse(@NonNull Call<List<HolidayData>> call, @NonNull Response<List<HolidayData>> response) {
                if(response.isSuccessful()) {
                    List<HolidayData> holidays = response.body();

                    setDescriptions(holidays);

                    holidayList.clear();
                    //holidayList.addAll(holidays);
                    holidayAdapter.addAll(holidays);
                } else if(isTokenError(response.code())) {
                    invalidToken();
                } else {
                    onFailure(call, new Exception());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<HolidayData>> call, @NonNull Throwable t) {
                // something
                int x = 0;
            }
        });
    }

    private void setDescriptions(List<HolidayData> holidays) {

        for(HolidayData holiday: holidays) {
            String startDate = DateFormat.getDateInstance(DateFormat.SHORT, getResources().getConfiguration().locale).format(holiday.getStartDate());
            String endDate = DateFormat.getDateInstance(DateFormat.SHORT, getResources().getConfiguration().locale).format(holiday.getEndDate());

            holiday.setDescription(getResources().getString(R.string.holidayDescription, startDate, endDate));
        }

    }

    private void goToAddHoliday() {
        Intent intent = new Intent(this, AddHoliday.class);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.holiday_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.editMenuOption:
                // Open edit view
                return true;
            case R.id.deleteMenuOption:
                // Alert dialog and then delete
                confirmDeleteDialog(info);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void confirmDeleteDialog(final AdapterView.AdapterContextMenuInfo info) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Delete?");
        alertDialog.setMessage("Are you sure you want to delete this holiday?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteHoliday(info.position);
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void deleteHoliday(int position) {
        HolidayData toDelete = holidayAdapter.getItem(position);

        RESTUtils.deleteHoliday(this,
                new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    holidayDeleted();
                } else if(isTokenError(response.code())) {
                    invalidToken();
                } else {
                    onFailure(call, new Exception());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                deleteFailed();
            }
        }, toDelete.getHolidayID());

        holidayAdapter.remove(toDelete);
    }

    private void holidayDeleted() {
        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.holidayDeleted), Toast.LENGTH_SHORT);
        toast.show();
    }

    private void deleteFailed() {
        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.failedToDeleteHoliday), Toast.LENGTH_SHORT);
        toast.show();
    }
}
