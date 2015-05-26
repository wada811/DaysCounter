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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.wada811.android.utils.ResourceUtils;
import java.io.File;
import at.wada811.dayscounter.CrashExceptionHandler;

public class CrashReportActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Crash Report
        if(CrashExceptionHandler.isCrashed(this)){
            CrashExceptionHandler.reportProblem(this);
        }else{
            startTutorialActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        File file = ResourceUtils.getFile(this, CrashExceptionHandler.FILE_NAME);
        file.delete();
        startTutorialActivity();
    }

    private void startTutorialActivity(){
        startActivity(new Intent(this, TutorialActivity.class));
        finish();
    }

}
