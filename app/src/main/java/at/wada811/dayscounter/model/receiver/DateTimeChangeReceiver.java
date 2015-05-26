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
package at.wada811.dayscounter.model.receiver;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import at.wada811.dayscounter.view.appwidget.CounterAppWidgetProvider;

public class DateTimeChangeReceiver extends BroadcastReceiver {

    public static String ACTION_DATE_CHANGED = "at.wada811.datecounter.action.DATE_CHANGED";

    @Override
    public void onReceive(Context context, Intent intent){
        if(isTimeChanged(context, intent) || isDateChanged(context, intent) || isBootCompleted(context, intent) || isUpdate(context, intent)){
            CounterAppWidgetProvider.setDateChangeAlarm(context);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, CounterAppWidgetProvider.class));
            for(int appWidgetId : appWidgetIds){
                CounterAppWidgetProvider.updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }
    }

    private static boolean isDateChanged(Context context, Intent intent){
        if(context == null){
            throw new IllegalArgumentException("Context must not be null.");
        }
        if(intent == null){
            throw new IllegalArgumentException("Intent must not be null.");
        }
        String action = intent.getAction();
        return DateTimeChangeReceiver.ACTION_DATE_CHANGED.equals(action);
    }

    public static boolean isUpdate(Context context, Intent intent){
        if(context == null){
            throw new IllegalArgumentException("Context must not be null.");
        }
        if(intent == null){
            throw new IllegalArgumentException("Intent must not be null.");
        }
        String action = intent.getAction();
        String packagePath = intent.getDataString(); // package:at.wada811.datecounter
        return Intent.ACTION_PACKAGE_REPLACED.equals(action) && packagePath.contains(context.getPackageName());
    }

    public static boolean isBootCompleted(Context context, Intent intent){
        if(context == null){
            throw new IllegalArgumentException("Context must not be null.");
        }
        if(intent == null){
            throw new IllegalArgumentException("Intent must not be null.");
        }
        String action = intent.getAction();
        return Intent.ACTION_BOOT_COMPLETED.equals(action);
    }

    public static boolean isTimeChanged(Context context, Intent intent){
        if(context == null){
            throw new IllegalArgumentException("Context must not be null.");
        }
        if(intent == null){
            throw new IllegalArgumentException("Intent must not be null.");
        }
        String action = intent.getAction();
        return Intent.ACTION_TIMEZONE_CHANGED.equals(action) || Intent.ACTION_TIME_CHANGED.equals(action);
    }

}
