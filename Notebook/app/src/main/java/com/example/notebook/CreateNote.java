package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreateNote extends AppCompatActivity {

    Spinner labelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        labelList = (Spinner) findViewById(R.id.labelList);
        LabelDbHelper dbHelper = new LabelDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(LabelDbHelper.TABLE_NAME, new String[]{LabelDbHelper.LABEL_NAME},
                null, null, null, null, null);

        String[] labels = new String[c.getCount()];

        for(int i = 0; c.moveToNext(); i++)
            labels[i] = c.getString(0);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, labels);
        labelList.setAdapter(adapter);

        c.close();
    }
}