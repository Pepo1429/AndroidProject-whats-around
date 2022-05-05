package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void registration(View view){
        // start registration activity
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
        finish();
    }

    public void login(View view){
        // start login user activity
        Intent intent = new Intent(this, login_user.class);
        startActivity(intent);
        finish();

    }

    public void guest(View view){
        // create anonymous user account
         FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        startActivity(new Intent(Login.this, AddLandmark.class));
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(Login.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }
}





