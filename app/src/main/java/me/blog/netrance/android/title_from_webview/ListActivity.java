package me.blog.netrance.android.title_from_webview;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;


/**
 * 리스트뷰를 사용하는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
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
        setContentView(R.layout.activity_list);
        // 리스트뷰 객체 참조
        listView1 = (ListView) findViewById(R.id.listView1);
        // 어댑터 객체 생성
        adapter = new IconTextListAdapter(this);
        AlarmSetting();
        // 아이템 데이터 만들기
        if(!getData())
        {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivityForResult(intent, 0);
        }
        // 리스트뷰에 어댑터 설정
        listView1.setAdapter(adapter);

        // 새로 정의한 리스너로 객체를 만들어 설정
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IconTextItem curItem = (IconTextItem) adapter.getItem(position);
                String[] curData = curItem.getData();
                Toast.makeText(getApplicationContext(), "Selected : " + curData[0], Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(), ModifyActivity.class);
                intent.putExtra("data", curData[0]);
                startActivityForResult(intent, 0);
            }

        });

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void AlarmSetting(){
        Intent intentx = new Intent(getBaseContext(),  AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(getBaseContext(), 0, intentx, 0);

        // 알람을 받을 시간을 5초 뒤로 설정
        Calendar calendar = Calendar.getInstance();
        /*
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 5);
        //알람 매니저에 알람을 등록
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
       */

        /*
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 21) {
            calendar.add(Calendar.DATE, 1);
        }
        */
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 24);
        calendar.set(Calendar.SECOND, 00);
        // 알람 매니저에 알람을 등록
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC, calendar.getTimeInMillis(), sender);

    }

    public boolean getData() {
        boolean isData = false;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ListActivity.this);
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
                    pdata = array.optString(i);
                    adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.icon05), pdata.split(",")[0], pdata.split(",")[2], pdata.split(",")[1]+"원"));
                }
                isData= true;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
