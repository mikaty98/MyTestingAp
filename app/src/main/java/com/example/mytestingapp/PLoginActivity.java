package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PLoginActivity extends AppCompatActivity {

    private EditText email,password;
    private TextView registerbtn,seekerbtn,forgetPassword;
    private Button loginbtn;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_login);

        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);

        forgetPassword = findViewById(R.id.forget_password);
        registerbtn = findViewById(R.id.textView);
        seekerbtn = findViewById(R.id.textView2);
        loginbtn = findViewById(R.id.button);

        registerbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startActivity(new Intent(getApplicationContext(), PRegisterActivity.class));
            }

        });

        seekerbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), SLoginActivity.class));
            }

        });

        forgetPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SLoginActivity.class));
            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ProviderLogin();
            }
        });
    }

    private void ProviderLogin(){

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Providers");
        String inputEmail = email.getText().toString().trim();
        String parts [] = inputEmail.split(".com");
        inputEmail = parts[0];

        String inputPassword = password.getText().toString().trim();

        Query checkUser = reference.orderByChild("email").equalTo(inputEmail+".com");

        String finalInputEmail = inputEmail;
        checkUser.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                if (dataSnapshot.exists()){
                    email.setError(null);

                    String userPassword = dataSnapshot.child(finalInputEmail).child("password").getValue(String.class);
                    if (userPassword.equals(inputPassword)){
                        password.setError(null);
                        Provider p = new Provider();
                        p.setEmail(finalInputEmail+".com");
                        p.setPassword(userPassword);
                        p.setId(dataSnapshot.child(finalInputEmail).child("id").getValue(String.class));
                        p.setUserName(dataSnapshot.child(finalInputEmail).child("userName").getValue(String.class));
                        p.setJobDesc(dataSnapshot.child(finalInputEmail).child("jobDesc").getValue(String.class));
                        p.setGender(dataSnapshot.child(finalInputEmail).child("gender").getValue(String.class));
                        p.setAge(dataSnapshot.child(finalInputEmail).child("age").getValue(String.class));
                        p.setPhoneNumber(dataSnapshot.child(finalInputEmail).child("phoneNumber").getValue(String.class));


                        Intent intent = new Intent(PLoginActivity.this, HomeActivity.class);
                        intent.putExtra("provider",p);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(PLoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(PLoginActivity.this, "User does not exist!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }

        });
    }
}