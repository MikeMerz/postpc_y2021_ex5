package exercise.android.reemh.todo_items;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

// TODO: implement!
public class TodoItemsHolderImpl implements TodoItemsHolder {
  private List<TodoItem> allList = new ArrayList<>();
  private Context context;
  private SharedPreferences sp;
  private Set<String> allIdsSet;

  static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

  public TodoItemsHolderImpl(Context context)
  {
    this.context = context;
    this.sp = context.getSharedPreferences("local_change",Context.MODE_PRIVATE);
    initFromSp();
  }
  class compareJobs implements Comparator<TodoItem>
  {
    @Override
    public int compare(TodoItem item, TodoItem t1) {
      if(item.getCurState() == t1.getCurState())
      {
        if(item.getModifiedDate().isAfter(t1.getModifiedDate()))
        {
          return 1;
        }else if(t1.getModifiedDate().isAfter(item.getModifiedDate()))
        {
          return  -1;
        }
        else
        {
          return 0;
        }
      }
      else
      {
        if(item.getCurState())
        {
          return -1;
        }
        return 1;
      }
    }
  }

  private void initFromSp()
  {
    this.allIdsSet= this.sp.getStringSet("allIdsSet",new HashSet<>());
    if(this.allIdsSet != null)
    {
      for(String key:this.allIdsSet)
      {
        TodoItem item = new TodoItem();
        item.setModifiedDate(LocalDateTime.parse(this.sp.getString(key+"modifiedDate",""),formatter));
        item.setModifiedDate(LocalDateTime.parse(this.sp.getString(key+"creationDate",""),formatter));
        item.setCurState(this.sp.getBoolean(key+"state",false));
        item.setDesc(this.sp.getString(key+"desc",""));
        item.setId(key);
        this.allList.add(item);
      }
    }
    this.allList.sort(new compareJobs());

  }
  private void updateSp(TodoItem item)
  {
    this.allIdsSet.add(item.getId());
    SharedPreferences.Editor edit = this.sp.edit();
    edit.putString(item.getId()+"desc",item.getDesc());
    edit.putBoolean(item.getId()+"state",item.getCurState());
    edit.putString(item.getId()+"creationDate",item.getCreateDate().format(formatter));
    edit.putString(item.getId()+"modifiedDate",item.getModifiedDate().format(formatter));
    edit.putStringSet("allIdsSet",allIdsSet);
    edit.apply();
  }
  private void removeFromSp(TodoItem item)
  {
    SharedPreferences.Editor edit = this.sp.edit();
    edit.remove(item.getId()+"desc");
    edit.remove(item.getId()+"state");
    edit.remove(item.getId()+"creationDate");
    edit.remove(item.getId()+"modifiedDate");
    this.allIdsSet.remove(item.getId());
    edit.apply();
  }

  public void changeDesc(TodoItem item,String des)
  {
    int oldPosition = this.allList.indexOf(item);
    item.setModifiedDate(LocalDateTime.now());
    item.setDesc(des);
    if(item.getCurState())
    {
      this.allList.remove(item);
      this.allList.add(0,item);
      sendBroadCast("item_to_progress",oldPosition);
    }
    updateSp(item);

  }
  @Override
  public List<TodoItem> getCurrentItems() { return allList; }

  @Override
  public void addNewInProgressItem(String description)
  {
    String newId = UUID.randomUUID().toString();
    TodoItem temp = new TodoItem();
    temp.setDesc(description);
    temp.setId(newId);
    allList.add(0,temp);
    updateSp(temp);
    sendBroadCast("added_item",0);
  }

  @Override
  public void markItemDone(TodoItem item)
  {
    int old_pos = this.allList.indexOf(item);
    allList.remove(item);
    allList.add(item);
    item.setCurState(false);
    updateSp(item);
    sendBroadCast("item_to_done",old_pos);
  }

  @Override
  public void markItemInProgress(TodoItem item) {
    int old_pos = this.allList.indexOf(item);
    allList.remove(item);
    allList.add(0,item);
    item.setCurState(true);
    updateSp(item);
    sendBroadCast("item_to_progress",old_pos);
  }

  @Override
  public void deleteItem(TodoItem item)
  {
    int old_pos = this.allList.indexOf(item);
    allList.remove(item);
    removeFromSp(item);
    sendBroadCast("deleted_item",old_pos);
  }
  @Override
  public Serializable saveState()
  {
    TodoState state = new TodoState();
    state.savedItems = allList;
    return state;
  }
  @Override
  public void loadState(Serializable state)
  {
    TodoState state2 = (TodoState) state;
    allList = state2.savedItems;

  }
  private static class TodoState implements Serializable{
    private List<TodoItem> savedItems;
  }

  private void sendBroadCast(String keyWord,int old_pos)
  {
    Intent broad = new Intent("db_change");
    broad.putExtra(keyWord,old_pos); //TODO MAYBE DOES PROBLEMS
    this.context.sendBroadcast(broad);;
  }
}