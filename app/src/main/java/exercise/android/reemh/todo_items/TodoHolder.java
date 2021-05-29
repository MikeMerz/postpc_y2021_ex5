package exercise.android.reemh.todo_items;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class TodoHolder extends RecyclerView.ViewHolder
{
    private TextView editText;
    private CheckBox checkBox;
    private Button imageButton;
    private View itemView;
    public TodoHolder(final View itemView) {
        super(itemView);
        editText = itemView.findViewById(R.id.textView);
        checkBox = itemView.findViewById(R.id.checkBox);
        imageButton = itemView.findViewById(R.id.button);
        this.itemView = itemView;

    }
    public void changeViewText(String text)
    {
        editText.setText(text);
    }
    public void setCheckBox(boolean status)
    {
        checkBox.setChecked(status);
    }
    public CheckBox getCheckBox(){return checkBox;}
    public Button getDeleteButton(){return imageButton;}
    public TextView getTextView(){return editText;}
    public View getRow(){return this.itemView;}

}