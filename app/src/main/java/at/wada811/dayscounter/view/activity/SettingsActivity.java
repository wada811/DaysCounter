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
package at.wada811.dayscounter.view.activity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TextView;
import com.wada811.android.utils.LogUtils;
import com.wada811.android.utils.ResourceUtils;
import org.joda.time.LocalDateTime;
import java.io.File;
import at.wada811.dayscounter.CrashExceptionHandler;
import at.wada811.dayscounter.R;
import at.wada811.dayscounter.model.WidgetModel;
import at.wada811.dayscounter.model.utils.DatePickerUtils;
import at.wada811.dayscounter.observable.DatePickerBinding;
import at.wada811.dayscounter.observable.EditTextBinding;
import at.wada811.dayscounter.observable.EditTextBinding.EditTextTextChanged;
import at.wada811.dayscounter.observable.Func;
import at.wada811.dayscounter.view.appwidget.CounterAppWidgetProvider;
import at.wada811.dayscounter.viewmodel.Widget1x1ViewModel;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettingsActivity extends FragmentActivity implements OnClickListener {

    private int appWidgetId;
    private Widget1x1ViewModel viewModel;
    @InjectView(R.id.titleEditText) EditText titleEditText;
    @InjectView(R.id.datePicker) DatePicker datePicker;
    @InjectView(R.id.submitButton) Button submitButton;
    @InjectView(R.id.title) TextView titleTextView;
    @InjectView(R.id.diff) TextView diffTextView;
    @InjectView(R.id.comparison) TextView daysTextView;

    public static PendingIntent createPendingIntent(Context context, int appWidgetId){
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        return PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // catch UncaughtException
        Thread.setDefaultUncaughtExceptionHandler(new CrashExceptionHandler(getApplicationContext()));
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);
        // Window size dialog
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        LogUtils.d();
        // check Crash report
        File file = ResourceUtils.getFile(this, CrashExceptionHandler.FILE_NAME);
        if(file.exists()){
            startActivity(new Intent(this, CrashReportActivity.class));
            finish();
            return;
        }
        // AppWidgetId
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        LogUtils.d("appWidgetId: " + appWidgetId);

        WidgetModel model = new WidgetModel(this, appWidgetId);
        viewModel = new Widget1x1ViewModel(this, model);
        // Title
        new EditTextBinding(titleEditText).bind(new Func<EditText, String>() {
            @Override
            public String apply(EditText editText){
                return editText.getText().toString();
            }
        }, viewModel.getTitle(), new EditTextTextChanged() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                titleTextView.setText(viewModel.getTitle().getValue());
            }
        });
        titleEditText.setText(viewModel.getTitle().getValue());
        // Date
        String date = viewModel.getDate().getValue();
        LogUtils.d("date: " + date);
        LocalDateTime dateTime = date != null ? new LocalDateTime(date) : LocalDateTime.now();
        DatePickerBinding datePickerBinding = new DatePickerBinding(datePicker);
        datePickerBinding.bind(new Func<DatePicker, String>() {
            @Override
            public String apply(DatePicker datePicker){
                return DatePickerUtils.format(datePicker);
            }
        }, viewModel.getDate(), new OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                diffTextView.setText(viewModel.getDiff().getValue());
                diffTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewModel.getDiffTextSize().getValue());
                daysTextView.setText(viewModel.getComparison().getValue());
            }
        });
        datePicker.init(dateTime.getYear(),
            dateTime.getMonthOfYear() - 1,
            dateTime.getDayOfMonth(),
            datePickerBinding.getOnDateChangedListener());
        diffTextView.setText(viewModel.getDiff().getValue());
        diffTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewModel.getDiffTextSize().getValue());
        daysTextView.setText(viewModel.getComparison().getValue());

        // Button
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.submitButton:
                submit();
                break;
            default:
                break;
        }
    }

    private void submit(){
        LogUtils.d();
        viewModel.save();
        // updateAppWidget
        CounterAppWidgetProvider.updateAppWidget(this, AppWidgetManager.getInstance(this), appWidgetId);
        // setResult
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

}
