package com.example.androidproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class nonUser extends AppCompatActivity implements OnMapReadyCallback{

    int atrSize;
    ListView coursesLV;
    ArrayList<attraction> atrList;
    ArrayList<AtrPosition> attractions;
    ArrayAdapter<attraction> atr;
    FirebaseFirestore db;
    GoogleMap myMap;
    FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_user);
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);
        initializeListView();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initializeListView();

    }


    @SuppressLint("CutPasteId")
    private void initializeListView() {
        attractions = new ArrayList<>(); // array to store object of the attractions
        atrList = new ArrayList<>(); // array to store values of location needed for the google markers
        atr = new ArrayAdapter<>(this, R.layout.custom, atrList); // creation of array adapter for displaying all object attraction
        AttractionAdapter placeAdapter = new AttractionAdapter(this, atrList);
        coursesLV = findViewById(R.id.idLVCourses); // initialise listView for attraction
        ListView listView = findViewById(R.id.idLVCourses);
        db = FirebaseFirestore.getInstance(); // initialize Firestore database
        db.collection("Attraction").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) { // get all date from the data base
                    attraction a = document.toObject(attraction.class);
                    placeAdapter.add(a); // place the objects into are for array adapter usage
                    setLocation(placeAdapter);
                }
            }
            public void setLocation(AttractionAdapter placeAdapter) {
                for (int i = 0; i < placeAdapter.getCount(); i++) { // store location info about the attractions
                    attraction atr = placeAdapter.getItem(i);
                    AtrPosition position = new AtrPosition(atr.getName(), atr.getLatitude(), atr.getLongitude());
                    attractions.add(position);
                }
            }

        });
        listView.setAdapter(placeAdapter);// initialize listView
        coursesLV.setOnItemClickListener((parent, view, position, id) -> { // function for selecting exact attraction from arrayadapter
            attraction current = atrList.get(position);
            Intent intent = new Intent(nonUser.this, visit.class);
            intent.putExtra("attraction", current);
            startActivity(intent);
        });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        AtrPosition location;
        LatLng defaultLoc = new LatLng(0,0);
        atrSize = attractions.size();
        for (int i = 0; i < atrSize; i++) { // for loop to extract every attraction possition
            location = attractions.get(i);
            String name = location.getName();
            String lat = location.getLatitude();
            String lon = location.getLongitude();
            double lat1 = Double.parseDouble(lat);
            double lon1 = Double.parseDouble(lon);
            LatLng coordinate = new LatLng(lat1, lon1);
            Objects.requireNonNull(myMap.addMarker(new MarkerOptions() // create markers for each attraction
                    .position(coordinate)
                    .title(name))).showInfoWindow();
            if (i == atrSize - 1) {
                defaultLoc = new LatLng(lat1, lon1); // create  location for camera update
            }
        }
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 4), 1000, null); // set camera to last attraction

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        myMap.setMyLocationEnabled(true);
        Button addLand = findViewById(R.id.button2);
        addLand.setVisibility(View.VISIBLE);

    }


    public void addLandmark(View view) {
        checkState();
    }

    public void signOut(View view) {

        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "You are logged out from the system", Toast.LENGTH_SHORT).show();

    }

    public void checkState() {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(nonUser.this, Login.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(nonUser.this, AddLandmark.class);
                    startActivity(intent);
                }



        }


}
