package com.example.comicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout tilUsername, tilPassword;
    EditText ed_username,ed_password;
    String str_username,str_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ed_username = findViewById(R.id.ed_username);
        ed_password = findViewById(R.id.ed_password);

        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPassword);
    }

    public void Login(View view) {

        if(ed_username.getText().toString().equals("")){
            tilUsername.setError("Enter Username!");

//            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
        }
        else if(ed_password.getText().toString().equals("")){
            tilPassword.setError("Enter Password!");
//            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        }
        else{


//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setMessage("Please Wait..");
//            progressDialog.show();

            userLogin();


//            if(str_username.equals("Huy") && str_password.equals("123")){
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
////            intent.putExtra("username", str_username);
//                startActivity(intent);
//            } else{
//                Toast.makeText(this, "Đi chỗ khác chơi", Toast.LENGTH_SHORT).show();
//            }


        }
    }

    public void moveToRegistration(View view) {
        startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
        finish();
    }

    public void userLogin(){

        str_username = ed_username.getText().toString().trim();
        str_password = ed_password.getText().toString().trim();

        DatabaseReference users = FirebaseDatabase.getInstance().getReference("User");
        Query checkUser = users.orderByChild("username").equalTo(str_username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    tilUsername.setError(null);
                    tilUsername.setEnabled(false);

                    String password = snapshot.child(str_username).child("password").getValue(String.class);
                    if(password.equals(str_password)){
                        tilPassword.setError(null);
                        tilPassword.setEnabled(false);
//                        String name = snapshot.child(str_username).child("name").getValue(String.class);
//                        String username = snapshot.child(str_username).child("username").getValue(String.class);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        intent.putExtra("name", name);
//                        intent.putExtra("username",username);
                        startActivity(intent);
                    }
                    else {
                        tilPassword.setError("Wrong Password");
                        tilPassword.requestFocus();
                    }
                }
                else {
                    tilUsername.setError("No such User exist");
                    tilUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}