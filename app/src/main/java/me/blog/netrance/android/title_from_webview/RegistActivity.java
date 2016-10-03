package me.blog.netrance.android.title_from_webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import static me.blog.netrance.android.title_from_webview.R.id.bmImage;
import static me.blog.netrance.android.title_from_webview.R.id.btnSave;
import static me.blog.netrance.android.title_from_webview.R.id.price;
import static me.blog.netrance.android.title_from_webview.R.id.product;
import static me.blog.netrance.android.title_from_webview.R.id.rdpGroup;
import static me.blog.netrance.android.title_from_webview.R.id.searchString;

/**
 * This example demonstrates how to read the title from a web view
 * and update it to the title of this action bar.
 * CustomWebViewClient class is derived from WebViewClient,
 * and it was implemented for this activity to read the title from the web view.
 *
 * @author Domone
 * //검색어,가격,이미지,상품명,product or search,현제최소가
 */
public class RegistActivity extends Activity {

/*
    public class AndroidBridge {
        EditText price;
        EditText product;
        ImageView bmImage;
        RadioGroup rdpGroup;
        Button btnSave;
        AlertDialog.Builder builder;
        String imgUrl="";
        private final Handler handler = new Handler();

        @JavascriptInterface
        public void Regist(final String arg, final String arg1, final String arg2) { // must be final

            handler.post(new Runnable() {
                @Override
                public void run() {

                    setContentView(R.layout.normal);
                    //조회 세팅
                    searchString = (EditText)findViewById(searchString);
                    rdpGroup = (RadioGroup)findViewById(rdpGroup);
                    price = (EditText)findViewById(price);
                    searchString.setText(strSearchString);
                    product =  (EditText)findViewById(product);
                    price.setText(arg);
                    product.setText(arg2);
                    //이미지 세팅
                    bmImage =  (ImageView) findViewById(bmImage);
                    String sarg1= arg1.substring(0, arg1.indexOf("?"));
                    Toast.makeText(RegistActivity.this
                            , sarg1
                            , Toast.LENGTH_SHORT).show();

                    ImageLoaderTask imageLoaderTask = new ImageLoaderTask(
                            bmImage,
                            sarg1
                    );
                    imgUrl = sarg1.toString();
                    imageLoaderTask.execute();

                    //번들 리스너
                    btnSave =  (Button) findViewById(btnSave);
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

    private android.app.AlertDialog createDialogBox(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("안내");
        //rdpGroup.get()
        if(rdpGroup.getCheckedRadioButtonId()==R.id.rdoSearch) {
            builder.setMessage("해당 검색명으로 [" +searchString.getText()+ "]로 최저가 등록하시겠습니까? \n" +
            " 해당 검색어를 사용해 "+price.getText()+"원보다 낮은 가격 상품 존재시 알림 드립니다.");
        }else{
            builder.setMessage("해당 상품명으로 [" +product.getText()+ "]로 최저가 등록하시겠습니까? \n" +
                    " 해당 검색어를 사용해 "+price.getText()+"원보다 낮은 가격 상품 존재시 알림 드립니다.");
        }
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        // 예 버튼 설정
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String pData="";
                String strRdoGroup="";
                if(rdpGroup.getCheckedRadioButtonId()==R.id.rdoSearch) {
                    strRdoGroup="search";
                }else
                {
                    strRdoGroup="product";
                }
                pData = strSearchString.replace("\"","'").replace(","," ") + "," + price.getText().toString().replace("\"","").replace(",","") + "," + imgUrl+"," +product.getText().toString().replace("\"","'").replace(","," ")+","+strRdoGroup + "," + price.getText().toString().replace("\"","'").replace(",","") ;
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RegistActivity.this);
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

                Toast.makeText(RegistActivity.this
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
    */

}
