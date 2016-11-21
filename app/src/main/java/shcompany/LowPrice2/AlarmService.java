package shcompany.LowPrice2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.Calendar;

public class AlarmService extends Service {

    @Override
    public void onCreate() {
        AlarmSetting(getBaseContext());
    }

    private void AlarmSetting(Context context){
        Intent intentx = new Intent(context,  AlarmReceiver.class);
        PendingIntent pendingintent = PendingIntent.getBroadcast(context, 0, intentx, 0);

        long period = 1000 * 60* 60*8;//하루에 세번
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
