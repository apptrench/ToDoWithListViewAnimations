package com.ganesh.learn.android.todowithlistviewanimations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static com.ganesh.learn.android.todowithlistviewanimations.DatabaseHelper.Columns.ID;
import static com.ganesh.learn.android.todowithlistviewanimations.DatabaseHelper.Columns.PRIORITY;
import static com.ganesh.learn.android.todowithlistviewanimations.DatabaseHelper.TABLE_NAME;

/**
 * Created by Ganesh on 06-06-2015.
 */
public class PersistenceManager {
    private static final String TAG = "PersistenceManager";
    private SQLiteDatabase db;
    private DatabaseHelper helper;
    private static final int INITIAL_PRIORITY = 100;

    public PersistenceManager(Context context) {
        helper = DatabaseHelper.getInstance(context);
        db = helper.getWritableDatabase();
    }

    public void insertToDo(ToDo toDo) {
        final int lastPriority = getPriorityOfLastToDo();
        Log.i(TAG, "Last Priority = " + lastPriority);
        toDo.setPriority(lastPriority + INITIAL_PRIORITY);
        db.insert(TABLE_NAME, null, toDo.contentValues());
    }

    private int getPriorityOfLastToDo() {
        Cursor cursor = db.query(TABLE_NAME, helper.getAllColumnNames(), null, null, null, null, PRIORITY + " DESC", "1");
        Log.i(TAG, String.valueOf(cursor.getCount()));
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            return cursor.getInt(cursor.getColumnIndex(PRIORITY));
        }
        return 0;
    }

    public void swapPriorities(long sourceId, long destinationId) {
        int sourcePriority = getPriority(sourceId);
        int destinationPriority = getPriority(destinationId);

        int priorityOfRowBeforeDestination = getPriorityOfRowBeforeDestination(destinationPriority);
        int priorityOfRowAfterDestination = getPriorityOfRowAfterDestination(destinationPriority);

        updatePriority(sourceId, newPriority(sourcePriority, destinationPriority, priorityOfRowBeforeDestination, priorityOfRowAfterDestination));
    }

    private int newPriority(int sourcePriority, int destinationPriority, int priorityOfRowBeforeDestination, int priorityOfRowAfterDestination) {
        priorityOfRowAfterDestination = priorityOfRowAfterDestination == 0 ? destinationPriority + INITIAL_PRIORITY : priorityOfRowAfterDestination;
        return (sourcePriority > destinationPriority) ? destinationPriority - (destinationPriority - priorityOfRowBeforeDestination) / 2 : destinationPriority + (priorityOfRowAfterDestination - destinationPriority) / 2;
    }

    private void updatePriority(long id, int priority) {
        ContentValues record = new ContentValues();
        record.put(ID, id);
        record.put(PRIORITY, priority);
        db.update(TABLE_NAME, record, ID + "=?", getWhereArgs(id));
    }

    private String[] getWhereArgs(long id) {
        return new String[]{Long.toString(id)};
    }

    private String[] getWhereArgsWithPriority(int priority) {
        return new String[]{Integer.toString(priority)};
    }

    private int getPriority(long id) {
        int priority = 0;
        Cursor cursor = db.query(TABLE_NAME, helper.getAllColumnNames(), ID + "=?", getWhereArgs(id), null, null, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            priority = cursor.getInt(cursor.getColumnIndex(PRIORITY));
        }
        cursor.close();
        return priority;
    }

    private int getPriorityOfRowBeforeDestination(int destinationPriority) {
        int priority = 0;
        Cursor cursor = db.query(TABLE_NAME, helper.getAllColumnNames(), PRIORITY + "<?", getWhereArgsWithPriority(destinationPriority), null, null, PRIORITY + " DESC", "1");
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            priority = cursor.getInt(cursor.getColumnIndex(PRIORITY));
        }
        cursor.close();
        return priority;
    }

    private int getPriorityOfRowAfterDestination(int destinationPriority) {
        int priority = 0;
        Cursor cursor = db.query(TABLE_NAME, helper.getAllColumnNames(), PRIORITY + ">?", getWhereArgsWithPriority(destinationPriority), null, null, PRIORITY, "1");
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            priority = cursor.getInt(cursor.getColumnIndex(PRIORITY));
        }
        cursor.close();
        return priority;
    }

    public Cursor getAllToDosCursor() {
        return db.query(TABLE_NAME, helper.getAllColumnNames(), null, null, null, null, PRIORITY);
    }

    public void clearAllData() {
        db.execSQL("delete from " + TABLE_NAME);
    }

    public void delete(long id) {
        db.delete(TABLE_NAME, ID + "=?", getWhereArgs(id));
    }
}
