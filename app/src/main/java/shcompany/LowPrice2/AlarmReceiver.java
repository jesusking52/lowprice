package shcompany.LowPrice2;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.PowerManager;
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
import java.util.Hashtable;


public class AlarmReceiver extends BroadcastReceiver {

    Context contextLocal;
    Intent intentLocal;
    private static PowerManager.WakeLock sCpuWakeLock;
    private static KeyguardManager.KeyguardLock mKeyguardLock;
    private static boolean isScreenLock;
    @Override
    public void onReceive(Context context, Intent intent) {
        contextLocal = context;

        // Thread로 웹서버에 접속
        new Thread() {
            public void run() {
                isLowerThenBefore(contextLocal);
                // 잠든 단말을 깨워라.
                //PushWakeLock.acquireCpuWakeLock(contextLocal);
                // WakeLock 해제.
                //PushWakeLock.releaseCpuLock();
            }
        }.start();

    }

    public boolean isLowerThenBefore(Context context){
        Log.i("isLowerThenBefore 시작","");
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
                Hashtable<String,Integer> ht = new Hashtable();

                array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++)
                {
                    pdata = array.optString(i);
                    String strSearch="";//= pdata.split(",")[0];
                    String isProduct = pdata.split(",")[4];

                    //검색어 세팅
                    if(isProduct.equals("product"))
                        strSearch = pdata.split(",")[3];
                    else
                        strSearch = pdata.split(",")[0];

                    int beforePrice = Integer.parseInt(pdata.split(",")[1]);
                    int nowPrice = GetNewPrice(strSearch);
                    if(nowPrice>0 && nowPrice < beforePrice){
                        ht.put(pdata,nowPrice);
                    }
                }
                //알림
                if(ht.size()>0)
                {
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, ListActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.icon01)
                            //Set the "ticker" text which is displayed in the status bar when the notification first arrives.
                            .setTicker("최저가가 갱신 되었습니다. 확인해 보세요.")
                            .setContentTitle("등록하신 상품의 최저가가 갱신되었습니다.")
                            .setContentText("현제가가 최종가가 아닐 경우 최저가를 갱신해 주십시오.")
                            .setAutoCancel(true)
                            .setVibrate(new long[] { 1000, 1000 }) //노티가 등록될 때 진동 패턴 1초씩 두번.
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE) //노티가 등록될 때 사운드와 진동이 오도록, 기본 사운드가 울리도록.
                            .setContentIntent(pendingIntent);
                    //Big Picture Style - Displays a bitmap up to 256 dp tall similar to a screenshot notification.
                    // NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
                    //style.setBigContentTitle("setBigContentTitle");
                    //style.setSummaryText("setSummaryText");
                    //style.bigPicture(banner);
                    //builder.setStyle(style);
                    builder.setContentIntent(pendingIntent);
                    builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, builder.build());
                }

                //최신가 수정
                for (int i = 0; i < array.length(); i++)
                {
                    pdata = array.optString(i);

                    if(ht.containsKey(pdata))
                    {
                        int newPrice = (Integer) ht.get(pdata);
                        String [] arrData = pdata.split(",");
                        String newData = "";
                        for(int j=0;j<arrData.length;j++){
                            if(j>0)
                                newData +=",";
                            if(j==5)
                                newData += newPrice;
                            else
                                newData += arrData[j];
                        }
                        System.out.println("newData="+newData);
                        array.remove(i);
                        array.put(newData);
                    }
                }
                edit.putString("favorite1", array.toString());
                edit.commit();
                Log.i("isLowerThenBefore 끝","");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return isNow;
    }

    private int GetNewPrice(String name) {
        int nowPrice=0;
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL("http://m.shopping.naver.com/search/all.nhn?frm=NVSCPRO&sort=price_asc&query="+name);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {
                Document document = Jsoup.connect("http://m.shopping.naver.com/search/all.nhn?frm=NVSCPRO&sort=price_asc&query="+name).get();//rss 데이터 저장.
                Elements elements = document.select(".price > strong");//item tag의 내용물 저장.
                String strPrice = elements.get(0).text().replace("원","").replace(",","");
                System.out.println("strName="+name);
                System.out.println("strPrice="+strPrice);

                nowPrice = Integer.parseInt(strPrice);
            }
        } catch(Exception ex) {
            Log.e("SampleHTTP", "Exception in processing response.", ex);
            ex.printStackTrace();
            nowPrice=-1;
        }
        return nowPrice;
    }


}

