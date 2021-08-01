package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.sql.Timestamp;

public class CreateNote extends AppCompatActivity {

    Spinner labelList;
    Button saveButton;
    EditText titleEntry, contentsEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        labelList = findViewById(R.id.labelList);
        saveButton = findViewById(R.id.saveButton);
        titleEntry = findViewById(R.id.titleEntry);
        contentsEntry = findViewById(R.id.contentsEntry);

        LabelDbHelper labelDbHelper = new LabelDbHelper(this);
        SQLiteDatabase labelDb = labelDbHelper.getReadableDatabase();

        Cursor c = labelDb.query(LabelDbHelper.TABLE_NAME, new String[]{LabelDbHelper.LABEL_NAME},
                null, null, null, null, null);

        String[] labels = new String[c.getCount()];

        for(int i = 0; c.moveToNext(); i++)
            labels[i] = c.getString(0);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, labels);
        labelList.setAdapter(adapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredTitle = titleEntry.getText().toString();
                String enteredContents = contentsEntry.getText().toString();
                String selectedLabel = labelList.getSelectedItem().toString();

                long res = dbInsert(enteredTitle, selectedLabel, enteredContents);
            }
        });

        c.close();
    }

    private long dbInsert(String title, String label, String contents) {
        NotesDbHelper notesDbHelper = new NotesDbHelper(this);
        SQLiteDatabase notesDb = notesDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NotesDbHelper.TITLE, title);
        values.put(NotesDbHelper.CONTENTS, contents);
        values.put(LabelDbHelper.LABEL_NAME, label);
        values.put(NotesDbHelper.TIMESTAMP, new Timestamp(System.currentTimeMillis()).toString());

        Log.d("Title", values.getAsString(NotesDbHelper.TITLE));
        Log.d("Contents", values.getAsString(NotesDbHelper.CONTENTS));
        Log.d("Label name", values.getAsString(LabelDbHelper.LABEL_NAME));
        Log.d("Timestamp", values.getAsString(NotesDbHelper.TIMESTAMP));

        return notesDb.insert(NotesDbHelper.TABLE_NAME, null, values);
    }
}