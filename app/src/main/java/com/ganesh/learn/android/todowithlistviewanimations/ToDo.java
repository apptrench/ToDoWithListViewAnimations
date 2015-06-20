package com.ganesh.learn.android.todowithlistviewanimations;

import android.content.ContentValues;
import android.util.Log;

/**
 * Created by Ganesh on 06-06-2015.
 */
public class ToDo {
    private final String task;
    private int priority;

    public ToDo(String task) {
        this.task=task;
    }

    public ToDo(String task, int priority) {
        this.task = task;
        this.priority = priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Columns.TASK, this.task);
        values.put(DatabaseHelper.Columns.PRIORITY, this.priority);
        return  values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ToDo toDo = (ToDo) o;

        if (priority != toDo.priority) return false;
        return task.equals(toDo.task);

    }

    @Override
    public int hashCode() {
        int result = task.hashCode();
        result = 31 * result + priority;
        Log.i("ToDo", result+"");
        return result;
    }


}
