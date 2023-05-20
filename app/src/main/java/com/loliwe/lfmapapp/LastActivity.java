package com.loliwe.lfmapapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class LastActivity extends AppCompatActivity {

    TextView txtV;
    EditText ED, ED1, ED2;
    Button updatebtn, btn2;
    FloatingActionButton floatingAcBtn;
    ArrayList<String> listItems;
    DbManager obj = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);

        Intent xIntent = getIntent();
        Bundle bundle = xIntent.getExtras();
        String search = bundle.getString("TYPE_KEY");

        obj = new DbManager(LastActivity.this);
        listItems = new ArrayList<>();

        ED = findViewById(R.id.NumberDecimal);
        ED1 = findViewById(R.id.editTextText31);
        ED2 = findViewById(R.id.editTextTextMultiLine31);

        txtV = findViewById(R.id.Txtview13);
        txtV.setText(search);
        floatingAcBtn = (FloatingActionButton) findViewById(R.id.floatingBtn);

        updatebtn = findViewById(R.id.btn_update);
        btn2 = findViewById(R.id.btn_delete);
        FragmentManager guess = getSupportFragmentManager();

        floatingAcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cursor cursor = obj.updateItem(String descr, String person);
                AlertDialog.Builder resetAlert = new AlertDialog.Builder(LastActivity.this);
                resetAlert.setTitle("DELETION OF A POST");
                resetAlert.setMessage("Have you provided the exact ID of the post you are deleting before your proceed?");
                resetAlert.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int deleteRows = obj.deleteData(ED.getText().toString());
                        if(deleteRows > 0) {
                            //txtV.setText(cursor.getString(cursor.getColumnIndex("type")));
                            Toast.makeText(LastActivity.this, "Successfully deleted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LastActivity.this, "Failed to delete", Toast.LENGTH_LONG).show();
                        }
                        Intent newIntent = new Intent(LastActivity.this, DisplayActivity.class);
                        startActivity(newIntent);
                    }
                });
                resetAlert.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(LastActivity.this, "OKAY!!", Toast.LENGTH_LONG).show();
                    }
                });
                resetAlert.setCancelable(false);
                resetAlert.show();
            }
        });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @SuppressLint("Range")
    private void updateData() {
        String ppk = ED.getText().toString().trim();
        String ste = ED1.getText().toString().trim();
        String gtr = ED2.getText().toString().trim();

        obj.updateData(ppk, ste, gtr);

        /*if(cursor == true) {
            //txtV.setText(cursor.getString(cursor.getColumnIndex("type")));
            Intent newIntent = new Intent(LastActivity.this, DisplayActivity.class);
            startActivity(newIntent);
            Toast.makeText(LastActivity.this, "Successfully updated", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(LastActivity.this, "Update failed", Toast.LENGTH_LONG).show();
        }*/
        Intent newIntent = new Intent(LastActivity.this, DisplayActivity.class);
        startActivity(newIntent);
    }
}