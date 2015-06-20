package com.ganesh.learn.android.todowithlistviewanimations;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ganesh on 02-03-2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static DatabaseHelper helper;

    static class Columns {
        static String ID = "_id";
        public static final String PRIORITY = "PRIORITY";
        static String TASK = "TASK";
        static String DUE_DATE = "DUE_DATE";
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "todolist.db";
    static final String TABLE_NAME = "TODO";
    private static final String COMMA_SEP = ",";
    private static final String TEXT_TYPE = " text ";
    public static final String INT_TYPE = " INTEGER ";
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    Columns.ID + INT_TYPE + "PRIMARY KEY" + COMMA_SEP +
                    Columns.TASK + TEXT_TYPE + COMMA_SEP +
                    Columns.PRIORITY + INT_TYPE +
                    " )";

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        super(context, null, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (helper == null) {
            helper = new DatabaseHelper(context);
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public String[] getAllColumnNames() {
        return new String[]{Columns.ID, Columns.TASK, Columns.PRIORITY};
    }
}
