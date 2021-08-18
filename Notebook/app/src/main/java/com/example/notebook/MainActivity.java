package com.example.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    String[] title, contents, timestamp, label;
    NotesListAdapter notesListAdapter;
    RecyclerView mainRecyclerView;
    HashMap<String, Boolean> passProtected = new HashMap<>();

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

        notesListAdapter = new NotesListAdapter(MainActivity.this, title, contents,
                timestamp, label, this);
        mainRecyclerView.setAdapter(notesListAdapter);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.createNoteMenu:
                startActivity(new Intent(this, CreateNote.class));
                break;
        }
        return super.onOptionsItemSelected(item);
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


        for (int i = 0; labelC.moveToNext(); i++)
            passProtected.put(labelC.getString(0),
                    !labelC.getString(1).equals(""));

        title = new String[c.getCount()];
        contents = new String[c.getCount()];
        timestamp = new String[c.getCount()];
        label = new String[c.getCount()];

        for (int i = 0; c.moveToNext(); i++){
            title[i] = c.getString(0);
            tempContents = c.getString(1);
            timestamp[i] = c.getString(2);
            contents[i] = tempContents.substring(0, Math.min(tempContents.length(), 30)) + " ...";
            label[i] = c.getString(3);
            if(passProtected.get(label[i])) {
                contents[i] = "Password protected note.";
            }
        }
        c.close();
        labelC.close();
    }

    @Override
    public void onClick(String timestamp) {
        Cursor c = notesDb.query(NotesDbHelper.TABLE_NAME, new String[]{LabelDbHelper.LABEL_NAME},
                NotesDbHelper.TIMESTAMP + "=?", new String[]{timestamp},
                null, null, null);
        c.moveToFirst();
        Boolean pass = passProtected.get(c.getString(0));
        c.close();
        if(pass)
            createPasswordDialog(timestamp);
        else
            viewNote(timestamp, "");
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

        cancelButton.setOnClickListener(
                v -> {
                    passwordDialog.dismiss();
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

        if (passProtected.get(c.getString(3))) {
            if (!authenticate(c.getString(3), enteredPassword)) {
                Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG).show();
                c.close();
                return;
            }
            passwordDialog.dismiss();
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