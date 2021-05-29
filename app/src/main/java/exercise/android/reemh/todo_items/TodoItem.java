package exercise.android.reemh.todo_items;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TodoItem implements Serializable {
    // TODO: edit this class as you want
    private boolean curState; // TRUE-IN PROGRESS -FALSE DONE
    private String desc;
    private String id;
    private long createTime;
    private LocalDateTime createDate = LocalDateTime.now();
    private LocalDateTime modifiedDate = LocalDateTime.now();
    public TodoItem()
    {
        curState = true;
        createTime = System.currentTimeMillis();
    }
    public void setId(String id){this.id=id;}
    public String getId(){return this.id;}
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
    public LocalDateTime getCreateDate() {return createDate;}
    public LocalDateTime getModifiedDate(){return modifiedDate;}
    public void setModifiedDate(LocalDateTime newDate)
    {
        this.modifiedDate = newDate;
    }

}