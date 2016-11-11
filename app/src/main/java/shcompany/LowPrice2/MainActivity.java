package shcompany.LowPrice2;

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

/**
 * This example demonstrates how to read the title from a web view
 * and update it to the title of this action bar.
 * CustomWebViewClient class is derived from WebViewClient,
 * and it was implemented for this activity to read the title from the web view.
 *
 * @author Domone
 * //검색어,가격,이미지,상품명,product or search,현제최소가
 */
public class MainActivity extends Activity {

    WebView wvExample;
    EditText searchString;
    String strSearchString;
    EditText price;
    EditText product;
    ImageView bmImage;
    RadioGroup rdpGroup;
    Button btnSave;
    AlertDialog.Builder builder;
    String imgUrl="";
    private final Handler handler = new Handler();
    String searchUrl="http://m.shopping.naver.com/search/all.nhn?pagingIndex=1&productSet=total&viewType=lst&sort=price_asc&showFilter=true&frm=NVSHSRC&selectedFilterTab=price&sps=Y&query=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_of_activity_main);
        builder = new AlertDialog.Builder(this);
        getParameter();
        WebSetting();
    }

    private  void getParameter(){
        Intent intent = getIntent();
        strSearchString = intent.getStringExtra("searchString");
    }

    private void WebSetting(){
        wvExample = (WebView)findViewById(R.id.wvExample);
        WebSettings wvExampleSettings = wvExample.getSettings();
        wvExampleSettings.setJavaScriptEnabled(true);
        wvExample.addJavascriptInterface(new AndroidBridge(), "LowPrice");
        wvExample.setWebChromeClient(new WebChromeClient());

        wvExample.loadUrl(searchUrl + strSearchString );
        wvExample.setWebViewClient(new WebViewClient() {
            @SuppressLint("SetJavascriptEnabled")
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:function fnCRegist(obj){" +
                        "var lowestPrice = jQuery('.price strong').eq(0).text().replace(',','').replace('원','');" +
                        "var price = jQuery(obj).parent().find('.price strong').text().replace('원','');" +
                        "var message ='';" +
                        "if(lowestPrice<price){" +
                        "   message='해당 상품은 최저가 상품이 아닙니다. 등록하시겠습니까?'" +
                        "}else{" +
                        "   message='해당 상품을 최저가 등록하시겠습니까?!'" +
                        "}" +
                        "if(confirm(message)){" +
                        "var img = jQuery(obj).parent().parent().find('img').attr('src');" +
                        "var pName = jQuery(obj).parent().find('.info_tit').text();" +
                        "var searchString = $(\"input[name='query']\").val();" +
                        "window.LowPrice.Regist(price,img, pName, searchString);" +
                            "}" +
                        "};"
                );
                view.loadUrl("javascript:jQuery(\".list_btn, .sr_opt_area, .sr_opt_depth, .tag_list \").remove();");//jQuery(".npay_point").remove();jQuery(".info_etc2").remove();
                view.loadUrl("javascript:jQuery(\".type_list .txt_area\").append(\"<span class='list_btn' onclick='fnCRegist(this);' id='gg'><a href='#' class='zzim' id='ddd' style='width:120px;line:0px;color:crimson;background-color: gold;font-family: sans-serif;' >최저가 등록</a></span>\");");
                //처리
                //view.loadUrl("javascript:jQuery(\"input[name='query']\").keyup(function(e){alert(e.keyCode);});");//if(e.keyCode == 13){e.preventDefault();e.stopPropagation();location.href='"+searchUrl+"'+$(\"input[name='query']\").val();}});");//
                view.loadUrl("javascript:$(\"input[name='query']\").keyup(function(e){if(e.keyCode == 13){e.preventDefault();e.stopPropagation();location.href='"+searchUrl+"'+$(\"input[name='query']\").val();}});");//
                Toast.makeText(MainActivity.this
                        , "원하는 상품이 가장 상위에 오도록 검색해 주셔야 정확한 알림을 드릴 수 있습니다."
                        , Toast.LENGTH_LONG).show();
            }
        });
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void Regist(final String arg, final String arg1, final String arg2, final String search) { // must be final

            handler.post(new Runnable() {
                @Override
                public void run() {
                    strSearchString = search;
                    setContentView(R.layout.normal);
                    //조회 세팅
                    searchString = (EditText)findViewById(R.id.searchString);
                    rdpGroup = (RadioGroup)findViewById(R.id.rdpGroup);
                    price = (EditText)findViewById(R.id.price);
                    searchString.setText(search);
                    product =  (EditText)findViewById(R.id.product);
                    price.setText(arg);
                    product.setText(arg2);
                    //이미지 세팅
                    bmImage =  (ImageView) findViewById(R.id.bmImage);
                    String sarg1= arg1.substring(0, arg1.indexOf("?"));
                    Toast.makeText(MainActivity.this
                            , sarg1
                            , Toast.LENGTH_SHORT).show();
/*
                    ImageLoaderTask imageLoaderTask = new ImageLoaderTask(
                            bmImage,
                            sarg1
                    );
                    imageLoaderTask.execute();
                    */
                    ImageLoader imageLoader = new ImageLoader(getBaseContext());
                    imageLoader.DisplayImage(sarg1, bmImage);

                    imgUrl = sarg1.toString();

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
                        , "최저가가 등록되었습니다."
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
