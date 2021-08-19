//package com.example.notebook;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import androidx.annotation.Nullable;
//
//import java.net.PasswordAuthentication;
//
//public class LabelDbHelper extends SQLiteOpenHelper {
//
//    public static final String LABEL_NAME = "name";
//    public static final String PASSWORD = "password";
//    public static final String TABLE_NAME = "Label";
//    public static final String DBNAME = "NOTEBOOK.db";
//
//
//
//    public LabelDbHelper(Context context) {
//        super(context, DBNAME, null, 1);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(String.format("create table %s (%s text NOT NULL PRIMARY KEY, %s text);",
//                TABLE_NAME, LABEL_NAME, PASSWORD));
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL(String.format("drop table if exists %s", TABLE_NAME));
//        onCreate(db);
//    }
//}
