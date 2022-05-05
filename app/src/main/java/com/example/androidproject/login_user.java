package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class login_user extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        final Button button = findViewById(R.id.button7);
        button.setOnClickListener(v -> {
            // get user input on button click
            EditText user = findViewById(R.id.editTextTextPersonName2);
            String userValue = user.getText().toString();
            EditText password = findViewById(R.id.editTextTextPassword3);
            String passwordValue = password.getText().toString();
            loginCheck(userValue, passwordValue);


        });
    }

    public void loginCheck(String userValue, String passValue){
        // authorize user by firebase authorization
        auth.signInWithEmailAndPassword(userValue, passValue)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(login_user.this, "Something went wrong try again." + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        startActivity(new Intent(login_user.this, AddLandmark.class));
                        finish();
                    }
                });

    }


}