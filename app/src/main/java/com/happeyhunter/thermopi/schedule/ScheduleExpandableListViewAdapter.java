package com.happeyhunter.thermopi.schedule;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.happeyhunter.thermopi.R;
import com.happeyhunter.thermopi.data.thermopi.QuarterData;

import java.util.HashMap;
import java.util.List;

/**
 * Created by david on 21/12/17.
 */

public class ScheduleExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListDays;
    private HashMap<String, List<QuarterData>> expandableListQuarters;

    public ScheduleExpandableListViewAdapter(Context aContext, List<String> days, HashMap<String, List<QuarterData>> quarters) {
        context = aContext;
        expandableListDays = days;
        expandableListQuarters = quarters;
    }

    @Override
    public int getGroupCount() {
        return expandableListDays.size();
    }

    @Override
    public int getChildrenCount(int groupIndex) {
        List<QuarterData> dayQuarters = expandableListQuarters.get(expandableListDays.get(groupIndex));
        return dayQuarters != null ? dayQuarters.size() : 0;
    }

    @Override
    public Object getGroup(int groupIndex) {
        return expandableListDays.get(groupIndex);
    }

    @Override
    public Object getChild(int groupIndex, int childIndex) {
        return expandableListQuarters.get(expandableListDays.get(groupIndex)).get(childIndex);
    }

    @Override
    public long getGroupId(int groupIndex) {
        return groupIndex;
    }

    @Override
    public long getChildId(int groupIndex, int childIndex) {
        return childIndex;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupIndex, boolean isExpanded, View convertView, ViewGroup parent) {
        String day = (String) getGroup(groupIndex);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.day_group, null);
        }

        TextView listTitleTextView = convertView.findViewById(R.id.dayGroup);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(day);

        return convertView;
    }

    @Override
    public View getChildView(int groupIndex, int childIndex, boolean isLastChild, View convertView, ViewGroup parent) {
        final QuarterData quarterData = (QuarterData) getChild(groupIndex, childIndex);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.day_quarter, null);
        }
        TextView expandedListTextView = convertView.findViewById(R.id.dayQuarter);
        expandedListTextView.setText(context.getString(R.string.formattedTime, quarterData.getHour(), quarterData.getQuarter() * 15));
        if(quarterData.getEnabled()) {
            expandedListTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorActive));
        } else {
            expandedListTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorInactive));
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupIndex, int childIndex) {
        return true;
    }
}
