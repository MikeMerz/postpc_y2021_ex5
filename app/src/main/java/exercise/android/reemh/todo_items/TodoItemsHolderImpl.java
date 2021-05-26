package exercise.android.reemh.todo_items;


import java.io.Serializable;
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
    allList.remove(item);
    allList.add(item);
    item.setCurState(false);
  }

  @Override
  public void markItemInProgress(TodoItem item) {
    allList.remove(item);
    allList.add(0,item);
    item.setCurState(true);
  }

  @Override
  public void deleteItem(TodoItem item)
  {
    allList.remove(item);
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
}
