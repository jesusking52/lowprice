package shcompany.LowPrice2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyAdView;
import com.fsn.cauly.CaulyCloseAd;
import com.fsn.cauly.CaulyCloseAdListener;
import com.fsn.cauly.CaulyInterstitialAd;
import com.fsn.cauly.CaulyInterstitialAdListener;

public class CaulyActivity extends Activity  {

    private CaulyAdView adView;           // 배너 광고 뷰
    private CaulyCloseAd closeAd;         // 종료 광고 뷰
    private CaulyInterstitialAd fullAd;   // 전면 광고 뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(new Intent(CaulyActivity.this, SplashActivity.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cauly);
        //showBanner();        // 배너 광고 초기화
        showFull();          // 전면 광고 초기화
        //initClose();         // 종료 광고 초기화
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(adView!=null) adView.destroy();  // 광고 소멸
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(closeAd!=null) closeAd.resume(this);  // 종료 광고 구현 시 반드시!! 호출
    }

    // 배너 광고 삽입
    private void showBanner(){
        LinearLayout layout=(LinearLayout)findViewById(R.id.main_layout);  // Root 레이아웃 참조
        CaulyAdInfo adInfo= new CaulyAdInfoBuilder("CAULY").effect("TopSlide").reloadInterval(1).build();       // CaulyAdInfo 생성, "CAULY"에 발급 ID 입력
        adView=new CaulyAdView(this);                                     // CaulyAdView 생성
        adView.setAdInfo(adInfo);                                          // CaulyAdView에 AdInfo 적용
        layout.addView(adView,0);                                          // Root 레이아웃 첫 뷰에 광고 삽입
    }

    // 전면 광고 삽입
    private void showFull(){
        CaulyAdInfo adInfo= new CaulyAdInfoBuilder("t1d4aWgM").build();              // CaulyAdInfo 생성, "CAULY"에 발급 ID 입력
        fullAd=new CaulyInterstitialAd();                                         // CaulyInterstitialAd 생성
        fullAd.setAdInfo(adInfo);                                                 // CaulyInterstitialAd에 AdInfo 적용
        // 광고 Listener 작성
        fullAd.setInterstialAdListener(new CaulyInterstitialAdListener() {
            // 전면 광고 수신 시
            // 여기에서 전면 광고를 띄움
            @Override
            public void onReceiveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd, boolean b) {
                fullAd.show(); //전면 광고 띄움
            }
            // 전면 광고 수신 실패 시
            @Override
            public void onFailedToReceiveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd, int i, String s) {
            }
            // 전면 광고 닫힐 시
            @Override
            public void onClosedInterstitialAd(CaulyInterstitialAd caulyInterstitialAd) {
                Intent intent = new Intent(getBaseContext(), ListActivity.class);
                startActivityForResult(intent, 0);
            }
            // 전면 광고 클릭으로 앱을 벗어날 시
            @Override
            public void onLeaveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd) {

            }
        });
        fullAd.requestInterstitialAd(this); //전면 광고 요청
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

    // Back Key가 눌러졌을 때, CloseAd 호출
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {  // Back 키이면
            // 앱을 처음 설치하여 실행할 때, 필요한 리소스를 다운받았는지 여부.
            if (closeAd.isModuleLoaded()) {
                // 종료 광고 띄움
                closeAd.show(this);
            } else {
                // 광고에 필요한 리소스를 한번만  다운받는데 실패했을 때 앱의 종료팝업 구현
                showDefaultClosePopup();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 기본 종료 팝업
    private void showDefaultClosePopup(){
        new AlertDialog.Builder(this).setTitle("").setMessage("종료 할까요?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("아니요",null)
                .show();
    }
}