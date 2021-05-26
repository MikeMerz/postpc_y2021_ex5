package exercise.android.reemh.todo_items;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Comparator;

public class TodoAdapterImpl  extends RecyclerView.Adapter<TodoHolder> {
    private TodoItemsHolder itemsHolder;

    interface checkBoxClickListener{
         void onCheckBoxCallback(int position);}
    interface onDelete{
         void onDeleteCallback(int position);
    }

    private checkBoxClickListener checkBoxClickListener;
    private onDelete  deleteCallbackListener;


    public TodoAdapterImpl(TodoItemsHolder holder2)
    {
        itemsHolder = holder2;
    }
    @Override
    public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_todo_item, parent, false);
        return  new TodoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoHolder holder, int position) {
        TodoItem todoItem = itemsHolder.getCurrentItems().get(position);
        if(todoItem != null)
        {
            holder.changeViewText(todoItem.getDesc());
            holder.getCheckBox().setChecked(!todoItem.getCurState());
            if(!holder.getCheckBox().isChecked())
            {
                holder.getTextView().setPaintFlags( holder.getTextView().getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
            else
            {
                holder.getTextView().setPaintFlags( holder.getTextView().getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
        CheckBox checkBox = holder.getCheckBox();
        checkBox.setOnClickListener(view -> {
            if(checkBoxClickListener != null)
            {
                checkBoxClickListener.onCheckBoxCallback(holder.getAdapterPosition());
            }
            if(!holder.getCheckBox().isChecked())
            {
                holder.getTextView().setPaintFlags( holder.getTextView().getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
            else
            {
                holder.getTextView().setPaintFlags( holder.getTextView().getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });
        Button delButton = holder.getDeleteButton();
        delButton.setOnClickListener(view -> {
            if(deleteCallbackListener != null)
            {
                deleteCallbackListener.onDeleteCallback(holder.getAdapterPosition());
            }
        });

    }
    @Override
    public int getItemCount() {
        return itemsHolder.getCurrentItems().size();
    }
    public void setCheckBoxClickListener(checkBoxClickListener toSet){
        checkBoxClickListener = toSet;
    }
    public void setonDeleteCallback(onDelete toSet){
        deleteCallbackListener = toSet;
    }
}
