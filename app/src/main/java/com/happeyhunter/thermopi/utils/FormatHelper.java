package com.happeyhunter.thermopi.utils;

import android.content.Context;

import com.happeyhunter.thermopi.R;

/**
 * Created by david on 18/12/17.
 */

public class FormatHelper {

    public static String formatTemperature(Context context, Double temperature) {
        return context.getString(R.string.formattedTemperature, temperature);
    }

    public static String formatTime(Context context, int hour, int quarter) {
        return context.getString(R.string.formattedTime, hour, quarter);
    }
}
