package exercise.android.reemh.todo_items;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

  public TodoItemsHolder holder = null;
  private BroadcastReceiver receiverDBChange;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    TodoItemApp app = (TodoItemApp) getApplicationContext();
    this.holder = app.db;
    if (holder == null) {
      holder = new TodoItemsHolderImpl(this);
    }
    EditText editText = findViewById(R.id.editTextInsertTask);
    FloatingActionButton createTodoItem = findViewById(R.id.buttonCreateTodoItem);
    RecyclerView recycler = findViewById(R.id.recyclerTodoItemsList);
    TodoAdapterImpl todoAdapter = new TodoAdapterImpl(holder);
    // TODO: implement the specs as defined below
    //    (find all UI components, hook them up, connect everything you need)

    recycler.setAdapter(todoAdapter);
    recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
    createTodoItem.setOnClickListener(view -> {
      String des = editText.getText().toString();
      if(!des.matches(""))
      {
        holder.addNewInProgressItem(des);
//        todoAdapter.notifyItemInserted(0);
        editText.setText("");
      }
    });
    todoAdapter.setCheckBoxClickListener(position -> {
      TodoItem todoItem = holder.getCurrentItems().get(position);
      boolean state = todoItem.getCurState();
      if(!state)
      {
        holder.markItemInProgress(todoItem);
//        todoAdapter.notifyItemMoved(position,0);
      }
      else
      {
        holder.markItemDone(todoItem);
//        todoAdapter.notifyItemMoved(position,holder.getCurrentItems().size()-1);
      }
    });
    todoAdapter.setonDeleteCallback(position -> {
      holder.deleteItem(holder.getCurrentItems().get(position));
//      todoAdapter.notifyItemRemoved(position);
    });
    todoAdapter.setonEditCallback(position -> {
      Intent newIntent = new Intent(MainActivity.this,EditTodoItem.class);
      newIntent.putExtra("pos",position);
      startActivity(newIntent);
    });

    receiverDBChange = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        if(intent.getAction()!= null && intent.getAction().equals("db_change"))
        {
          if (intent.getIntExtra("added_item",-1) != -1)
          {
            todoAdapter.notifyItemInserted(intent.getIntExtra("added_item",-1));
          }
          else if(intent.getIntExtra("item_to_done",-1) != -1)
          {
            todoAdapter.notifyItemMoved(intent.getIntExtra("item_to_done",-1),holder.getCurrentItems().size()-1);
          }
          else if(intent.getIntExtra("item_to_progress",-1) != -1)
          {
            todoAdapter.notifyItemMoved(intent.getIntExtra("item_to_done",-1),0);
          }
          else if(intent.getIntExtra("deleted_item",-1) != -1)
          {
            todoAdapter.notifyItemRemoved(intent.getIntExtra("deleted_item",-1));
          }
          todoAdapter.notifyDataSetChanged();
        }
      }
    };
    registerReceiver(receiverDBChange,new IntentFilter("db_change"));

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(receiverDBChange);
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable("toDo",holder.saveState());
  }

  @Override
  public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    holder.loadState(savedInstanceState.getSerializable("toDo"));

  }
}

/*

SPECS:

- the screen starts out empty (no items shown, edit-text input should be empty)
- every time the user taps the "add TODO item" button:
    * if the edit-text is empty (no input), nothing happens
    * if there is input:
        - a new TodoItem (checkbox not checked) will be created and added to the items list
        - the new TodoItem will be shown as the first item in the Recycler view
        - the edit-text input will be erased
- the "TodoItems" list is shown in the screen
  * every operation that creates/edits/deletes a TodoItem should immediately be shown in the UI
  * the order of the TodoItems in the UI is:
    - all IN-PROGRESS items are shown first. items are sorted by creation time,
      where the last-created item is the first item in the list
    - all DONE items are shown afterwards, no particular sort is needed (but try to think about what's the best UX for the user)
  * every item shows a checkbox and a description. you can decide to show other data as well (creation time, etc)
  * DONE items should show the checkbox as checked, and the description with a strike-through text
  * IN-PROGRESS items should show the checkbox as not checked, and the description text normal
  * upon click on the checkbox, flip the TodoItem's state (if was DONE will be IN-PROGRESS, and vice versa)
  * add a functionality to remove a TodoItem. either by a button, long-click or any other UX as you want
- when a screen rotation happens (user flips the screen):
  * the UI should still show the same list of TodoItems
  * the edit-text should store the same user-input (don't erase input upon screen change)

Remarks:
- you should use the `holder` field of the activity
- you will need to create a class extending from RecyclerView.Adapter and use it in this activity
- notice that you have the "row_todo_item.xml" file and you can use it in the adapter
- you should add tests to make sure your activity works as expected. take a look at file `MainActivityTest.java`



(optional, for advanced students:
- save the TodoItems list to file, so the list will still be in the same state even when app is killed and re-launched
)

*/
