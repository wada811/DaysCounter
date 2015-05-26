/*
 * Copyright 2014 wada811<at.wada811@gmail.com>
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
package at.wada811.dayscounter.view.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import com.wada811.android.utils.ApplicationUtils;
import com.wada811.android.utils.IntentUtils;
import com.wada811.android.utils.ResourceUtils;
import java.io.File;
import at.wada811.dayscounter.CrashExceptionHandler;
import at.wada811.dayscounter.R;

public class TutorialActivity extends FragmentActivity {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        // catch UncaughtException
        Thread.setDefaultUncaughtExceptionHandler(new CrashExceptionHandler(getApplicationContext()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        // Window size dialog
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        // Title
        setTitle(getString(R.string.app_name) + " " + ApplicationUtils.getVersionName(this));
        // check Crash report
        File file = ResourceUtils.getFile(this, CrashExceptionHandler.FILE_NAME);
        if(file.exists()){
            startActivity(new Intent(this, CrashReportActivity.class));
            finish();
            return;
        }
        // Tutorial
        ImageView how2use = (ImageView)findViewById(R.id.how2use);
        how2use.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = IntentUtils.createOpenBrowserIntent(getString(R.string.YouTubeURL));
                startActivity(intent);
            }
        });
        Button reportProblem = (Button)findViewById(R.id.reportProblem);
        reportProblem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                reportProblem();
            }
        });
        Button rateThisApp = (Button)findViewById(R.id.rateThisApp);
        rateThisApp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                rateThisApp();
            }
        });
    }

    protected void reportProblem(){
        CrashExceptionHandler.makeReportFile(this, null);
        CrashExceptionHandler.reportProblem(this);
    }

    protected void rateThisApp(){
        Intent intent = IntentUtils.createOpenMarketIntent(getPackageName());
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CrashExceptionHandler.REQ_REPORT){
            File file = ResourceUtils.getFile(this, CrashExceptionHandler.FILE_NAME);
            file.delete();
        }
    }
}
