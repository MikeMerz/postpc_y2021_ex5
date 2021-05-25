package exercise.android.reemh.todo_items;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// TODO: implement!
public class TodoItemsHolderImpl implements TodoItemsHolder {
  private List<TodoItem> allList = new ArrayList<>();


  @Override
  public List<TodoItem> getCurrentItems() { return allList; }

  @Override
  public void addNewInProgressItem(String description)
  {
    TodoItem temp = new TodoItem();
    temp.setDesc(description);
    allList.add(0,temp);
  }

  @Override
  public void markItemDone(TodoItem item)
  {
    for(int i=0;i<allList.size();++i)
    {
      if (item == allList.get(i))
      {
        allList.get(i).setCurState(true);
      }
    }
  }

  @Override
  public void markItemInProgress(TodoItem item) {item.setCurState(false);}

  @Override
  public void deleteItem(TodoItem item)
  {
    allList.remove(item);
  }

}
