package me.blog.netrance.android.title_from_webview;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
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


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {



        if(isLowerThenBefore(context)) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.icon01)
                    //Set the "ticker" text which is displayed in the status bar when the notification first arrives.
                    .setContentTitle("제목")
                    .setTicker("내용")
                    .setContentText("내용")
                    .setAutoCancel(true);
            //Big Picture Style - Displays a bitmap up to 256 dp tall similar to a screenshot notification.
            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
            style.setBigContentTitle("제목");
            style.setSummaryText("내용");
            //style.bigPicture(banner);
            builder.setStyle(style);
            builder.setContentIntent(pendingIntent);
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());
        }
    }

    public boolean isLowerThenBefore(Context context){
        boolean isNow = false;
        SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        JSONArray array = new JSONArray();
        String json = prefs.getString("favorite1", null);
        String pdata="";
        if (json != null)
        {
            try
            {
                array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++)
                {
                    pdata = array.optString(i);
                    request(pdata.split(",")[0],Integer.parseInt(pdata.split(",")[1]));
                }
                edit.putString("favorite1", array.toString());
                edit.commit();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return isNow;
    }

    private String request(String name, int price) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL("@Strings/searchUrl"+name);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {

                Document document = Jsoup.connect("@Strings/searchUrl"+name).get();//rss 데이터 저장.
                Elements elements = document.select(".price > strong");//item tag의 내용물 저장.

                String strPrice = elements.get(0).text().replace("원","").replace(",","");
                int iPrice = Integer.parseInt(strPrice);

            }
        } catch(Exception ex) {
            Log.e("SampleHTTP", "Exception in processing response.", ex);
            ex.printStackTrace();
        }
        return output.toString();
    }

}

