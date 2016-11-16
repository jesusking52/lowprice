package shcompany.LowPrice2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyCloseAd;
import com.fsn.cauly.CaulyCloseAdListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * 리스트뷰를 사용하는 방법에 대해 알 수 있습니다.
 *
 * @author csh
 *         http://blog.naver.com/PostView.nhn?blogId=sulbe3000&logNo=220130879333&redirect=Dlog&widgetTypeCall=true
 */
public class ListActivity extends ActionBarActivity {
    ListView listView1;
    IconTextListAdapter adapter;
    Button btnAdd;
    private CaulyCloseAd closeAd;         // 종료 광고 뷰
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //세로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_list);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFF0000));
        // 리스트뷰 객체 참조
        listView1 = (ListView) findViewById(R.id.listView1);
        // 어댑터 객체 생성
        adapter = new IconTextListAdapter(this);

        AlarmSetting();

        // 아이템 데이터 만들기
        if (!getData()) {
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void AlarmSetting() {
        SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        JSONArray array = new JSONArray();
        String json = prefs.getString("favorite1", null);
        if (json == null) {
            Intent myIntent = new Intent(this, AlarmService.class);
            startService(myIntent);
        }
    }


    public boolean getData() {
        boolean isData = false;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String json = prefs.getString("favorite1", null);
        String pdata = "";
        Resources res = getResources();
        if (json != null) {
            try {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    String dispName = "";
                    pdata = array.optString(i);
                    if (pdata.split(",")[4].equals("product"))
                        dispName = pdata.split(",")[3];
                    else
                        dispName = pdata.split(",")[0];

                    if (dispName.length() > 10)
                        dispName = dispName.substring(0, 10) + "...";

                    adapter.addItem(new CustomWebViewClient.IconTextItem(pdata.split(",")[2], dispName, pdata.split(",")[1] + "원", "최저가 : " + pdata.split(",")[5] + "원", pdata));
                }
                isData = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return isData;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0,0,Menu.NONE, "최저가 추가");
        menu.add(0,1,Menu.NONE, "종료");
        //getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == 0) {
            btnAdd.callOnClick();
            return true;
        }else if (id == 1) {
            //setContentView(R.layout.cauly);
            //initClose();
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 종료 광고 초기화
    public void initClose(){
        CaulyAdInfo adInfo= new CaulyAdInfoBuilder("CAULY").build();       // CaulyAdInfo 생성, "CAULY"에 발급 ID 입력
        closeAd=new CaulyCloseAd();                                        // CaulyCloseAd 생성
        closeAd.setAdInfo(adInfo);                                         // CaulyAdView에 AdInfo 적용
        closeAd.setButtonText("아니요","네");                             // 버튼 텍스트 사용자 지정
        closeAd.setDescriptionText("종료 할까요?");                       // 질문 텍스트 사용자 지정
        // 종료 광고 리스너 작성
        closeAd.setCloseAdListener(new CaulyCloseAdListener() {
            // 종료 광고 수신 시
            @Override
            public void onReceiveCloseAd(CaulyCloseAd caulyCloseAd, boolean b) {}
            // 종료 광고가 보여질 시
            @Override
            public void onShowedCloseAd(CaulyCloseAd caulyCloseAd, boolean b) {}
            // 종료 광고 수신 실패 시
            @Override
            public void onFailedToReceiveCloseAd(CaulyCloseAd caulyCloseAd, int i, String s) {}
            // 종료 광고 왼쪽 버튼 클릭 시
            @Override
            public void onLeftClicked(CaulyCloseAd caulyCloseAd) {}
            // 종료 광고 오른쪽 버튼 클릭 시
            // 기본으로 오른쪽 버튼이 종료 버튼
            @Override
            public void onRightClicked(CaulyCloseAd caulyCloseAd) { finish(); }
            // 광고 클릭으로 앱을 벗어 날 시
            @Override
            public void onLeaveCloseAd(CaulyCloseAd caulyCloseAd) {}
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "List Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://shcompany.title_from_webview/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "List Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://shcompany.title_from_webview/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
