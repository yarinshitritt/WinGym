package com.example.newwimgym;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadCastBattery extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int batteryLevel = intent.getIntExtra("level", 0);

        if (batteryLevel <= 20) {
            Toast.makeText(context, "Battery level is " + batteryLevel + "%", Toast.LENGTH_SHORT).show();
        }
    }
}
