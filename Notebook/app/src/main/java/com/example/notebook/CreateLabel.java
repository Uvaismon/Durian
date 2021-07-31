package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.*;
import android.widget.TextView;

import com.google.android.material.internal.ParcelableSparseArray;

public class CreateLabel extends AppCompatActivity {

    EditText label, passwordEntry, passwordReEntry;
    Button createButton;
    TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_label);
        label = findViewById(R.id.labelEntry);
        passwordEntry = findViewById(R.id.passwordEntry);
        passwordReEntry = findViewById(R.id.passwordReEntry);
        createButton = findViewById(R.id.createButton);
        heading = findViewById(R.id.heading);

        heading.setText(R.string.create_label);
        createButton.setText(R.string.create);


        createButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String enteredLabel = label.getText().toString();
                String enteredPassword = passwordEntry.getText().toString();
                String reEnteredPassword = passwordReEntry.getText().toString();

                Log.d("Label text", enteredLabel);
                Log.d("Password text", enteredPassword);
                Log.d("Re-entered Password", reEnteredPassword);

                long res = dbInsert(enteredLabel, enteredPassword);
                testing();
            }
        });
    }

    private long dbInsert(String label_name, String  password) {
        LabelDbHelper dbHelper = new LabelDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LabelDbHelper.LABEL_NAME, label_name);
        values.put(LabelDbHelper.PASSWORD, password);

        return db.insert(LabelDbHelper.TABLE_NAME, null, values);
    }

    private void testing() {
        LabelDbHelper dbHelper = new LabelDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projections = {LabelDbHelper.LABEL_NAME, LabelDbHelper.PASSWORD};

        Cursor c = db.query(LabelDbHelper.TABLE_NAME, projections, LabelDbHelper.LABEL_NAME + "=?", new String[]{"Todo"},
                null, null, null);
        c.moveToFirst();
        Log.d("Search Result", c.getString(1));
        c.close();
    }
}