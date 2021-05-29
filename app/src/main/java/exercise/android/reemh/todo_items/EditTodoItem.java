package exercise.android.reemh.todo_items;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditTodoItem extends AppCompatActivity {

    TodoItemsHolder holder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_todo_activity);

        TodoItemApp app = (TodoItemApp) getApplicationContext();
        this.holder = app.db;

        TextView dateCreated = findViewById(R.id.editTextDate);
        TextView dateModified = findViewById(R.id.editTextDate2);
        CheckBox curState = findViewById(R.id.checkBox2);
        TextInputEditText desc = findViewById(R.id.textView8);

        dateCreated.setInputType(InputType.TYPE_NULL);
        dateModified.setInputType(InputType.TYPE_NULL);


        Intent intent = getIntent();
        int position = intent.getIntExtra("pos",-1);


        TodoItem item = holder.getCurrentItems().get(position);
        curState.setChecked(!item.getCurState());
        desc.setText(item.getDesc());
        dateCreated.setText(convertDateToString(item.getCreateDate()));
        dateModified.setText(chooseCorrectFormatLastModified(item.getModifiedDate()));

        curState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!curState.isChecked())
                {
                    holder.markItemInProgress(item);
                    LocalDateTime modifiedDate = item.getModifiedDate();
                    dateModified.setText(chooseCorrectFormatLastModified(modifiedDate));
                }
                else
                {
                    holder.markItemDone(item);
                    LocalDateTime modifiedDate = item.getModifiedDate();
                    dateModified.setText(chooseCorrectFormatLastModified(modifiedDate));

                }
            }
        });
        desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                holder.changeDesc(item,editable.toString());
            }
        });


    }
    private String convertDateToString(LocalDateTime curDate)
    {
        return  curDate.format(TodoItemsHolderImpl.formatter).replace('T',' ').substring(0,19);
    }
    private String chooseCorrectFormatLastModified(LocalDateTime curDate)
    {
        LocalDateTime now = LocalDateTime.now();
        String minutes;
        if(curDate.getMinute()<10)
        {
            minutes = "0" +curDate.getMinute();
        }
        else
        {
            minutes = Integer.toString(curDate.getMinute());
        }
        if(now.getYear() == curDate.getYear() && now.getDayOfYear() == curDate.getDayOfYear() && now.getHour() - curDate.getHour()==0)
        {
            return (now.getMinute() - curDate.getMinute()) + "minutes ago";
        }
        else if(now.getYear() == curDate.getYear() && now.getDayOfYear() == curDate.getDayOfYear())
        {
            return "Today at "+curDate.getHour() + ":" + minutes;
        }
        else
        {
            return curDate.format(DateTimeFormatter.ofPattern("yyyy MM dd"))+"at" + curDate.getHour() + ":" + minutes;
        }
    }
}