//package com.example.notebook;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//
//public class NotesDbHelper extends SQLiteOpenHelper {
//
//    public static final String TABLE_NAME = "Notes";
//    public static final String TITLE = "title";
//    public static final String CONTENTS = "contents";
//    public static final String TIMESTAMP = "timestamp";
//    public static final String DBNAME = "Notes.db";
//
//    public  static final String CREATE_TABLE = String.format(
//            "create table %s (%s text, %s text, %s text NOT NULL PRIMARY KEY, %s text," +
//                    " FOREIGN KEY(%s) REFERENCES %s(%s) ON DELETE CASCADE);",
//            TABLE_NAME, TITLE, CONTENTS, TIMESTAMP, LabelDbHelper.LABEL_NAME,
//            LabelDbHelper.LABEL_NAME, LabelDbHelper.TABLE_NAME, LabelDbHelper.LABEL_NAME
//    );
//
//    public NotesDbHelper(Context context) {
//        super(context, DBNAME, null, 1);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//
//        db.execSQL(CREATE_TABLE);
//
//        Log.d("CREATE_TABLE command" , CREATE_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL(String.format(
//                "drop table if exists %s", TABLE_NAME
//        ));
//        onCreate(db);
//    }
//}
