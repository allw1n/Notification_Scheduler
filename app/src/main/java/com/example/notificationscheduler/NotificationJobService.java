package com.example.notificationscheduler;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationJobService extends JobService {

    private NotificationManager notificationManager;

    private static final String PRIMARY_CHANNEL_ID = "PRIMARY_NOTIFICATION_CHANNEL";
    private static final String PRIMARY_CHANNEL_NAME = "Job service Notification";

    private static final int NOTIFICATION_ID = 0;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        createNotificationChannel();

        PendingIntent pendingContentIntent = PendingIntent.getActivity(
                this,
                NOTIFICATION_ID,
                new Intent(this, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this, PRIMARY_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_running)
                .setContentTitle(getString(R.string.job_service))
                .setContentText(getString(R.string.your_job_is_running))
                .setContentIntent(pendingContentIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

    private void createNotificationChannel() {

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = new NotificationChannel(
                PRIMARY_CHANNEL_ID,
                PRIMARY_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription(getString(R.string.app_name));

        notificationManager.createNotificationChannel(notificationChannel);
    }
}
