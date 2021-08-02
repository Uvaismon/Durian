package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CpuUsageInfo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    NotesDbHelper notesDbHelper;
    SQLiteDatabase notesDb;
    String[] title, contents, timestamp;
    NotesListAdapter notesListAdapter;
    RecyclerView mainRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainRecyclerView = findViewById(R.id.mainRecyclerView);

        loadNotesList();

        notesListAdapter = new NotesListAdapter(MainActivity.this, title, contents
                , timestamp);
        mainRecyclerView.setAdapter(notesListAdapter);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }

    private void loadNotesList() {
        notesDbHelper = new NotesDbHelper(this);
        notesDb = notesDbHelper.getReadableDatabase();
        String[] projections = {NotesDbHelper.TITLE, NotesDbHelper.CONTENTS,
                NotesDbHelper.TIMESTAMP};

        Cursor c = notesDb.query(NotesDbHelper.TABLE_NAME, projections, null,
                null, null ,null, NotesDbHelper.TIMESTAMP);

        title = new String[c.getCount()];
        contents = new String[c.getCount()];
        timestamp = new String[c.getCount()];

        for (int i = 0; c.moveToNext(); i++){
            title[i] = c.getString(0);
            contents[i] = c.getString(1);
            timestamp[i] = c.getString(2);
        }
        c.close();
    }
}