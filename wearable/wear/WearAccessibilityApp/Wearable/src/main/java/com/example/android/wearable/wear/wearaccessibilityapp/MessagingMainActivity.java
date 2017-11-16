package com.example.android.wearable.wear.wearaccessibilityapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

/**
 * Template class meant to include functionality for your Messaging App. (This project's main focus
 * is on Notification Styles.)
 */
public class MessagingMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_main);

        // Cancel Notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(NotificationsActivity.NOTIFICATION_ID);
    }
}