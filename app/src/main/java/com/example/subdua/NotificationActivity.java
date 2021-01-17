package com.example.subdua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.subdua.broadcast.NotificationReceiver;

import java.util.Calendar;

public class NotificationActivity extends AppCompatActivity {
    private static final int DAILY_NOTIFICATION_ID = 100;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferenceEditor;
    SwitchCompat switchCompat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        switchCompat = findViewById(R.id.notifaktivitas);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
        }

        sharedPreferences = getSharedPreferences("reminder", Context.MODE_PRIVATE);

        switchCompat.setOnCheckedChangeListener((compoundButton, taiwan) -> {
            sharedPreferenceEditor = sharedPreferences.edit();
            sharedPreferenceEditor.putBoolean("daily_notification", taiwan);
            sharedPreferenceEditor.apply();

            if (taiwan) {
                allowNotification();
                Toast.makeText(this, "Allow Notification", Toast.LENGTH_SHORT).show();
            } else {
                disallowNotification();
                Toast.makeText(this, "Disallow Notification", Toast.LENGTH_SHORT).show();
            }
        });

        boolean check = sharedPreferences.getBoolean("daily_notification", false);
        switchCompat.setChecked(check);
    }

    public void allowNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, DAILY_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null)
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
    public void disallowNotification() {
        Intent intent = new Intent(this, NotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, DAILY_NOTIFICATION_ID, intent, 0);
        pendingIntent.cancel();

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null)
            alarmManager.cancel(pendingIntent);
    }
}