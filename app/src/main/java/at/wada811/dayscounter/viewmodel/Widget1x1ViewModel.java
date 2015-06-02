package at.wada811.dayscounter.viewmodel;

import android.content.Context;
import com.wada811.observableproperty.ObservableProperty;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutablePeriod;
import at.wada811.dayscounter.R;
import at.wada811.dayscounter.model.WidgetModel;
import at.wada811.dayscounter.model.utils.WidgetTextSize;
import backport.java8.util.function.Function;

public class Widget1x1ViewModel {

    private WidgetModel model;
    private ObservableProperty<String> title;
    private ObservableProperty<String> date;
    private ObservableProperty<Integer> diffDays;
    private ObservableProperty<String> diff;
    private ObservableProperty<Float> diffTextSize;
    private ObservableProperty<String> comparison;

    public Widget1x1ViewModel(final Context context, WidgetModel model){
        this.model = model;
        this.title = model.title.createReactiveObservableProperty();
        this.date = model.date.createReactiveObservableProperty();
        this.diffDays = this.date.createReactiveObservableProperty(new Function<String, Integer>() {
            @Override
            public Integer apply(String date){
                return calcDiffDays(date);
            }
        });
        this.diff = this.diffDays.createReactiveObservableProperty(new Function<Integer, String>() {
            @Override
            public String apply(Integer diffDays){
                return String.valueOf(Math.abs(diffDays));
            }
        });
        this.diffTextSize = this.diff.createReactiveObservableProperty(new Function<String, Float>() {
            @Override
            public Float apply(String diff){
                int dimenId = WidgetTextSize.getSize(diff.length());
                return context.getResources().getDimension(dimenId);
            }
        });
        this.comparison = this.diffDays.createReactiveObservableProperty(new Function<Integer, String>() {
            @Override
            public String apply(Integer diffDays){
                if(diffDays == 0){
                    return context.getString(R.string.day_today);
                }else if(diffDays > 0){
                    return context.getString(R.string.day_before);
                }else{
                    return context.getString(R.string.day_after);
                }
            }
        });
    }

    public ObservableProperty<String> getTitle(){
        return title;
    }

    public ObservableProperty<String> getDate(){
        return date;
    }

    public ObservableProperty<Integer> getDiffDays(){
        return diffDays;
    }

    public ObservableProperty<String> getDiff(){
        return diff;
    }

    public ObservableProperty<Float> getDiffTextSize(){
        return diffTextSize;
    }

    public ObservableProperty<String> getComparison(){
        return comparison;
    }

    private int calcDiffDays(String date){
        LocalDateTime dateTime = new LocalDate(date).toLocalDateTime(new LocalTime(0, 0));
        LocalDateTime today = new LocalDate().toLocalDateTime(new LocalTime(0, 0));
        MutablePeriod diff = new MutablePeriod(dateTime.toDateTime().getMillis(), today.toDateTime().getMillis());
        Duration diffDuration = new Duration(dateTime.toDateTime(), today.toDateTime());
        int diffDays = diff.getDays();
        diffDays = (int)diffDuration.getStandardDays();
        return diffDays;
    }

    public void save(){
        model.save(title.getValue(), date.getValue());
    }
}
