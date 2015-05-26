/*
 * Copyright 2013 wada811<at.wada811@gmail.com>
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
package at.wada811.dayscounter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import com.wada811.android.utils.ApplicationUtils;
import com.wada811.android.utils.FileUtils;
import com.wada811.android.utils.IntentUtils;
import com.wada811.android.utils.LogUtils;
import com.wada811.android.utils.PreferenceUtils;
import com.wada811.android.utils.ResourceUtils;
import com.wada811.android.utils.ToastUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Map;
import java.util.Map.Entry;
import at.wada811.dayscounter.view.activity.CrashReportActivity;

/**
 * キャッチされなかったExceptionが発生した場合にログを記録する
 *
 * @author wada
 */
public final class CrashExceptionHandler implements UncaughtExceptionHandler {

    public static final int REQ_REPORT = 811;
    public static final String FILE_NAME = "report.txt";
    public static final String MAILTO = "at.wada811+DaysCounterCrashReport@gmail.com";

    private final Context mContext;
    private final UncaughtExceptionHandler mHandler;

    public CrashExceptionHandler(Context context){
        mContext = context;
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    /**
     * キャッチされなかった例外発生時に各種情報をJSONでテキストファイルに書き出す
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void uncaughtException(Thread thread, Throwable throwable){
        // make Crash report
        CrashExceptionHandler.makeReportFile(mContext, throwable);
        // launch CrashReportActivity
        Intent intent = new Intent(mContext, CrashReportActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
        //
        mHandler.uncaughtException(thread, throwable);
    }

    public static void makeReportFile(Context context, Throwable throwable){
        PrintWriter writer = null;
        FileOutputStream outputStream;
        try{
            outputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            writer = new PrintWriter(outputStream);
            String report = CrashExceptionHandler.makeReport(context, throwable);
            writer.print(report);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }finally{
            if(writer != null){
                writer.close();
            }
        }
    }

    private static String makeReport(Context context, Throwable throwable){
        JSONObject json = new JSONObject();
        try{
            json.put("Build", CrashExceptionHandler.getBuildInfo());
            json.put("PackageInfo", CrashExceptionHandler.getPackageInfo(context));
            if(throwable != null){
                json.put("Exception", CrashExceptionHandler.getExceptionInfo(throwable));
            }
            json.put("SharedPreferences", CrashExceptionHandler.getPreferencesInfo(context));
        }catch(JSONException e){
            e.printStackTrace();
        }
        LogUtils.e(json.toString());
        return json.toString();
    }

    /**
     * ビルド情報をJSONで返す
     *
     * @return
     *
     * @throws JSONException
     */
    public static JSONObject getBuildInfo(){
        JSONObject json = new JSONObject();
        try{
            json.put("BRAND", Build.BRAND); // キャリア、メーカー名など(docomo)
            json.put("MODEL", Build.MODEL); // ユーザーに表示するモデル名(SO-01C)
            json.put("DEVICE", Build.DEVICE); // デバイス名(SO-01C)
            json.put("MANUFACTURER", Build.MANUFACTURER); // 製造者名(Sony Ericsson)
            json.put("VERSION.SDK_INT", Build.VERSION.SDK_INT); // フレームワークのバージョン情報(10)
            json.put("VERSION.RELEASE", Build.VERSION.RELEASE); // ユーザーに表示するバージョン番号(2.3.4)
        }catch(JSONException e){
            e.printStackTrace();
        }
        return json;
    }

    /**
     * パッケージ情報を返す
     *
     * @return
     *
     * @throws JSONException
     */
    public static JSONObject getPackageInfo(Context context){
        JSONObject json = new JSONObject();
        try{
            json.put("packageName", context.getPackageName());
            json.put("versionCode", ApplicationUtils.getVersionCode(context));
            json.put("versionName", ApplicationUtils.getVersionName(context));
        }catch(JSONException e){
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 例外情報を返す
     *
     * @param throwable
     *
     * @return
     *
     * @throws JSONException
     */
    public static JSONObject getExceptionInfo(Throwable throwable) throws JSONException{
        JSONObject json = new JSONObject();
        json.put("name", throwable.getClass().getName());
        json.put("message", throwable.getMessage());
        if(throwable.getStackTrace() != null){
            // ExceptionStacktrace
            JSONArray exceptionStacktrace = new JSONArray();
            for(StackTraceElement element : throwable.getStackTrace()){
                exceptionStacktrace.put("at " + LogUtils.getMetaInfo(element));
            }
            json.put("ExceptionStacktrace", exceptionStacktrace);
        }
        if(throwable.getCause() != null){
            json.put("cause", throwable.getCause());
            // CausedStacktrace
            if(throwable.getCause().getStackTrace() != null){
                JSONArray causedStacktrace = new JSONArray();
                for(StackTraceElement element : throwable.getCause().getStackTrace()){
                    causedStacktrace.put("at " + LogUtils.getMetaInfo(element));
                }
                json.put("CausedStacktrace", causedStacktrace);
            }
        }
        return json;
    }

    /**
     * Preferencesを返す
     *
     * @return
     *
     * @throws JSONException
     */
    public static JSONObject getPreferencesInfo(Context context){
        SharedPreferences preferences = PreferenceUtils.getDefaultSharedPreferences(context);
        JSONObject json = new JSONObject();
        Map<String, ?> map = preferences.getAll();
        for(Entry<String, ?> entry : map.entrySet()){
            try{
                json.put(entry.getKey(), entry.getValue());
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return json;
    }

    public static boolean isCrashed(Context context){
        File file = ResourceUtils.getFile(context, CrashExceptionHandler.FILE_NAME);
        return file.exists();
    }

    public static void reportProblem(Context context){
        File file = ResourceUtils.getFile(context, CrashExceptionHandler.FILE_NAME);
        File dirFile = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_dir));
        File attachmentFile = new File(dirFile, CrashExceptionHandler.FILE_NAME);
        Intent intent;
        String report = ResourceUtils.readFileString(context, CrashExceptionHandler.FILE_NAME);
        if(FileUtils.move(file, attachmentFile)){
            intent = IntentUtils.createSendMailIntent(CrashExceptionHandler.MAILTO,
                context.getString(R.string.reportProblemMailTitle),
                context.getString(R.string.reportProblemMailBody));
            intent = IntentUtils.addFile(intent, attachmentFile);
        }else{
            intent = IntentUtils.createSendMailIntent(CrashExceptionHandler.MAILTO,
                context.getString(R.string.reportProblemMailTitle),
                context.getString(R.string.reportProblemMailBody, report));
        }
        Intent gmailIntent = IntentUtils.createGmailIntent(intent);
        if(IntentUtils.canIntent(context, gmailIntent)){
            ((Activity)context).startActivityForResult(gmailIntent, REQ_REPORT);
        }else if(IntentUtils.canIntent(context, intent)){
            ((Activity)context).startActivityForResult(Intent.createChooser(intent,
                context.getString(R.string.reportProblem)), REQ_REPORT);
        }else{
            ToastUtils.show(context, R.string.mailerNotFound);
        }
    }
}
