package uit.com.airview;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {
            // Reset the alarm here
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, FetchDataAlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_IMMUTABLE);

            // Set the alarm to start immediately and repeat every hour
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60 * 60 * 1000, pendingIntent);
        }
    }

}