package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytestingapp.Classes.Seeker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SLoginActivity extends AppCompatActivity {

    private EditText email,password;
    private TextView registerbtn,providerbtn,forgetPassword;
    private Button loginbtn;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Seekers");

    private FirebaseAuth mauth;

    private String userID;


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mauth.getCurrentUser();
        if (user!=null){
            userID = user.getUid();
            Query checkuser = reference.orderByChild("userID").equalTo(userID);
            checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String email = snapshot.child(userID).child("email").getValue(String.class);
                        Intent intent = new Intent(SLoginActivity.this, SeekerHome0.class);
                        intent.putExtra("seeker email", email);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        //user must log in
                        mauth.signOut();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else{
            //user must log in
            mauth.signOut();
        }

    }

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

        mauth = FirebaseAuth.getInstance();

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

        boolean errorFlag = false;
        String inputEmail = email.getText().toString().trim();
        String inputPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(inputEmail)){
            email.setError("Email Required");
            //email.setBackgroundColor(0xFFFF0000);
            errorFlag = true;
        }
        else if (!inputEmail.contains("@") || !inputEmail.contains(".com")){
            email.setError("Invalid Email");
            //email.setBackgroundColor(0xFFFF0000);
            errorFlag = true;
        }
        if (TextUtils.isEmpty(inputPassword)){
            password.setError("Password Required");
            //password.setBackgroundColor(0xFFFF0000);
            errorFlag = true;
        }

        if (errorFlag){return;}
        else {

            mauth.signInWithEmailAndPassword(inputEmail,inputPassword).addOnCompleteListener(SLoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){ //email and password found
                        userID = task .getResult().getUser().getUid();
                        mauth.updateCurrentUser(task.getResult().getUser());
                        reference = rootNode.getReference().child("Seekers");
                        Query checkuser = reference.orderByChild("userID").equalTo(userID);
                        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){ //email and passowrd found in Seeker database
                                    String text = inputEmail;
                                    Intent intent = new Intent(SLoginActivity.this, SeekerHome0.class);
                                    intent.putExtra("seeker email", text);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(SLoginActivity.this,"User not registered",Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else {
                        Toast.makeText(SLoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }
}