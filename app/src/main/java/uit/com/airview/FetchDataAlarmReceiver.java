package uit.com.airview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FetchDataAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, FetchDataService.class);
        context.startService(serviceIntent);
    }
}
