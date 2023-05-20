package com.loliwe.lfmapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class NewAdActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton, Lost, Found;
    Button saveButton, dateButton, chkBtn;
    String type, name, phone, descr, date, area;
    String name1, phone1, descr1, date1, area1;
    String receivedValue, Lyui;
    SharedPreferences sharedPreferences;
    String SHARED_PREFS = "sharedPrefs";
    int sp_type;
    String sp_name = "name", sp_phone = "phone", sp_descr = "descr", sp_date = "date";

    EditText textName, textPhone, textDescr, textDate;
    TextView locationView;
    DbManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ad);

        radioGroup = findViewById(R.id.radioGroup);
        Lost = findViewById(R.id.radio_inch);
        Found = findViewById(R.id.radio_yard);
        textName = (EditText) findViewById(R.id.editTextText3);
        textPhone = (EditText) findViewById(R.id.editTextPhone);
        textDescr = (EditText) findViewById(R.id.editTextTextMultiLine);
        textDate = (EditText) findViewById(R.id.editTextDate);
        locationView = findViewById(R.id.textView8);

        db = new DbManager(NewAdActivity.this);

        Intent valueIntent = getIntent();
        receivedValue = valueIntent.getStringExtra("VALUE_KEY");
        Lyui = valueIntent.getStringExtra("SCORE_KEY");
        locationView.setText(receivedValue);

        loadData();
        updateData();

        dateButton = findViewById(R.id.button3);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == dateButton) {
                    final Calendar d = Calendar.getInstance();
                    int day = d.get(Calendar.DAY_OF_MONTH);
                    int month = d.get(Calendar.MONTH);
                    int year = d.get(Calendar.YEAR);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            NewAdActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String select1 = Integer.toString(dayOfMonth);
                            String select2 = Integer.toString(month);
                            String select3 = Integer.toString(year);

                            textDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                        }
                    }, day, month, year);
                    datePickerDialog.show();
                }
            }
        });
        chkBtn = findViewById(R.id.button1);
        chkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);

                sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                /*if(radioId == R.id.radio_inch) {
                    editor.putBoolean("Lost", radioLBtn.isChecked());
                } else {
                    editor.putBoolean("Found", radioFBtn.isChecked());
                }*/
                editor.putString(sp_name, textName.getText().toString());
                editor.putString(sp_phone, textPhone.getText().toString());
                editor.putString(sp_descr, textDescr.getText().toString());
                editor.putString(sp_date, textDate.getText().toString());
                editor.apply();

                Intent newIntent = new Intent(NewAdActivity.this, LocationActivity.class);
                startActivity(newIntent);
            }
        });

        saveButton = findViewById(R.id.button2);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().clear().commit();
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);

                try {
                    if (radioButton.getText() != null) {
                    //insert();

                        type = radioButton.getText().toString();
                        name = textName.getText().toString();
                        phone = textPhone.getText().toString();
                        descr = textDescr.getText().toString();
                        date = textDate.getText().toString();
                        //area = locationView.getText().toString();
                        Toast.makeText(NewAdActivity.this, "Successfully inserted." + receivedValue, Toast.LENGTH_LONG).show();

                        boolean res = db.addRecord(type, name, phone, descr, date, receivedValue, Lyui);
                        if(res==true) {
                            Toast.makeText(NewAdActivity.this, "Successfully inserted.", Toast.LENGTH_LONG).show();
                            Intent newIntent = new Intent(NewAdActivity.this, MainActivity.class);
                            startActivity(newIntent);
                        } else {
                            Toast.makeText(NewAdActivity.this, "Failed." + date, Toast.LENGTH_LONG).show();
                        }
                    }
                    radioGroup.clearCheck();
                    textName.setText("");
                    textPhone.setText("");
                    textDescr.setText("");
                    textDate.setText("");
                    locationView.setText("");
                    //display.setText("");

                }catch (Exception e) {
                    Toast.makeText(NewAdActivity.this, "Something was not done right" + textDescr.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public  void loadData() {
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        /*radioLBtn.setChecked(sharedPreferences.getBoolean("Lost", false));
        radioFBtn.setChecked(sharedPreferences.getBoolean("Found", false));

        if(sharedPreferences.getBoolean("Lost", false)) {
            Lost.setChecked(true);
        } else {
            Found.setChecked(true);
        }*/
        name1 = sharedPreferences.getString(sp_name, "");
        phone1 = sharedPreferences.getString(sp_phone, "");
        descr1 = sharedPreferences.getString(sp_descr, "");
        date1 = sharedPreferences.getString(sp_date, "");
    }

    public void updateData(){
        textName.setText(name1);
        textPhone.setText(phone1);
        textDescr.setText(descr1);
        textDate.setText(date1);
    }
}