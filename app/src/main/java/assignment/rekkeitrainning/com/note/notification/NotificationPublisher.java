package assignment.rekkeitrainning.com.note.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by hoang on 7/20/2018.
 */

class NotificationPublisher extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification mNotification = intent.getParcelableExtra(SetupNotification.NOTIFICATION);
        int id = intent.getIntExtra(SetupNotification.NOTIFICATION_ID, 0);
        manager.notify(id, mNotification);
    }
}
