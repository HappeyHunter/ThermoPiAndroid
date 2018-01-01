package com.happeyhunter.thermopi;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.happeyhunter.thermopi.data.thermopi.DayScheduleData;
import com.happeyhunter.thermopi.data.thermopi.HourScheduleData;
import com.happeyhunter.thermopi.data.thermopi.QuarterData;
import com.happeyhunter.thermopi.data.thermopi.QuarterScheduleData;
import com.happeyhunter.thermopi.data.thermopi.WeekScheduleData;
import com.happeyhunter.thermopi.rest.RESTUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleManager extends AppCompatActivity {

    private List<String> days;
    private HashMap<String, List<QuarterData>> quarters = new HashMap<String, List<QuarterData>>();
    private WeekScheduleData weekScheduleData;
    private List<Boolean> daysUpdateTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_manager);

        initialise();

        populateList(getCurrentMonth());
    }

    private void initialise() {

        Spinner monthSpinner = findViewById(R.id.monthSpinner);
        monthSpinner.setSelection(getCurrentMonth());

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                populateList(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(days == null) {
            days = new ArrayList<>();

            days.add("Monday");
            days.add("Tuesday");
            days.add("Wednesday");
            days.add("Thursday");
            days.add("Friday");
            days.add("Saturday");
            days.add("Sunday");

            daysUpdateTracker = new ArrayList<>();

            for(int i = 0; i < days.size(); i++) {
                daysUpdateTracker.add(Boolean.FALSE);
            }
        }


        ExpandableListView expListView = findViewById(R.id.weekScheduleContent).findViewById(R.id.weekExpand);

        ExpandableListAdapter adapter = new ScheduleExpandableListViewAdapter(this, days, quarters);

        expListView.setAdapter(adapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupIndex, int childIndex, long id) {
                QuarterData clickedQuarter = quarters.get(days.get(groupIndex)).get(childIndex);
                Boolean newState = !clickedQuarter.getEnabled();
                clickedQuarter.setEnabled(newState);
                expandableListView.getExpandableListAdapter().getChildView(groupIndex, childIndex, false, view, null);

                daysUpdateTracker.set(groupIndex, Boolean.TRUE);
                setQuarterEnabledSelection(groupIndex, clickedQuarter);

                return true;
            }
        });

        Button submitScheduleButton = findViewById(R.id.submitScheduleButton);
        submitScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateWeeklySchedule();
            }
        });
    }

    private int updateDaysCount() {
        int updateDays = 0;

        for(Boolean updated : daysUpdateTracker) {
            if(updated) {
                updateDays++;
            }
        }

        return updateDays;
    }

    private void updateSuccessfulToast() {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.scheduleUpdateSuccess), duration);
        toast.show();
    }

    private void updateFailedToast() {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.failedToUpdateData), duration);
        toast.show();
    }

    private void fullWeekUpdate() {
        Spinner monthSpinner = findViewById(R.id.monthSpinner);
        int month = monthSpinner.getSelectedItemPosition();

        RESTUtils.updateWeeklySchedule(new Callback<ResponseBody>() {
                                           @Override
                                           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                               if(response.isSuccessful()) {
                                                   updateSuccessfulToast();
                                               } else {
                                                   onFailure(call, null);
                                               }
                                           }

                                           @Override
                                           public void onFailure(Call<ResponseBody> call, Throwable t) {
                                               updateFailedToast();
                                           }
                                       },
                month,
                weekScheduleData
                );
    }

    private void dailyUpdate() {
        Spinner monthSpinner = findViewById(R.id.monthSpinner);
        int month = monthSpinner.getSelectedItemPosition();

        for(int i = 0; i < daysUpdateTracker.size(); i++) {

            if(daysUpdateTracker.get(i)) {
                String day = days.get(i);

                RESTUtils.updateDailySchedule(new Callback<ResponseBody>() {
                                                  @Override
                                                  public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                      if (response.isSuccessful()) {
                                                          updateSuccessfulToast();
                                                      } else {
                                                          onFailure(call, null);
                                                      }
                                                  }

                                                  @Override
                                                  public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                      updateFailedToast();
                                                  }
                                              },
                        month,
                        day,
                        weekScheduleData.getDays().get(day)
                );
            }
        }
    }

    private void updateWeeklySchedule() {
        switch(updateDaysCount()) {
            case 0:
                break;
            case 1:
                dailyUpdate();
                break;
            default:
                fullWeekUpdate();
                break;
        }
    }

    private void setQuarterEnabledSelection(int groupIndex, QuarterData quarterData) {
        String day = days.get(groupIndex);
        DayScheduleData dayData = weekScheduleData.getDays().get(day);
        dayData.getHours().get(String.valueOf(quarterData.getHour())).getQuarters().get(String.valueOf(quarterData.getQuarter())).setEnabled(quarterData.getEnabled());
    }

    private int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    private void buildExpandableData(WeekScheduleData weekScheduleData) {
        this.weekScheduleData = weekScheduleData;
        quarters.clear();

        HashMap<String, DayScheduleData> daysMap = weekScheduleData.getDays();

        for(String day : days) {
            quarters.put(day, getQuartersForDay(daysMap.get(day)));
        }

        activateExpandableView();
    }

    private void activateExpandableView() {
        ScheduleExpandableListView expListView = findViewById(R.id.weekScheduleContent).findViewById(R.id.weekExpand);
        expListView.setActive(true);
    }

    private void deactivateExpandableView() {
        ScheduleExpandableListView expListView = findViewById(R.id.weekScheduleContent).findViewById(R.id.weekExpand);
        expListView.collapseAll();
        expListView.setActive(false);
    }

    private List<QuarterData> getQuartersForDay(DayScheduleData dayData) {
        HashMap<String, QuarterScheduleData> quarterMap;
        HashMap<String, HourScheduleData> hourMap = dayData.getHours();
        List<QuarterData> aQuarterList = new ArrayList<>();

        for(int hour = 0; hour < hourMap.size(); hour++) {
            quarterMap = hourMap.get(String.valueOf(hour)).getQuarters();
            for(int quarter = 0; quarter < quarterMap.size(); quarter++) {
                QuarterScheduleData quarterData = quarterMap.get(String.valueOf(quarter));
                QuarterData aQuarter = new QuarterData();
                aQuarter.setHour(hour);
                aQuarter.setQuarter(quarter);
                aQuarter.setEnabled(quarterData.getEnabled());

                aQuarterList.add(aQuarter);
            }
        }

        return aQuarterList;
    }

    private void failedToGetData() {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.failedToRetrieveData), duration);
        toast.show();
    }

    public void populateList(int month) {
        deactivateExpandableView();
        resetUpdateTracker();

        RESTUtils.getWeeklySchedule(new Callback<WeekScheduleData>() {
            @Override
            public void onResponse(Call<WeekScheduleData> call, Response<WeekScheduleData> response) {
                if(response.isSuccessful()) {
                    buildExpandableData(response.body());
                } else {
                    onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<WeekScheduleData> call, Throwable t) {
                failedToGetData();
            }
        }, month);


    }

    private void resetUpdateTracker() {
        for(int i = 0; i < daysUpdateTracker.size(); i++) {
            daysUpdateTracker.set(i, Boolean.FALSE);
        }
    }
}
