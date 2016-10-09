package me.blog.netrance.android.title_from_webview;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Hashtable;

public class AlarmService extends Service {

    @Override
    public void onCreate() {
        AlarmSetting(getBaseContext());
    }

    private void AlarmSetting(Context context){
        Intent intentx = new Intent(context,  AlarmReceiver.class);
        PendingIntent pendingintent = PendingIntent.getBroadcast(context, 0, intentx, 0);

        long period = 1000 * 60* 60*8;//하루에 두번
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        // 알람 매니저에 알람을 등록
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(), period, pendingintent);
        //재부팅 후 알람 실행
        //am.setRepeating(AlarmManager.ELAPSED_REALTIME,0, 1000*60, pendingintent);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
