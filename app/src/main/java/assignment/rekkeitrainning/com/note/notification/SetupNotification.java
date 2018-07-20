package assignment.rekkeitrainning.com.note.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import assignment.rekkeitrainning.com.note.R;
import assignment.rekkeitrainning.com.note.model.Note;

/**
 * Created by hoang on 7/20/2018.
 */

public class SetupNotification {
    public static String NOTIFICATION_ID = "notificaiton_id";
    public static String NOTIFICATION = "notification";
    private Context mContext;

    public SetupNotification(Context mContext) {
        this.mContext = mContext;
    }

    @SuppressLint("NewApi")
    public Notification getNotification(Note mNote) {
        Notification.Builder mNotification = new Notification.Builder(mContext);
        mNotification.setContentTitle(mNote.getTitle());
        mNotification.setContentText(mNote.getContent());
        mNotification.setSmallIcon(R.drawable.clock);
        return mNotification.build();
    }

    public void scheduleNotification(Notification mNotification, Note mNote, long milisecond) {
        Intent mIntent = new Intent(mContext, NotificationPublisher.class);
        mIntent.putExtra(NOTIFICATION_ID, mNote.getId());
        mIntent.putExtra(NOTIFICATION, mNotification);
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(mContext, mNote.getId(), mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, milisecond, mPendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, milisecond, mPendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, milisecond, mPendingIntent);
        }
    }

    public void cancleNotification(Note mNote) {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(mNote.getId());
    }

    public void cancleAlaramManager(Note mNote) {
        Intent mIntent = new Intent(mContext, NotificationPublisher.class);
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(mContext, mNote.getId(), mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(mPendingIntent);
    }
}
