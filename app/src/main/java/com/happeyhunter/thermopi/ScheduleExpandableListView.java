package com.happeyhunter.thermopi;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * Created by david on 01/01/18.
 */

public class ScheduleExpandableListView extends ExpandableListView {

    private boolean active = false;

    public ScheduleExpandableListView(Context context) {
        super(context);
        setActiveAbility();
    }

    public ScheduleExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setActiveAbility();
    }

    public ScheduleExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setActiveAbility();
    }

    public ScheduleExpandableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setActiveAbility();
    }

    private void setActiveAbility() {
        this.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return !active;
            }
        });

    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void collapseAll() {
        ExpandableListAdapter listAdapter = getExpandableListAdapter();

        int groupsCount = listAdapter.getGroupCount();
        for (int i = 0; i < groupsCount; i++) {
            collapseGroup(i);
        }
    }

    @Override
    public boolean expandGroup(int groupPos, boolean animate) {
        return active && super.expandGroup(groupPos);
    }

    @Override
    public boolean expandGroup(int groupPos) {
        return expandGroup(groupPos, false);
    }
}
