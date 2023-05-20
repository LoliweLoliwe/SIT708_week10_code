package com.loliwe.lfmapapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.StyleSpan;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int Request_code = 100;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    ListView listVw;
    Button backBtn, closeBtn, linesBtn;
    DbManager DB;
    ArrayList<String> listItems;
    ArrayAdapter adapter;
    Polyline line;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        listVw = findViewById(R.id.textView22);
        backBtn = findViewById(R.id.button6);
        closeBtn = findViewById(R.id.button7);
        linesBtn = findViewById(R.id.button8);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        listItems = new ArrayList<>();
        DB = new DbManager(MapActivity.this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MapActivity.this, DisplayActivity.class);
                startActivity(newIntent);
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveLocations();
            }
        });

        linesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });
    }

    private void retrieveLocations() {
        Cursor cursor = DB.viewData();

        if (cursor.getCount() == 0) {
            Toast.makeText(MapActivity.this, "No items to show", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                String pl = cursor.getString(6);
                String [] divide = cursor.getString(7).split("\\:");
                String [] latLng = divide[1].split(",");
                String [] lati = latLng[0].split("\\(");
                String [] lngt = latLng[1].split("\\)");

                listItems.add(pl +"\n"+ Double.parseDouble(lati[1]) +"\n"+ Double.parseDouble(lngt[0]));

                map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lati[1]),Double.parseDouble(lngt[0]))).title(pl));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lati[1]),Double.parseDouble(lngt[0])), 2));
                map.getUiSettings().setZoomControlsEnabled(true);
            }

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
            listVw.setAdapter(adapter);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
    }

    private void getCurrentLocation() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, Request_code);
            return;
        }

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Toast.makeText(getApplicationContext(), "Location result is =" + locationResult, Toast.LENGTH_LONG).show();
                if(locationResult == null) {
                    Toast.makeText(getApplicationContext(), "Current location result is null", Toast.LENGTH_LONG).show();
                    return;
                }

                for(Location location:locationResult.getLocations()) {
                    if(location != null) {
                        Toast.makeText(getApplicationContext(), "Current location result is " + location.getLatitude(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title("Here, you are.."));
                    map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    //map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9));

                    Cursor cursor = DB.viewData();

                    if (cursor.getCount() == 0) {
                        Toast.makeText(MapActivity.this, "No items to show", Toast.LENGTH_LONG).show();
                    } else {
                        while (cursor.moveToNext()) {
                            String pl = cursor.getString(6);
                            String [] divide = cursor.getString(7).split("\\:");
                            String [] latLnge = divide[1].split(",");
                            String [] lati = latLnge[0].split("\\(");
                            String [] lngt = latLnge[1].split("\\)");

                            map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lati[1]),Double.parseDouble(lngt[0]))).title(pl));
                            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lati[1]),Double.parseDouble(lngt[0])), 2));
                            map.getUiSettings().setZoomControlsEnabled(true);

                            line = map.addPolyline(new PolylineOptions()
                                    .add(latLng, new LatLng(Double.parseDouble(lati[1]),Double.parseDouble(lngt[0])))
                                    .addSpan(new StyleSpan(Color.RED)));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (Request_code) {
            case Request_code:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
        }
    }
}