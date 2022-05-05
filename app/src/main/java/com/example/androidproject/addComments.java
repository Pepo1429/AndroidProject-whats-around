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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class addComments extends AppCompatActivity {
    attraction attr = new attraction();
    Uri selectedImage;
    String imagePath;
    FusedLocationProviderClient mFusedLocationClient;
    String star;
    RatingBar ratingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.activity_add_comments);
        ratingBar = findViewById(R.id.ratingBar4);
        Intent intent = getIntent();
        attr = intent.getParcelableExtra("attraction");
        checkPerm();
        getLastLocation();
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
               String sLat = String.valueOf(latitude);
                String sLon = String.valueOf(longitude);
                checkIfThere(sLat, sLon);

            }
        });
    }


    public void checkIfThere(String uLat, String uLon){
        String aLat = attr.getLatitude();
        String aLon = attr.getLongitude();
        // get current user position and compared with the attraction position
        if((aLat.substring(0, 6).equals(uLat.substring(0, 6)))&&(aLon.substring(0, 6).equals(uLon.substring(0, 6)))){
             star = "true";

        }
        else{
            star = "false";
            ratingBar.setVisibility(View.GONE);
        }
    }

    public void checkPerm() {
      //check if user has been allowed storage access
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA,READ_EXTERNAL_STORAGE}, 5);
        }
    }

    public void commImg(View view){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 10);
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
            case 10:
                if(resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData();
                    ImageView img = findViewById(R.id.image3);
                    img.setImageURI(selectedImage);
                    uploadImg();
                }
                break;
        }
    }
    public void uploadImg(){
        FirebaseStorage storage;
        StorageReference storageReference;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if (selectedImage != null) {
            //check if img is selected
            StorageReference ref = storageReference.child("comments/" + UUID.randomUUID().toString());
            //insert image to co,ments folder in firebase storage

            ref.putFile(selectedImage).addOnSuccessListener(taskSnapshot -> {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(this::imgUrl);
                Toast.makeText(addComments.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
            });
        }

    }

    public void imgUrl(Uri uri){
        this.imagePath = uri.toString();
    }

    public void uploadComment(View view){
        float tempRate = this.ratingBar.getRating();
        String email;
        String barV = String.valueOf(tempRate);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.isAnonymous()) {
            email = "Anonymous user";
        }
        else {
            email = user.getEmail();
        }
        EditText comment = findViewById(R.id.editTextTextMultiLine);
        String sComm = comment.getText().toString();

        if (sComm.matches("")) {
            Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show();
        }
        else{
            String name = attr.getName();
            comments newComment = new comments(imagePath,sComm,name,email,barV,star);
            addCommendToDb(newComment);        }
    }

    public void addCommendToDb(comments a){
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        db.collection("Comments").add(a);
        reloadCom();

    }

    public void reloadCom(){
        finish();
    }

}