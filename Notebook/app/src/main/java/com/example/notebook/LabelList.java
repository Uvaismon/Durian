package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class LabelList extends AppCompatActivity implements LabelListAdapter.LabelClickListener {

    String[] label;
    LabelDbHelper labelDbHelper;
    SQLiteDatabase labelDb;
    String[] labelName;
    RecyclerView recyclerView;
    LabelListAdapter labelListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_list);
        labelDbHelper = new LabelDbHelper(this);
        recyclerView = findViewById(R.id.labelRecycler);
        labelDbHelper = new LabelDbHelper(this);
        labelDb = labelDbHelper.getReadableDatabase();

        loadDb();

        labelListAdapter = new LabelListAdapter(LabelList.this, labelName,
                this);
        recyclerView.setAdapter(labelListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(LabelList.this));
    }

    private void loadDb() {

        Cursor c = labelDb.query(LabelDbHelper.TABLE_NAME, new String[] {LabelDbHelper.LABEL_NAME},
                null, null, null, null, null);

        labelName = new String[c.getCount() + 1];


        for(int i = 0; c.moveToNext(); i++)
            labelName[i] = c.getString(0);

        labelName[c.getCount()] = "Add label";

        c.close();
    }

    @Override
    public void onClick(String element) {

        Intent intent = new Intent(this, CreateLabel.class);

        if (!element.equals("Add label")) {
            intent.putExtra(LabelDbHelper.LABEL_NAME, element);
        }
        startActivity(intent);

    }
}