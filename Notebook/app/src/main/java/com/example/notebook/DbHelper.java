package com.example.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {


    public static final String DBNAME = "Notebook.db";

    public static final String LABEL_NAME = "name";
    public static final String PASSWORD = "password";
    public static final String LABEL_TABLE_NAME = "Label";

    public static final String NOTES_TABLE_NAME = "Notes";
    public static final String TITLE = "title";
    public static final String CONTENTS = "contents";
    public static final String TIMESTAMP = "timestamp";

    String CREATE_LABEL_TABLE = String.format("create table %s (%s text NOT NULL PRIMARY KEY, %s text);",
            LABEL_TABLE_NAME, LABEL_NAME, PASSWORD);

    String CREATE_NOTE_TABLE = String.format(
            "create table %s (%s text, %s text, %s text NOT NULL PRIMARY KEY, %s text," +
                    " FOREIGN KEY(%s) REFERENCES %s(%s) ON DELETE CASCADE ON UPDATE CASCADE);",
            NOTES_TABLE_NAME, TITLE, CONTENTS, TIMESTAMP, LABEL_NAME, LABEL_NAME, LABEL_TABLE_NAME,
            LABEL_NAME
    );

    String DELETE_LABEL_TABLE = String.format("drop table if exists %s", LABEL_TABLE_NAME);
    String DELETE_NOTE_TABLE = String.format("drop table if exists %s", NOTES_TABLE_NAME);

    public DbHelper(@Nullable Context context) {
        super(context, DBNAME, null, 1);;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LABEL_TABLE);
        db.execSQL(CREATE_NOTE_TABLE);

        ContentValues values = new ContentValues();
        values.put(LABEL_NAME, "General");
        values.put(PASSWORD, "");
        db.insert(LABEL_TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_NOTE_TABLE);
        db.execSQL(DELETE_LABEL_TABLE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
