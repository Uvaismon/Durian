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

public class LabelList extends AppCompatActivity implements LabelListAdapter.LabelClickListener {

    String[] label;
    DbHelper labelDbHelper;
    SQLiteDatabase labelDb;
    String[] labelName;
    RecyclerView recyclerView;
    LabelListAdapter labelListAdapter;

    AlertDialog.Builder passwordDialogBuilder;
    AlertDialog passwordDialog;
    EditText enterPassword;
    Button openButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_list);
        labelDbHelper = new DbHelper(this);
        recyclerView = findViewById(R.id.labelRecycler);
        labelDbHelper = new DbHelper(this);
        labelDb = labelDbHelper.getReadableDatabase();

        loadDb();

        labelListAdapter = new LabelListAdapter(LabelList.this, labelName,
                this);
        recyclerView.setAdapter(labelListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(LabelList.this));
    }

    private void loadDb() {

        Cursor c = labelDb.query(DbHelper.LABEL_TABLE_NAME, new String[] {DbHelper.LABEL_NAME},
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

        if(element.equals("General"))
            return;
        
        if(element.equals("Add label")) {
            startActivity(new Intent(this, CreateLabel.class));
            return;
        }

        Cursor c = labelDb.query(DbHelper.LABEL_TABLE_NAME, new String[]{DbHelper.PASSWORD},
                DbHelper.LABEL_NAME + "=?", new String[]{element}, 
                null, null, null);
        
        c.moveToFirst();
        
        if(!c.getString(0).equals("")) {
            createPasswordDialog(element, c.getString(0));
            c.close();
            return;
        }
        c.close();

        intent.putExtra(DbHelper.LABEL_NAME, element);
        startActivity(intent);

    }

    public void createPasswordDialog(String element, String password) {
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
                    if(password.equals(enteredPassword)) {
                        Intent intent = new Intent(LabelList.this, CreateLabel.class);
                        intent.putExtra(DbHelper.LABEL_NAME, element);
                        passwordDialog.dismiss();
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        cancelButton.setOnClickListener(
                v -> {
                    passwordDialog.dismiss();
                }
        );
    }
    
}