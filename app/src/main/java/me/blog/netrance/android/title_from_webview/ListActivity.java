package me.blog.netrance.android.title_from_webview;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;

/**
 * 리스트뷰를 사용하는 방법에 대해 알 수 있습니다.
 *
 * @author csh
 *
 */
public class ListActivity extends ActionBarActivity {
    ListView listView1;
    IconTextListAdapter adapter;
    SimpleData sd;
    Button btnAdd;
    /**
     * 부가 데이터의 키 값 정의
     */
    public static final String KEY_SIMPLE_DATA = "data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //세로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_list);
        // 리스트뷰 객체 참조
        listView1 = (ListView) findViewById(R.id.listView1);
        // 어댑터 객체 생성
        adapter = new IconTextListAdapter(this);
        AlarmSetting();

        // 아이템 데이터 만들기
        if(!getData())
        {
            Intent intent = new Intent(getBaseContext(), SearchActivity.class);
            startActivityForResult(intent, 0);
        }
        // 리스트뷰에 어댑터 설정
        listView1.setAdapter(adapter);

        // 새로 정의한 리스너로 객체를 만들어 설정
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomWebViewClient.IconTextItem curItem = (CustomWebViewClient.IconTextItem) adapter.getItem(position);
                String[] curData = curItem.getData();
                //Toast.makeText(getApplicationContext(), "Selected : " + curData[3], Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(), ModifyActivity.class);
                intent.putExtra("data", curData[3]);
                startActivityForResult(intent, 0);
            }
        });

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SearchActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void AlarmSetting(){
        Intent intentx = new Intent(getBaseContext(),  AlarmReceiver.class);
        PendingIntent pendingintent = PendingIntent.getBroadcast(getBaseContext(), 0, intentx, 0);

        long period = 1000 * 60*60*1;//하루에 두번
        long t = SystemClock.elapsedRealtime();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        // 알람 매니저에 알람을 등록
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(), period, pendingintent);
    }

    public boolean getData() {
        boolean isData = false;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String json = prefs.getString("favorite1", null);
        String pdata="";
        Resources res = getResources();
        if (json != null)
        {
            try
            {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++)
                {
                    String dispName ="";
                    pdata = array.optString(i);
                    if(pdata.split(",")[4].equals("product"))
                        dispName = pdata.split(",")[3];
                    else
                        dispName = pdata.split(",")[0];

                    if(dispName.length()>10)
                        dispName = dispName.substring(0,10)+"...";
                    ImageView bmImage =  new ImageView(getBaseContext());
                    ImageLoaderTask imageLoaderTask = new ImageLoaderTask(
                            bmImage,
                            pdata.split(",")[2]
                    );
                    imageLoaderTask.execute();

                    adapter.addItem(new CustomWebViewClient.IconTextItem(pdata.split(",")[2], dispName, pdata.split(",")[1]+"원", "현제가 : "+pdata.split(",")[5] + "원", pdata));
                }
                isData= true;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return isData;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            btnAdd.callOnClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
