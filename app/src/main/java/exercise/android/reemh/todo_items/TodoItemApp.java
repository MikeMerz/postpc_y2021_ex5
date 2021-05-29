package exercise.android.reemh.todo_items;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TodoItemApp extends Application {

    public TodoItemsHolder db = null;

    @Override
    public void onCreate() {
        super.onCreate();
        if(db==null)
        {
            db = new TodoItemsHolderImpl(this);
        }

    }
}