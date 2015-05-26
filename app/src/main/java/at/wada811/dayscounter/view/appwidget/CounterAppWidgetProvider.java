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
package at.wada811.dayscounter.view.appwidget;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;
import com.wada811.android.utils.AndroidUtils;
import com.wada811.android.utils.LogUtils;
import org.joda.time.LocalDateTime;
import at.wada811.dayscounter.model.receiver.DateTimeChangeReceiver;
import at.wada811.dayscounter.model.utils.WidgetSize;

public class CounterAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context){
        super.onEnabled(context);
        LogUtils.d();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        LogUtils.d();
        for(int appWidgetId : appWidgetIds){
            LogUtils.d("appWidgetId: " + appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds){
        super.onDeleted(context, appWidgetIds);
        LogUtils.d();
    }

    @Override
    public void onDisabled(Context context){
        super.onDisabled(context);
        LogUtils.d();
    }

    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);
        LogUtils.d();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions){
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        LogUtils.d();
        updateAppWidget(context, appWidgetManager, appWidgetId);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setDateChangeAlarm(Context context){
        Intent intent = new Intent(DateTimeChangeReceiver.ACTION_DATE_CHANGED);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        LocalDateTime today = new LocalDateTime();
        LocalDateTime tomorrow = today.plusDays(1);
        alarmManager.cancel(pendingIntent);
        if(AndroidUtils.isLessThanBuildVersion(Build.VERSION_CODES.KITKAT)){
            alarmManager.set(AlarmManager.RTC, tomorrow.toDateTime().getMillis(), pendingIntent);
        }else{
            alarmManager.setExact(AlarmManager.RTC, tomorrow.toDateTime().getMillis(), pendingIntent);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        WidgetSize widgetSize = WidgetSize.calculate(context, appWidgetManager, appWidgetId);
        RemoteViews views = null;
        if(widgetSize.width == 1 && widgetSize.height == 1){
            views = new Widget1x1View(context, appWidgetId).inflateRemoteViews();
        }
        // AppWidgetManager
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
