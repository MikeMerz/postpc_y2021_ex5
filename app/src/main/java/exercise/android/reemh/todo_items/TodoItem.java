package exercise.android.reemh.todo_items;

import java.io.Serializable;

public class TodoItem implements Serializable {
  // TODO: edit this class as you want
    private boolean curState; // TRUE-IN PROGRESS -FALSE DONE
    private String desc;
    private long createTime;
    public TodoItem()
    {
        curState = false;
        createTime = System.currentTimeMillis();
    }
    public boolean getCurState(){return curState;}
    public void setCurState(boolean state)
    {
        curState = state;
    }
    public void setDesc(String des)
    {
        desc = des;
    }
    public String getDesc()
    {
        return desc;
    }
    public long getCreateTIme()
    {
        return createTime;
    }


}
