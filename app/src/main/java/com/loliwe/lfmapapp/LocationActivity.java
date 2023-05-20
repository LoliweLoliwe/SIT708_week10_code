package com.loliwe.lfmapapp;

import static com.google.android.libraries.places.api.model.Place.Field.NAME;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {
    EditText editPlace;
    private double lat, lng;
    GoogleMap map;
    TextView display;
    private PlacesClient placesClient;
    private Location lastKnownLocation;
    private LocationCallback locationCallback;
    private static final int Request_code = 100;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        editPlace = findViewById(R.id.editText4);
        display = findViewById(R.id.textView17);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Places.initialize(getApplicationContext(), "MAPS_API_KEY");
        placesClient = Places.createClient(this);
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        editPlace.setFocusable(false);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        editPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize p[aces field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG, NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(LocationActivity.this);
                startActivityForResult(intent, 100);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            editPlace.setText(place.getAddress());
            display.setText(String.valueOf(place.getLatLng()));
            String plae = place.getName();
            LatLng latLngOfPlace = place.getLatLng();
            String nuh = String.valueOf(place.getLatLng());
            Toast.makeText(LocationActivity.this, "location.." + plae, Toast.LENGTH_LONG).show();

            Bundle bundle = new Bundle();
            bundle.putString("VALUE_KEY", plae);
            bundle.putString("SCORE_KEY", nuh);
            Intent newIntent = new Intent(LocationActivity.this, NewAdActivity.class);
            newIntent.putExtras(bundle);
            startActivity(newIntent);

        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        getCurrentLocation();
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
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        //editPlace.setText(latLng.toString());
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

                    map.addMarker(new MarkerOptions().position(latLng).title("current location"));
                    map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9));
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