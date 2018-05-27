package com.happeyhunter.thermopi.settings;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

/**
 * Created by david on 14/01/18.
 */

public class TokenPreference extends EditTextPreference {
    public TokenPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TokenPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TokenPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TokenPreference(Context context) {
        super(context);
    }

    @Override
    public void setSummary(CharSequence summary) {
        super.setSummary(new String(new char[summary.length()]).replace("\0", "*"));
    }
}
