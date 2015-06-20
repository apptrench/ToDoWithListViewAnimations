package com.ganesh.learn.android.todowithlistviewanimations;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;


public class MainActivity extends ActionBarActivity implements NewTaskDialog.OnSaveListener {

    private DynamicListView listView;
    private ToDoListAdapter adapter;
    private PersistenceManager persistenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPersistenceManager();
        setUpListView();
    }

    private void initPersistenceManager() {
        persistenceManager = new PersistenceManager(this);
    }

    private void setUpListView() {
        listView = (DynamicListView) findViewById(R.id.listview);
        listView.enableDragAndDrop();
        listView.setOnItemLongClickListener(new MyItemLongClickListener(listView));
        listView.setDraggableManager(new TouchViewDraggableManager(R.id.gripview));

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(adapter);
        alphaInAnimationAdapter.setAbsListView(listView);

        listView.enableSwipeToDismiss(new OnDismissCallback() {
            @Override
            public void onDismiss(@NonNull ViewGroup listView, @NonNull int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    persistenceManager.delete(adapter.getItemId(position));
                }
                adapter.changeCursor(persistenceManager.getAllToDosCursor());
            }
        });

        setUpAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemMovedListener(adapter);
    }

    private void setUpAdapter() {
        adapter = new ToDoListAdapter(this, persistenceManager.getAllToDosCursor(), 0, persistenceManager);
    }

    private void addNewTask() {
        Log.i("", "About to add new task");
        NewTaskDialog dialog = NewTaskDialog.newInstance(this);
        dialog.show(getSupportFragmentManager(), "newTask");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_task) {
            addNewTask();
            return true;
        }else if (id == R.id.deleteAllRecords) {
            clearAllRecords();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearAllRecords() {
        persistenceManager.clearAllData();
        persistenceManager.insertToDo(new ToDo("One"));
        persistenceManager.insertToDo(new ToDo("Two"));
        persistenceManager.insertToDo(new ToDo("Three"));
        persistenceManager.insertToDo(new ToDo("Four"));
        adapter.changeCursor(persistenceManager.getAllToDosCursor());
    }

    @Override
    public void onNewTaskCreated(ToDo toDo) {
        persistenceManager.insertToDo(toDo);
        adapter.changeCursor(persistenceManager.getAllToDosCursor());
    }

    private static class MyItemLongClickListener implements AdapterView.OnItemLongClickListener {
        private final DynamicListView listView;

        private MyItemLongClickListener(DynamicListView listView) {
            this.listView = listView;
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (listView != null) {
                listView.startDragging(position);
            }
            return true;
        }
    }
}
