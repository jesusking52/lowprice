package me.blog.netrance.android.title_from_webview;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * This example demonstrates how to read the title from a web view
 * and update it to the title of this action bar.
 * CustomWebViewClient class is derived from WebViewClient,
 * and it was implemented for this activity to read the title from the web view.
 *
 * @author Domone
 */
public class ModifyActivity extends AppCompatActivity {
    //main.xml
    EditText input01;
    //normal.xml
    EditText searchString;
    EditText price;
    ImageView bmImage;
    Button btnSave;
    Button exitBtn;
    AlertDialog.Builder builder;
    String keyData;
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
        setContentView(R.layout.normal);


        price =  (EditText) findViewById(R.id.price);
        searchString =  (EditText) findViewById(R.id.searchString);
        btnSave =  (Button) findViewById(R.id.btnSave);
        exitBtn =  (Button) findViewById(R.id.exitBtn);
        exitBtn.setText("삭제");
        Intent intent = getIntent();
        keyData = intent.getStringExtra("data");
        Toast.makeText(ModifyActivity.this
                , keyData
                , Toast.LENGTH_SHORT).show();
        getData(keyData);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.app.AlertDialog dialog = createDialogBox();
                dialog.show();
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.app.AlertDialog dialog = createDialogBox2();
                dialog.show();
            }
        });

    }

    public boolean getData(String keyData) {
        boolean isData = false;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ModifyActivity.this);
        String json = prefs.getString("favorite1", null);
        String pdata="";
        if (json != null)
        {
            try
            {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++)
                {
                    pdata = array.optString(i);

                    if(pdata.split(",")[0].equals(keyData))
                    {
                        price.setText(pdata.split(",")[1]);
                        searchString.setText(pdata.split(",")[0]);
                        break;
                    }
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
    /**
     * 대화상자 객체 생성
     */
    private android.app.AlertDialog createDialogBox(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        builder.setTitle("안내");
        builder.setMessage("수정하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        // 예 버튼 설정
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String pData = input01.getText().toString()+","+price.getText().toString()+","+bmImage.getBackground().toString();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ModifyActivity.this);
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

                Toast.makeText(ModifyActivity.this
                        , "즐겨찾기가 저장되었습니다."
                        , Toast.LENGTH_SHORT).show();
                // 액티비티를 띄워주도록 startActivityForResult() 메소드를 호출합니다.
                Intent intent = new Intent(getBaseContext(), ListActivity.class);
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

    private android.app.AlertDialog createDialogBox2(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        builder.setTitle("안내");
        builder.setMessage("삭제하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        // 예 버튼 설정
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ModifyActivity.this);
                String json = prefs.getString("favorite1", null);
                String pdata="";
                if (json != null)
                {
                    try
                    {
                        JSONArray array = new JSONArray(json);
                        for (int i = 0; i < array.length(); i++)
                        {
                            pdata = array.optString(i);

                            if(pdata.split(",")[0].equals(keyData))
                            {
                                array.remove(i);
                            }
                        }
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putString("favorite1", array.toString());
                        edit.commit();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(ModifyActivity.this
                        , "해당 최저가가 삭제되었습니다."
                        , Toast.LENGTH_SHORT).show();
                // 액티비티를 띄워주도록 startActivityForResult() 메소드를 호출합니다.
                Intent intent = new Intent(getBaseContext(), ListActivity.class);
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
}
