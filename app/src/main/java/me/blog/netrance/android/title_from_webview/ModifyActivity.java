package me.blog.netrance.android.title_from_webview;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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

    EditText input01;
    EditText searchString;
    EditText price;
    EditText product;
    TextView lowestPrice;
    RadioGroup rdoGroup;
    ImageView bmImage;
    Button btnSave;
    Button exitBtn;
    Button buyBtn;
    Button btnReset;
    Button listBtn;
    AlertDialog.Builder builder;
    RadioButton rdoSearch;
    RadioButton rdoProduct;
    String keyData;
    String nowPrice;
    String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify);
        bmImage =  (ImageView) findViewById(R.id.bmImage);
        price =  (EditText) findViewById(R.id.price);
        product =  (EditText) findViewById(R.id.product);
        rdoGroup = (RadioGroup) findViewById(R.id.rdpGroup);
        searchString =  (EditText) findViewById(R.id.searchString);
        rdoProduct = (RadioButton)  findViewById(R.id.rdoProduct);
        rdoSearch = (RadioButton)  findViewById(R.id.rdoSearch);
        btnSave =  (Button) findViewById(R.id.btnSave);
        exitBtn =  (Button) findViewById(R.id.exitBtn);
        btnReset =  (Button) findViewById(R.id.btnReset);
        buyBtn =  (Button) findViewById(R.id.buyBtn);
        listBtn =  (Button) findViewById(R.id.listBtn);
        lowestPrice =  (TextView) findViewById(R.id.lowestPrice);
        exitBtn.setText("삭제");
        Intent intent = getIntent();
        keyData = intent.getStringExtra("data");
        btnSave.setText("수정");
        getData(keyData);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.app.AlertDialog dialog = createDialogBox();
                dialog.show();
            }
        });

        listBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ListActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                price.setText(nowPrice);
                Toast.makeText(ModifyActivity.this
                        , "최저가가 갱신되었습니다."
                        , Toast.LENGTH_SHORT).show();
                btnSave.callOnClick();
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.app.AlertDialog dialog = createDialogBox2();
                dialog.show();
            }
        });
        buyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Intent intent = new Intent(getBaseContext(), MainActivity.class);
                String searchUrl="http://m.shopping.naver.com/search/all.nhn?pagingIndex=1&productSet=total&viewType=lst&sort=price_asc&showFilter=true&frm=NVSHSRC&selectedFilterTab=price&sps=Y&query=";
                if(rdoSearch.isChecked()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl+searchString.getText().toString()));
                    //intent.setPackage("com.android.chrome");
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl+product.getText().toString()));
                    //intent.setPackage("com.android.chrome");
                    startActivity(intent);
                    finish();
                }
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
                    if(pdata.equals(keyData))
                    {
                        price.setText(pdata.split(",")[1]);
                        searchString.setText(pdata.split(",")[0]);
                        product.setText(pdata.split(",")[3]);
                        nowPrice = pdata.split(",")[5];
                        lowestPrice.setText("최저가 : "+nowPrice+"원");
                        if(pdata.split(",")[4].equals("product"))
                        {
                            rdoProduct.setChecked(true);
                        }else
                        {
                            rdoSearch.setChecked(true);
                        }
                        bmImage =  (ImageView) findViewById(R.id.bmImage);
                        ImageLoader imageLoader = new ImageLoader(getBaseContext());
                        imageLoader.DisplayImage(pdata.split(",")[2], bmImage);

                        imgUrl = pdata.split(",")[2].toString();


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
        //rdpGroup.get()
        if(rdoGroup.getCheckedRadioButtonId()==R.id.rdoSearch) {
            builder.setMessage("해당 검색명으로 [" +searchString.getText()+ "]로 최저가 수정하시겠습니까? \n" +
                    " 해당 검색어를 사용해 "+price.getText()+"원보다 낮은 가격 상품 존재시 알림 드립니다.");
        }else{
            builder.setMessage("해당 상품명으로 [" +product.getText()+ "]로 최저가 수정하시겠습니까? \n" +
                    " 해당 검색어를 사용해 "+price.getText()+"원보다 낮은 가격 상품 존재시 알림 드립니다.");
        }
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        // 예 버튼 설정
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String strRdoGroup="";
                if(rdoGroup.getCheckedRadioButtonId()==rdoProduct.getId()) {
                    strRdoGroup="product";
                }else{
                    strRdoGroup="search";
                }
                String pData = searchString.getText().toString().replace("\"","'").replace(","," ")+","+price.getText().toString().replace("\"","").replace(",","")+","+imgUrl+","+product.getText().toString().replace("\"","'").replace(","," ")+","+strRdoGroup + "," + nowPrice.toString().replace("\"","").replace(",","") ;

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ModifyActivity.this);
                SharedPreferences.Editor edit = prefs.edit();
                JSONArray array = new JSONArray();
                String json = prefs.getString("favorite1", null);
                if (json != null)
                {
                    try
                    {
                        array = new JSONArray(json);
                        for (int i = 0; i < array.length(); i++)
                        {
                            String removeData = array.optString(i);
                            if(removeData.equals(keyData))
                            {
                                array.remove(i);
                            }
                        }
                        array.put(pData);
                        Toast.makeText(ModifyActivity.this
                                , "생성"+pData
                                , Toast.LENGTH_SHORT).show();
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
                        , "최저가가 수정되었습니다."
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

                        if(pdata.equals(keyData))
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
