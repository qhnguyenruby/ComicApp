package com.example.comicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.comicapp.Model.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity {
    TextInputLayout tilName,tilUsername,tilPassword,tilComfirmPassword;
    EditText ed_name,ed_username,ed_password,ed_cfpassword;
    String str_name,str_username,str_password;

    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        users = FirebaseDatabase.getInstance().getReference("User");

        ed_name = findViewById(R.id.ed_name);
        ed_username = findViewById(R.id.ed_username);
        ed_password = findViewById(R.id.ed_password);
        ed_cfpassword = findViewById(R.id.ed_cfpassword);

        tilName = findViewById(R.id.til_name);
        tilUsername = findViewById(R.id.til_username);
        tilPassword = findViewById(R.id.til_password);
        tilComfirmPassword = findViewById(R.id.til_cfpassword);
    }

    public void moveToLogin(View view) {

        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }

    public void Register(View view) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");


        if(ed_name.getText().toString().equals("")){
//            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
            tilName.setError("Enter Name");
        }
        else if(ed_username.getText().toString().equals("")){
//            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
            tilUsername.setError("Enter Username");
        }
        else if(ed_password.getText().toString().equals("")){
//            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            tilPassword.setError("Enter Password");
        }
        else if(ed_cfpassword.getText().toString().equals("")){
//            Toast.makeText(this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
            tilComfirmPassword.setError("Enter Confirm Password");
        }
        else if(!ed_cfpassword.getText().toString().equals(ed_password.getText().toString())){
//            Toast.makeText(this, "Confirm Password must be matched with password", Toast.LENGTH_SHORT).show();
            tilComfirmPassword.setError("Confirm Password must be matched with password");
        }
        else{

            progressDialog.show();
            str_name = ed_name.getText().toString().trim();
            str_username = ed_username.getText().toString().trim();
            str_password = ed_password.getText().toString().trim();

            try {
                progressDialog.dismiss();

                users.child(str_username).setValue(new User(str_name,str_username,str_password));
                finish();
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }catch (Exception e){
                progressDialog.dismiss();
                Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
            }





        }

    }
}