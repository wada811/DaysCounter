package at.wada811.dayscounter.model.utils;

import android.widget.DatePicker;
import java.util.Locale;

public class DatePickerUtils {

    private DatePickerUtils(){}

    public static String format(DatePicker datePicker){
        return String.format(Locale.ENGLISH,
            "%04d-%02d-%02d",
            datePicker.getYear(),
            datePicker.getMonth() + 1,
            datePicker.getDayOfMonth());
    }
}
