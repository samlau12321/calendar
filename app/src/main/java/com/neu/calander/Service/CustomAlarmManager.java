package com.neu.calander.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.neu.calander.ui.ClockActivity;

public class CustomAlarmManager extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String id = intent.getStringExtra("id");
        Intent intent1 = new Intent(context, ClockActivity.class);
        intent1.putExtra("id",id);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

    }
}
