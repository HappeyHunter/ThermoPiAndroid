package com.happeyhunter.thermopi.activities;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.happeyhunter.thermopi.R;

import java.net.HttpURLConnection;

/**
 * Created by david on 14/01/18.
 */

public abstract class ThermoPiActivity extends AppCompatActivity {

    protected boolean isTokenError(int code) {
        return code == HttpURLConnection.HTTP_FORBIDDEN || code == HttpURLConnection.HTTP_UNAUTHORIZED;
    }

    protected void invalidToken() {
        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalidToken), Toast.LENGTH_SHORT);
        toast.show();
    }
}
