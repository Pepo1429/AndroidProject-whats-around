package com.example.androidproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class visit extends AppCompatActivity implements OnMapReadyCallback {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    attraction attr;
    FirebaseFirestore db;
    String nameAtr;
    ListView commentsLV;
    ArrayList<comments> comments;
    ArrayAdapter<comments> comm;
    GoogleMap mMap;
    float cur;
    TextView totalVis;
    TextView visits;
    ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map2, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);
                setValues();

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setValues();
    }

    public void addC(View view) {
        attraction current = attr;
        Intent intent = new Intent(this, addComments.class);
        intent.putExtra("attraction", current);
        startActivity(intent);
    }

    private void setValues() {
        comments = new ArrayList<>();
        Intent intent = getIntent();
        attr = intent.getParcelableExtra("attraction");
        TextView name = findViewById(R.id.textView2);
        TextView createdName = findViewById(R.id.textView5);
        totalVis = findViewById(R.id.textView6);
        visits = findViewById(R.id.textView8);

        TextView createDate = findViewById(R.id.textView7);
        ImageView img = findViewById(R.id.image);
        Button button = findViewById(R.id.button);


        String image = attr.getImgSrc();
        Picasso.get().load(image).into(img);

        name.setText(attr.getName());
        nameAtr = attr.getName();
        createdName.setText(attr.getCreatedBy());
        createDate.setText(attr.getDateOfAdd());
        initializeListView();

        if(user == null){
            button.setVisibility(View.GONE);
        }
        else{
            button.setVisibility(View.VISIBLE);
        }




    }

    @SuppressLint({"CutPasteId", "SetTextI18n"})
    private void initializeListView() {
        final int[] total = {0};
        final int[] visitors = {0};
        List<Float> totalRate = new ArrayList<Float>(); // used to calculate total rate of attraction
        comm = new ArrayAdapter<>(this, R.layout.comments, comments);
        CommentAdapter placeAdapter = new CommentAdapter(this, comments);
        commentsLV = findViewById(R.id.commentView);
        ListView listView = findViewById(R.id.commentView); // create lis view for comments adapter
        db = FirebaseFirestore.getInstance();
        db.collection("Comments").whereEqualTo("name", nameAtr).get().addOnCompleteListener(task -> {
            // get all comments for a given attraction
            comments a;
            for (QueryDocumentSnapshot document : task.getResult()) { // for loop for extracting every comment for a given attraction
                a = document.toObject(comments.class);
                placeAdapter.add(a);
                total[0]++;// count total comments
                if (a.getStar().equals("true")) {
                    visitors[0]++; // count total visitors
                    totalRate.add(Float.parseFloat(a.getRate()));
                }
            }
            displayTotalRate(totalRate);
            totalVis.setText(Integer.toString(total[0]));
            visits.setText(Integer.toString(visitors[0]));
        });




        db = FirebaseFirestore.getInstance();
        db.collection("Attraction").whereEqualTo("name", nameAtr).get().addOnCompleteListener(task -> {
            attraction a = new attraction();
            for (QueryDocumentSnapshot document : task.getResult()) {
                a = document.toObject(attraction.class);
            }
            String sCur = Float.toString(cur);
            a.setRate(sCur);

        });

        listView.setAdapter(placeAdapter);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        String name = attr.getName();
        String lat = attr.getLatitude();
        String lon = attr.getLongitude();
        double lat1 = Double.parseDouble(lat);
        double lon1 = Double.parseDouble(lon);
        LatLng position = new LatLng(lat1, lon1);
        LatLng coordinate = new LatLng(lat1, lon1);
        Objects.requireNonNull(mMap.addMarker(new MarkerOptions()
                .position(coordinate)
                .title(name))).showInfoWindow();


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 13), 1000, null);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            mMap.setMyLocationEnabled(false);

        }
        mMap.setMyLocationEnabled(true);

    }

    public void displayTotalRate(List rate) {
        Float sum = new Float(0);
        for( int i = 0; i<rate.size();i++){
            Float temp = (Float) rate.get(i);
            sum = + sum + temp;
        }
        Float tRate = sum / rate.size();
        RatingBar atrRating = (RatingBar) findViewById(R.id.ratingBar3);
        atrRating.setRating(tRate);
        TextView rateValue = findViewById(R.id.textView11);
        rateValue.setText(Float.toString(tRate));
    }
}