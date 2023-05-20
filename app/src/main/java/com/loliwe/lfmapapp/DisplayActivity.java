package com.loliwe.lfmapapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {
    Button btn1, btn2;
    TextView textVw;
    Toolbar toolbar;
    ListView listView;
    ArrayList<String> listItems;
    ArrayAdapter adapter;
    DbManager obj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        obj = new DbManager(DisplayActivity.this);

        listItems = new ArrayList<>();

        listView = findViewById(R.id.listvw2);
        toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        viewData();

        btn1 = findViewById(R.id.btn_back);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DisplayActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn2 = findViewById(R.id.btn_geo);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                String text = listView.getItemAtPosition(i).toString();

                Bundle bundle = new Bundle();
                bundle.putString("TYPE_KEY", text);
                Intent newIntent = new Intent(DisplayActivity.this, LastActivity.class);
                newIntent.putExtras(bundle);
                startActivity(newIntent);
            }
        });
    }
    private void viewData() {
        Cursor cursor = obj.viewData();

        if (cursor.getCount() == 0) {
            Toast.makeText(DisplayActivity.this, "No items to show", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                listItems.add("[" +cursor.getString(0)+ "] " + cursor.getString(1) + " : " + cursor.getString(4) + " On " + cursor.getString(5) + ". Contact: " + cursor.getString(2) + " @ " + cursor.getString(3) + ". GPS " + cursor.getString(7));
            }

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.t_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setQueryHint("Type here to search....");
        //searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //    Toast.makeText(SearchActivity.this, "Not found", Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}