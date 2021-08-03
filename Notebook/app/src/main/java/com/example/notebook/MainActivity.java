package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NotesListAdapter.NoteClickListener {

    NotesDbHelper notesDbHelper;
    SQLiteDatabase notesDb;
    LabelDbHelper labelDbHelper;
    SQLiteDatabase labelDb;
    String[] title, contents, timestamp;
    NotesListAdapter notesListAdapter;
    RecyclerView mainRecyclerView;

    AlertDialog.Builder passwordDialogBuilder;
    AlertDialog passwordDialog;
    EditText enterPassword;
    Button openButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainRecyclerView = findViewById(R.id.mainRecyclerView);


        notesDbHelper = new NotesDbHelper(this);
        notesDb = notesDbHelper.getReadableDatabase();
        labelDbHelper = new LabelDbHelper(this);
        labelDb = labelDbHelper.getReadableDatabase();

        loadNotesList();

        notesListAdapter = new NotesListAdapter(MainActivity.this, title, contents
                , timestamp, this);
        mainRecyclerView.setAdapter(notesListAdapter);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }

    private void loadNotesList() {
        String[] projections = {NotesDbHelper.TITLE, NotesDbHelper.CONTENTS,
                NotesDbHelper.TIMESTAMP, LabelDbHelper.LABEL_NAME};
        String[] labelProjection = {LabelDbHelper.LABEL_NAME, LabelDbHelper.PASSWORD};

        String tempContents;

        Cursor c = notesDb.query(NotesDbHelper.TABLE_NAME, projections, null,
                null, null ,null, NotesDbHelper.TIMESTAMP);

        Cursor labelC = labelDb.query(LabelDbHelper.TABLE_NAME, labelProjection, null,
                null, null, null,null);

        HashMap<String, Boolean> passProtected = new HashMap<>();

        for (int i = 0; labelC.moveToNext(); i++)
            passProtected.put(labelC.getString(0),
                    !labelC.getString(1).equals(""));

        title = new String[c.getCount()];
        contents = new String[c.getCount()];
        timestamp = new String[c.getCount()];

        for (int i = 0; c.moveToNext(); i++){
            title[i] = c.getString(0);
            tempContents = c.getString(1);
            timestamp[i] = c.getString(2);
            contents[i] = tempContents.substring(0, Math.min(tempContents.length(), 30)) + " ...";
//            if(passProtected.get(c.getString(3))) {
//                contents[i] = "Password protected note.";
//            }
        }
        c.close();
        labelC.close();
    }

    @Override
    public void onClick(String timestamp) {
        createPasswordDialog(timestamp);
    }

    public void createPasswordDialog(String timestamp) {
        passwordDialogBuilder = new AlertDialog.Builder(this);
        final View passwordPop = getLayoutInflater().inflate(R.layout.password_popup, null);

        passwordDialogBuilder.setView(passwordPop);
        passwordDialog = passwordDialogBuilder.create();
        passwordDialog.show();

        openButton = passwordPop.findViewById(R.id.openButton);
        cancelButton = passwordPop.findViewById(R.id.cancelButton);

        openButton.setOnClickListener(
                v -> {
                    enterPassword = passwordPop.findViewById(R.id.enterPassword);
                    String enteredPassword = enterPassword.getText().toString();
                    viewNote(timestamp, enteredPassword);
                }
        );
    }

    public void viewNote(String timestamp, String enteredPassword) {

        Intent noteView = new Intent(this, ViewNote.class);
        Bundle extras = new Bundle();
        String[] projections = {NotesDbHelper.TITLE, NotesDbHelper.CONTENTS,
                NotesDbHelper.TIMESTAMP, LabelDbHelper.LABEL_NAME};

        Cursor c = notesDb.query(NotesDbHelper.TABLE_NAME, projections,
                NotesDbHelper.TIMESTAMP + "=?", new String[]{timestamp},
                null, null, null);

        c.moveToFirst();

        if(!authenticate(c.getString(3), enteredPassword)) {
            Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG).show();
            c.close();
            return;
        }

        extras.putString(NotesDbHelper.TITLE, c.getString(0));
        extras.putString(NotesDbHelper.CONTENTS, c.getString(1));
        extras.putString(NotesDbHelper.TIMESTAMP, c.getString(2));
        extras.putString(LabelDbHelper.LABEL_NAME, c.getString(3));
        noteView.putExtras(extras);
        startActivity(noteView);
        c.close();
    }

    public Boolean authenticate(String label, String enteredPassword) {
        String[] projections = new String[]{LabelDbHelper.PASSWORD};

        Cursor c = labelDb.query(LabelDbHelper.TABLE_NAME, projections,
                LabelDbHelper.LABEL_NAME + "=?", new String[]{label}, null,
                null, null);
        c.moveToFirst();
        Boolean res = c.getString(0).equals(enteredPassword);
        c.close();
        return res;
    }
}