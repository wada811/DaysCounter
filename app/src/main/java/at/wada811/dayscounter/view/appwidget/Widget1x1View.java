package at.wada811.dayscounter.view.appwidget;

import android.content.Context;
import android.util.TypedValue;
import android.widget.RemoteViews;
import at.wada811.dayscounter.R;
import at.wada811.dayscounter.model.WidgetModel;
import at.wada811.dayscounter.view.activity.SettingsActivity;
import at.wada811.dayscounter.viewmodel.Widget1x1ViewModel;

public class Widget1x1View extends WidgetView {

    private final Widget1x1ViewModel viewModel;

    public Widget1x1View(Context context, int appWidgetId){
        super(context, appWidgetId);
        WidgetModel model = new WidgetModel(context,appWidgetId);
        viewModel = new Widget1x1ViewModel(context, model);
    }

    @Override
    public RemoteViews inflateRemoteViews(){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget);
        views.setOnClickPendingIntent(R.id.widget, SettingsActivity.createPendingIntent(context, appWidgetId));
        views.setTextViewText(R.id.title, viewModel.getTitle().getValue());
        views.setTextViewText(R.id.diff, viewModel.getDiff().getValue());
        views.setTextViewTextSize(R.id.diff, TypedValue.COMPLEX_UNIT_PX, viewModel.getDiffTextSize().getValue());
        views.setTextViewText(R.id.comparison, viewModel.getComparison().getValue());
        return views;
    }
}
