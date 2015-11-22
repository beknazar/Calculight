package kr.ac.unist.calculight;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class CalculightMonitorStarter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // assumes WordService is a registered service
        Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
        Log.i("CALCULIGHT", "Starting service");


        Intent intent1 = new Intent(context, CalculightMonitor.class);
        context.startService(intent1);
    }
}
