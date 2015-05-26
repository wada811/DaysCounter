package at.wada811.dayscounter.model.utils;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import com.wada811.android.utils.DisplayUtils;
import com.wada811.android.utils.LogUtils;

public class WidgetSize {

    public int width;
    public int height;

    public WidgetSize(int width, int height){
        this.width = width;
        this.height= height;
    }

    /**
     * App Widget Design Guidelines | Android Developers
     * https://developer.android.com/guide/practices/ui_guidelines/widget_design.html#anatomy_determining_size
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     * @return WidgetSize
     */
    public static WidgetSize calculate(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        AppWidgetProviderInfo appWidgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId);
        LogUtils.d("appWidgetInfo.minHeight: " + appWidgetInfo.minHeight);
        LogUtils.d("appWidgetInfo.minWidth: " + appWidgetInfo.minWidth);
        LogUtils.d("appWidgetInfo.minResizeHeight: " + appWidgetInfo.minResizeHeight);
        LogUtils.d("appWidgetInfo.minResizeWidth: " + appWidgetInfo.minResizeWidth);
        float dpi = DisplayUtils.getDensity(context);
        int widthCellCount = calculate(appWidgetInfo.minWidth, dpi);
        int heightCellCount = calculate(appWidgetInfo.minHeight, dpi);
        return new WidgetSize(widthCellCount, heightCellCount);
    }

    private static int calculate(int size, float dpi){
        return ((int)(size / dpi) + 30) / 70;
    }
}
