package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        final Button button = findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // get user inputs
                EditText user = findViewById(R.id.editTextTextPersonName);
                String userValue = user.getText().toString();
                EditText password = findViewById(R.id.editTextTextPassword);
                String passValue = password.getText().toString();
                EditText conf = findViewById(R.id.editTextTextPassword2);
                String confPass = conf.getText().toString();
              boolean checkPass =  checkPassword(passValue, confPass);
                if(checkPass){
                    insertDB(userValue, passValue);
                }
            }

            public boolean checkPassword(String pass, String conf) { // validator for password matching
                if (!pass.equals(conf)) {
                    Toast.makeText(getApplicationContext(), "Passwords does not match. Please try again.", Toast.LENGTH_SHORT)
                            .show();
                    return false;
                }
                else return true;
            }

            public void insertDB(String userValue, String passValue){ // authorize user
                auth.createUserWithEmailAndPassword(userValue, passValue)
                        .addOnCompleteListener(Registration.this, task -> {
                            Toast.makeText(Registration.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            if (!task.isSuccessful()) {
                                Toast.makeText(Registration.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            else {
                                startActivity(new Intent(Registration.this, AddLandmark.class));
                                finish();
                            }
                        });

            }


        });
    }
}