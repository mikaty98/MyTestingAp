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

import com.example.mytestingapp.Classes.Seeker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SLoginActivity extends AppCompatActivity {

    public static final String TEXT = "com.example.mytestingapp";
    private EditText email,password;
    private TextView registerbtn,providerbtn,forgetPassword;
    private Button loginbtn;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_login);

        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);

        forgetPassword = findViewById(R.id.forget_password);
        registerbtn = findViewById(R.id.textView);
        providerbtn = findViewById(R.id.textView2);
        loginbtn = findViewById(R.id.button);

        registerbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startActivity(new Intent(getApplicationContext(), SRegisterActivity.class));
                finish();
            }

        });

        providerbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), PLoginActivity.class));
                finish();
            }

        });

        forgetPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), PLoginActivity.class));
                finish();
            }

        });

        loginbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SeekerLogin();

            }
        });




    }
    private void SeekerLogin(){
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Seekers");
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
                        Seeker s = new Seeker();
                        s.setEmail(finalInputEmail+".com");
                        s.setPassword(userPassword);
                        s.setId(dataSnapshot.child(finalInputEmail).child("id").getValue(String.class));
                        s.setUserName(dataSnapshot.child(finalInputEmail).child("userName").getValue(String.class));
                        s.setAge(dataSnapshot.child(finalInputEmail).child("age").getValue(String.class));
                        s.setGender(dataSnapshot.child(finalInputEmail).child("gender").getValue(String.class));
                        s.setPhoneNumber(dataSnapshot.child(finalInputEmail).child("phoneNumber").getValue(String.class));


                        EditText editText = (EditText) findViewById(R.id.editTextTextEmailAddress);
                        String text = editText.getText().toString();

                        Intent intent = new Intent(SLoginActivity.this, SeekerHome.class);
                        intent.putExtra("seeker email",text);
                        startActivity(intent);
                        finish();



                    }
                    else{
                        Toast.makeText(SLoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(SLoginActivity.this, "User does not exist!", Toast.LENGTH_SHORT).show();
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }

        });

    }
}