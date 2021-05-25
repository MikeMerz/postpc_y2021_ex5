package exercise.android.reemh.todo_items

import java.util.*

// TODO: implement!
class TodoItemsHolderImpl : TodoItemsHolder {
    private val allList: MutableList<TodoItem> = ArrayList()
    override fun getCurrentItems(): List<TodoItem> {
        return allList
    }

    override fun addNewInProgressItem(description: String) {
        val temp = TodoItem()
        temp.desc = description
        allList.add(0, temp)
    }

    override fun markItemDone(item: TodoItem) {
        for (i in allList.indices) {
            if (item === allList[i]) {
                allList[i].curState = true
            }
        }
    }

    override fun markItemInProgress(item: TodoItem) {
        item.curState = false
    }

    override fun deleteItem(item: TodoItem) {
        allList.remove(item)
    }
}