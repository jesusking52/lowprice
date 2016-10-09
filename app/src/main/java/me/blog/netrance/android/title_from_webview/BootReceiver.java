package me.blog.netrance.android.title_from_webview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("JYJ_TEST", "onReceive : BOOT_COMPLETED");
       // Toast.makeText(context, "onReceive : BOOT_COMPLETED", Toast.LENGTH_LONG).show();
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            context.startService(new Intent(context, AlarmService.class));
        }
    }
}
