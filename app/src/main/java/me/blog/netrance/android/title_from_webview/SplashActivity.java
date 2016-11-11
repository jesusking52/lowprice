/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.blog.netrance.android.title_from_webview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class SplashActivity extends Activity {
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        iv = (ImageView) findViewById(R.id.iv);
        //iv.setScaleType(ImageView.ScaleType.CENTER);
        //iv.setScaleType(ImageView.ScaleType.CENTER);
        initialize();

    }

    private void initialize(){
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg)
            {
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0,3000);
    }

    class initialzationRunnable implements Runnable{
        public void run(){

        }
    }
}

