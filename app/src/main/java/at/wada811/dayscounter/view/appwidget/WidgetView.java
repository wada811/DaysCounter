package at.wada811.dayscounter.view.appwidget;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

public abstract class WidgetView {

    protected final Context context;
    protected final int appWidgetId;

    public WidgetView(Context context, int appWidgetId){
        this.context = context;
        this.appWidgetId = appWidgetId;
    }

    public abstract RemoteViews inflateRemoteViews();
}
