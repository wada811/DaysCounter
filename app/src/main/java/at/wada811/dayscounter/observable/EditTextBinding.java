package at.wada811.dayscounter.observable;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;

public class EditTextBinding {

    private EditText editText;
    private List<TextWatcher> textWatchers = new ArrayList<>();

    public interface EditTextTextChanged {

        void onTextChanged(CharSequence s, int start, int before, int count);
    }

    public EditTextBinding(EditText editText){
        this.editText = editText;
    }

    public void bind(final Func<EditText, String> propertySelector, final ObservableProperty<String> source){
        bind(propertySelector, source, null);
    }

    public void bind(final Func<EditText, String> propertySelector, final ObservableProperty<String> source, final EditTextTextChanged updateSourceTrigger){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                source.setValue(propertySelector.apply(editText));
                if(updateSourceTrigger != null){
                    updateSourceTrigger.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void afterTextChanged(Editable s){
            }
        };
        textWatchers.add(textWatcher);
        editText.addTextChangedListener(textWatcher);
    }

    public void unbind(){
        for(TextWatcher textWatcher : textWatchers){
            editText.removeTextChangedListener(textWatcher);
        }
        textWatchers.clear();
    }

}
