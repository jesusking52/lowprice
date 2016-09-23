package me.blog.netrance.android.title_from_webview;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URL;

/**
 * This example demonstrates how to read the title from a web view
 * and update it to the title of this action bar.
 * CustomWebViewClient class is derived from WebViewClient,
 * and it was implemented for this activity to read the title from the web view.
 *
 * @author Domone
 */
public class MainActivity extends AppCompatActivity {
    //main.xml
    EditText input01;
    WebView wvExample;
    //normal.xml
    EditText searchString;
    EditText price;
    ImageView bmImage;
    Button btnSave;
    AlertDialog.Builder builder;
    SimpleData sData = new SimpleData();
    private final Handler handler = new Handler();
    /**
     * 요청 코드 정의
     */
    public static final int REQUEST_CODE_LIST = 1001;
    /**
     * 부가 데이터의 키 값 정의
     */
    public static final String KEY_SIMPLE_DATA = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_of_activity_main);
        WebSetting();
    }

    private void WebSetting(){
        wvExample = (WebView)findViewById(R.id.wvExample);
        input01 = (EditText)findViewById(R.id.input01);
        WebSettings wvExampleSettings = wvExample.getSettings();
        wvExampleSettings.setJavaScriptEnabled(true);
        wvExample.addJavascriptInterface(new AndroidBridge(), "LowPrice");
        wvExample.setWebViewClient(new CustomWebViewClient(getSupportActionBar()));
        wvExample.setWebChromeClient(new WebChromeClient());
        wvExample.loadUrl("http://m.shopping.naver.com/search/all.nhn");
        builder = new AlertDialog.Builder(this);
        // 버튼 이벤트 처리
        Button requestBtn = (Button) findViewById(R.id.requestBtn);
        requestBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String searchString = input01.getText().toString();
                wvExample.loadUrl("http://m.shopping.naver.com/search/all.nhn?query=" + searchString + "&frm=NVSCPRO&sort=price_asc");
                wvExample.setWebViewClient(new WebViewClient() {
                    @SuppressLint("SetJavascriptEnabled")
                    public void onPageFinished(WebView view, String url) {
                        view.loadUrl("javascript:function fnCRegist(obj){" +
                                "if(confirm('해당 상품을 최저가 등록하시겠습니까?!')){" +
                                "var price = jQuery(obj).parent().find('.price strong').text().replace('원','');" +
                                "var img = jQuery(obj).parent().parent().find('img').attr('src');" +
                                "window.LowPrice.Regist(price,img);" +
                                "}" +
                                "}"
                        );
                        view.loadUrl("javascript:jQuery(\".txt_area\").append(\"<span class='list_btn' onclick='fnCRegist(this);' id='gg'><a href='#' class='zzim' id='ddd'>최저가 등록</a></span>\");");
                    }
                });
            }

        });
    }


    protected Bitmap doInBackground(String urls) {
        Bitmap bmp= null;
        Bitmap bitmap2=null;
        try {
            URL url = new URL(urls);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            bitmap2 = Bitmap.createScaledBitmap(bmp, 200, 90, false);

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return  bitmap2;
    }

    private android.app.AlertDialog createDialogBox(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("등록하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        // 예 버튼 설정
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String pData = input01.getText().toString()+","+price.getText().toString()+","+bmImage.getBackground().toString();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor edit = prefs.edit();
                JSONArray array = new JSONArray();
                String json = prefs.getString("favorite1", null);
                if (json != null)
                {
                    try
                    {
                        array = new JSONArray(json);
                        array.put(pData);
                        edit.putString("favorite1", array.toString());
                        edit.commit();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    array.put(pData);
                    edit.putString("favorite1", array.toString());
                    edit.commit();
                }

                Toast.makeText(MainActivity.this
                        , "즐겨찾기가 저장되었습니다."
                        , Toast.LENGTH_SHORT).show();
                // 액티비티를 띄워주도록 startActivityForResult() 메소드를 호출합니다.
                Intent intent = new Intent(getBaseContext(), ListActivity.class);
                //intent.putExtra(KEY_SIMPLE_DATA, bd1);
                //intent.putExtra("bd1",bd1);",bd1);
                startActivityForResult(intent, 0);

                finish();
            }
        });
        // 취소 버튼 설정
        builder.setNeutralButton("취소",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        // 아니오 버튼 설정
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        // 빌더 객체의 create() 메소드 호출하면 대화상자 객체 생성
        android.app.AlertDialog dialog = builder.create();
        return dialog;
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void Regist(final String arg, final String arg1) { // must be final

            handler.post(new Runnable() {
                @Override
                public void run() {

                    setContentView(R.layout.normal);
                    //조회 세팅
                    searchString = (EditText)findViewById(R.id.searchString);
                    price = (EditText)findViewById(R.id.price);
                    searchString.setText(input01.getText());
                    price.setText(arg);
                    //이미지 세팅
                    bmImage =  (ImageView) findViewById(R.id.bmImage);
                    String sarg1= arg1.substring(0, arg1.indexOf("?"));
                    Toast.makeText(MainActivity.this
                            , sarg1
                            , Toast.LENGTH_SHORT).show();
                    bmImage.setImageBitmap(doInBackground(sarg1));
                    //번들 리스너
                    btnSave =  (Button) findViewById(R.id.btnSave);
                    btnSave.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            android.app.AlertDialog dialog = createDialogBox();
                            dialog.show();
                        }
                    });
                }
            });
        }
    }
}
