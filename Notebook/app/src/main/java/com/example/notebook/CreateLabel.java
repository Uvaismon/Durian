package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.*;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLData;

public class CreateLabel extends AppCompatActivity {

    EditText label, passwordEntry, passwordReEntry;
    Button createButton, deleteButton;
    TextView heading;
    Intent intent;
    String prefilledLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_label);
        label = findViewById(R.id.labelEntry);
        passwordEntry = findViewById(R.id.passwordEntry);
        passwordReEntry = findViewById(R.id.passwordReEntry);
        createButton = findViewById(R.id.createUpdateButton);
        heading = findViewById(R.id.heading);
        deleteButton = findViewById(R.id.deleteLabelButton);

        heading.setText(R.string.create_label);
        createButton.setText(R.string.create);


        createButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String enteredLabel = label.getText().toString();
                String enteredPassword = passwordEntry.getText().toString();
                String reEnteredPassword = passwordReEntry.getText().toString();

                if(!enteredPassword.equals(reEnteredPassword)) {
                    Toast.makeText(CreateLabel.this, "Passwords doesn't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                long res = dbInsert(enteredLabel, enteredPassword);
                if (res == -1) {
                    Toast.makeText(CreateLabel.this, "Label already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                go_label_home();
                Log.d("Update return value.", String.valueOf(res));
            }
        });

        intent = getIntent();
        if (intent.hasExtra(DbHelper.LABEL_NAME)) {
            prefilledLabel = intent.getStringExtra(DbHelper.LABEL_NAME);
            preFillData();
            
        deleteButton.setOnClickListener( v -> {
           long res = dbDelete(prefilledLabel);
           if (res < 1) {
               Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
           }
           else
               startActivity(new Intent(this, LabelList.class));
        });
        
        }

    }

    public void go_label_home() {
        startActivity(new Intent(this, LabelList.class));
    }

    public void preFillData() {
        DbHelper labelDbHelper = new DbHelper(this);
        SQLiteDatabase labelDb = labelDbHelper.getReadableDatabase();

        String[] projection = {DbHelper.LABEL_NAME, DbHelper.PASSWORD};

        Cursor c = labelDb.query(DbHelper.LABEL_TABLE_NAME, projection,
                DbHelper.LABEL_NAME + "=?", new String[]{prefilledLabel},
                null, null, null);

        c.moveToFirst();
        label.setText(c.getString(0));
        passwordEntry.setText(c.getString(1));
        passwordEntry.setText(c.getString(1));
        createButton.setText(R.string.update);
        deleteButton.setVisibility(View.VISIBLE);
        c.close();
    }

    private long dbInsert(String label_name, String  password) {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbHelper.LABEL_NAME, label_name);
        values.put(DbHelper.PASSWORD, password);

        if(intent.hasExtra(DbHelper.LABEL_NAME)) {
            return db.update(DbHelper.LABEL_TABLE_NAME, values,
                    DbHelper.LABEL_NAME + "=?", new String[]{prefilledLabel});
        }

        return db.insert(DbHelper.LABEL_TABLE_NAME, null, values);
    }
    
    private long dbDelete(String label_name) {
        DbHelper labelDbHelper = new DbHelper(this);
        SQLiteDatabase labelDb = labelDbHelper.getWritableDatabase();

        return labelDb.delete(DbHelper.LABEL_TABLE_NAME, DbHelper.LABEL_NAME + "=?",
                new String[]{label_name});
    }
}