package at.wada811.dayscounter.model;

import android.content.Context;
import com.wada811.android.utils.PreferenceUtils;
import com.wada811.observableproperty.ObservableProperty;
import org.joda.time.LocalDate;
import at.wada811.dayscounter.R;

public class WidgetModel {

    private final Context context;
    private final int appWidgetId;
    public ObservableProperty<String> title;
    public ObservableProperty<String> date;

    public WidgetModel(Context context, int appWidgetId){
        this.context = context;
        this.appWidgetId = appWidgetId;
        String title = PreferenceUtils.getString(context, context.getString(R.string.keyTitle, appWidgetId), "");
        this.title = new ObservableProperty<>(title);
        String today = LocalDate.now().toString();
        String date = PreferenceUtils.getString(context, context.getString(R.string.keyDate, appWidgetId), today);
        this.date = new ObservableProperty<>(date);
    }

    public void save(String title, String date){
        this.title.setValue(title);
        this.date.setValue(date);
        PreferenceUtils.putString(context, context.getString(R.string.keyTitle, appWidgetId), title);
        PreferenceUtils.putString(context, context.getString(R.string.keyDate, appWidgetId), date);
    }
}
