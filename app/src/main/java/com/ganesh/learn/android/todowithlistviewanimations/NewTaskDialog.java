package com.ganesh.learn.android.todowithlistviewanimations;


import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewTaskDialog extends DialogFragment {
    private OnSaveListener onSaveListener;

    public static interface OnSaveListener {
        void onNewTaskCreated(ToDo toDo);
    }

    public NewTaskDialog() {
        // Required empty public constructor
    }

    public static NewTaskDialog newInstance(OnSaveListener onSaveListener) {
        NewTaskDialog dialog = new NewTaskDialog();
        dialog.onSaveListener = onSaveListener;
        return dialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_newtask_dialog, container, false);

        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        view.setMinimumWidth((int) (displayRectangle.width() * 0.9f));

        Button button = (Button) view.findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = ((EditText) view.findViewById(R.id.new_task)).getText().toString();
                onSaveListener.onNewTaskCreated(new ToDo(task));
                NewTaskDialog.this.dismiss();
            }
        });
        return view;
    }
}
