package com.example.androidproject;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AddLandmark extends AppCompatActivity {
    FusedLocationProviderClient mFusedLocationClient;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String sLat, sLon;
    String name;
    Uri selectedImage;
    FirebaseStorage storage;
    StorageReference storageReference;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_landmark);
        checkPerm();
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            sLat=String.valueOf(latitude);
            sLon=String.valueOf(longitude);
        });
    }
    public void uploadImg(){
        // function for image insertion ito the data base
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if (selectedImage != null) {
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

            ref.putFile(selectedImage).addOnSuccessListener(taskSnapshot -> {

                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(this::setUrl);
                        Toast.makeText(AddLandmark.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
            });
        }

    }

    public void setUrl(Uri uri){
        this.imagePath = uri.toString();
    }





    public void checkPerm() {

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA,READ_EXTERNAL_STORAGE}, 5);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You cannot add image place check yor permissions", Toast.LENGTH_SHORT).show();
                Button gallery = findViewById(R.id.button9);
                gallery.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void galleryImg(View view){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = imageReturnedIntent.getExtras();
                    Bitmap photo = (Bitmap) bundle.get("data");
                    ImageView img = findViewById(R.id.imageAdd);
                    img.setImageBitmap(photo);

                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData();
                    ImageView img = findViewById(R.id.imageAdd);
                    img.setImageURI(selectedImage);
                    uploadImg();

                }
                break;
        }
    }

    public void upload(View view){
        EditText text = findViewById(R.id.editTextTextPersonName3);
        name = text.getText().toString();
        if (name.matches("")) {
            Toast.makeText(this, "You did not enter a Attraction name", Toast.LENGTH_SHORT).show();
        }
        else{
            castAtr();
        }
    }
    public void castAtr(){
        // Here the user is being checked if its authorized like anonymous or regular user
        Date currentTime = Calendar.getInstance().getTime(); // get current time
        String date = DateFormat.format("yyyy/MM/dd", currentTime).toString();
        String email; // get user details
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.isAnonymous()) {
            email = "Anonymous user";
        }
        else {
            email = user.getEmail();
        }
        String rate = "0";
        String total = "0";
        attraction atr = new attraction(name,sLat,sLon,rate,total,imagePath,email,date);
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        db.collection("Attraction").add(atr);
        reloadAtr();
    }
    public void reloadAtr(){
        finish();
    }
    @Override
    public void onBackPressed() {
        finish();
    }

}
