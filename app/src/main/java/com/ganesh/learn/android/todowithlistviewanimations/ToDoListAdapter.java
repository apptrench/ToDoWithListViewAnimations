package com.ganesh.learn.android.todowithlistviewanimations;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.OnItemMovedListener;
import com.nhaarman.listviewanimations.util.Swappable;

import java.util.HashMap;
import java.util.Map;

import static com.ganesh.learn.android.todowithlistviewanimations.DatabaseHelper.Columns.TASK;

/**
 * Created by Ganesh on 06-06-2015.
 */
public class ToDoListAdapter extends CursorAdapter implements Swappable, OnItemMovedListener {
    private static final String TAG = "ToDoListAdapter";
    private Map<Integer, Integer> swappedPositions = new HashMap<>();

    private final PersistenceManager persistenceManager;

    public ToDoListAdapter(Context context, Cursor c, int flags, PersistenceManager persistenceManager) {
        super(context, c, flags);
        this.persistenceManager = persistenceManager;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String task = cursor.getString(cursor.getColumnIndex(TASK));
        TextView taskView = (TextView) view.getTag();

        if (taskView == null) {
            taskView = (TextView) view.findViewById(R.id.task);
            view.setTag(taskView);
        }
        taskView.setText(task);
    }

    @Override
    public void swapItems(int destination, int source) {
        final int origDestination = getSwappedPositionIfExists(destination);
        swappedPositions.put(destination, getSwappedPositionIfExists(source));
        notifyDataSetChanged();
        swappedPositions.put(source, origDestination);
    }

    private int getSwappedPositionIfExists(int source) {
        return swappedPositions.containsKey(source) ? swappedPositions.get(source) : source;
    }


    @Override
    public long getItemId(int position) {
        int swappedPosition = getSwappedPositionIfExists(position);
        return super.getItemId(swappedPosition);
    }

    @Override
    public Object getItem(int position) {
        int swappedPosition = getSwappedPositionIfExists(position);
        return super.getItem(swappedPosition);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int swappedPosition = getSwappedPositionIfExists(position);
        return super.getView(swappedPosition, convertView, parent);
    }

    @Override
    public void onItemMoved(int source, int destination) {
        //Commit the changes to the database
        swappedPositions.clear();
        persistenceManager.swapPriorities(getItemId(source), getItemId(destination));
        changeCursor(persistenceManager.getAllToDosCursor());
    }
}
