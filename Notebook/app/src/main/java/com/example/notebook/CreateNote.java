package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.sql.Timestamp;
import java.util.ArrayList;

public class CreateNote extends AppCompatActivity {

    Spinner labelList;
    Button saveButton;
    EditText titleEntry, contentsEntry;

    NotesDbHelper notesDbHelper;
    SQLiteDatabase notesDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        notesDbHelper = new NotesDbHelper(this);
        notesDb = notesDbHelper.getWritableDatabase();

        labelList = findViewById(R.id.labelList);
        saveButton = findViewById(R.id.saveButton);
        titleEntry = findViewById(R.id.titleEntry);
        contentsEntry = findViewById(R.id.contentsEntry);

        LabelDbHelper labelDbHelper = new LabelDbHelper(this);
        SQLiteDatabase labelDb = labelDbHelper.getReadableDatabase();

        Cursor c = labelDb.query(LabelDbHelper.TABLE_NAME, new String[]{LabelDbHelper.LABEL_NAME},
                null, null, null, null, null);

        ArrayList<String> labels = new ArrayList<>();

        for(int i = 0; c.moveToNext(); i++)
            labels.add(c.getString(0));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, labels);
        labelList.setAdapter(adapter);

        try {

            Intent editIntent = getIntent();
            String timestamp = editIntent.getData().toString();
            String[] notesProjection = new String[]{NotesDbHelper.TITLE, LabelDbHelper.LABEL_NAME,
                    NotesDbHelper.CONTENTS};

            Cursor notesC = notesDb.query(NotesDbHelper.TABLE_NAME, notesProjection,
                    NotesDbHelper.TIMESTAMP + "=?", new String[]{timestamp},null,
                    null, null);

            saveButton.setOnClickListener(
                    v -> {
                        String enteredTitle = titleEntry.getText().toString();
                        String enteredContents = contentsEntry.getText().toString();
                        String selectedLabel = labelList.getSelectedItem().toString();

                        long res = dbUpdate(enteredTitle, selectedLabel, enteredContents, timestamp);
                        go_home();
                    }
            );

            notesC.moveToFirst();
            editFill(notesC.getString(0), notesC.getString(1),
                    notesC.getString(2));
            labelList.setSelection(labels.indexOf(notesC.getString(1)));
            notesC.close();
        }
        catch (Exception e){
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String enteredTitle = titleEntry.getText().toString();
                    String enteredContents = contentsEntry.getText().toString();
                    String selectedLabel = labelList.getSelectedItem().toString();

                    long res = dbInsert(enteredTitle, selectedLabel, enteredContents);
                    go_home();
                }
            });
        }
        c.close();
    }

    private long dbInsert(String title, String label, String contents) {
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

    private long dbUpdate(String title, String label, String contents, String timestamp) {
        ContentValues values = new ContentValues();
        values.put(NotesDbHelper.TITLE, title);
        values.put(NotesDbHelper.CONTENTS, contents);
        values.put(LabelDbHelper.LABEL_NAME, label);
        values.put(NotesDbHelper.TIMESTAMP, new Timestamp(System.currentTimeMillis()).toString());

        return notesDb.update(NotesDbHelper.TABLE_NAME, values,
                NotesDbHelper.TIMESTAMP + "=?", new String[]{timestamp});
    }

    private void editFill(String title, String label, String content) {
        Log.d("title", title);
        titleEntry.setText(title);
        contentsEntry.setText(content);
    }

    private void go_home() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void deleteNote(String timestamp) {
        notesDbHelper = new NotesDbHelper(this);
        notesDb = notesDbHelper.getWritableDatabase();

        notesDb.delete(NotesDbHelper.TABLE_NAME,
                NotesDbHelper.TIMESTAMP + "=?",
                new String[]{timestamp});
        startActivity(new Intent(this, MainActivity.class));
    }
}