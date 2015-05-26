package at.wada811.dayscounter.observable;

import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import java.util.ArrayList;
import java.util.List;

public class DatePickerBinding {

    private DatePicker datePicker;
    private List<OnDateChangedListener> onDateChangedListeners = new ArrayList<>();
    OnDateChangedListener onDateChangedListener = new OnDateChangedListener() {
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            for(OnDateChangedListener onDateChangedListener : onDateChangedListeners){
                onDateChangedListener.onDateChanged(view, year, monthOfYear, dayOfMonth);
            }
        }
    };

    public DatePickerBinding(DatePicker datePicker){
        this.datePicker = datePicker;
    }

    public OnDateChangedListener getOnDateChangedListener(){
        return onDateChangedListener;
    }

    public <TProperty> void bind(final Func<DatePicker, TProperty> propertySelector, final ObservableProperty<TProperty> source){
        bind(propertySelector, source, null);
    }

    public <TProperty> void bind(final Func<DatePicker, TProperty> propertySelector, final ObservableProperty<TProperty> source, final OnDateChangedListener updateSourceTrigger){
        OnDateChangedListener onDateChangedListener = new OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                source.setValue(propertySelector.apply(view));
                if(updateSourceTrigger != null){
                    updateSourceTrigger.onDateChanged(view, year, monthOfYear, dayOfMonth);
                }
            }
        };
        onDateChangedListeners.add(onDateChangedListener);
    }

    public void unbind(){
        onDateChangedListeners.clear();
    }

}
