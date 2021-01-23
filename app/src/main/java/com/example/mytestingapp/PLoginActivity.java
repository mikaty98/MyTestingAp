package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class PLoginActivity extends AppCompatActivity {

    private EditText email,password;
    private TextView registerbtn,seekerbtn,forgetPassword;
    private Button loginbtn;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private FirebaseStorage storage;
    private StorageReference storageReference;

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

    private void getProfilePic(Provider provider){
        storageReference = FirebaseStorage.getInstance().getReference().child("image/"+provider.getId()+".jpg");

        try{
            File localfile = File.createTempFile( provider.getId(),".jpg");
            storageReference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            provider.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Bitmap bitmap = BitmapFactory.decodeFile("defaultProfilePic.jpeg");
                    provider.setImageBitmap(bitmap);
                }
            });

        }
        catch (Exception e){
            Bitmap bitmap = BitmapFactory.decodeFile("app/defaultProfilePic.jpeg");
            provider.setImageBitmap(bitmap);
        }


    }

    private void ProviderLogin(){

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Providers");
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


        if (errorFlag){ return;}
        else {
            String parts[] = inputEmail.split(".com");
            inputEmail = parts[0];

            Query checkUser = reference.orderByChild("email").equalTo(inputEmail + ".com");

            String finalInputEmail = inputEmail;
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        email.setError(null);

                        String userPassword = dataSnapshot.child(finalInputEmail).child("password").getValue(String.class);
                        if (userPassword.equals(inputPassword)) {
                            password.setError(null);
                            /*Provider p = new Provider();
                            p.setEmail(finalInputEmail + ".com");
                            p.setPassword(userPassword);
                            p.setId(dataSnapshot.child(finalInputEmail).child("id").getValue(String.class));
                            p.setUserName(dataSnapshot.child(finalInputEmail).child("userName").getValue(String.class));
                            p.setJobDesc(dataSnapshot.child(finalInputEmail).child("jobDesc").getValue(String.class));
                            p.setGender(dataSnapshot.child(finalInputEmail).child("gender").getValue(String.class));
                            p.setAge(dataSnapshot.child(finalInputEmail).child("age").getValue(String.class));
                            p.setPhoneNumber(dataSnapshot.child(finalInputEmail).child("phoneNumber").getValue(String.class));

                            getProfilePic(p);*/

                            Intent intent = new Intent(PLoginActivity.this, ProviderHomeActivity.class);
                            intent.putExtra("Provider email",finalInputEmail);
                            startActivity(intent);
                        } else {
                            //Toast.makeText(PLoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            password.setError("Incorrect Password");
                            //password.setBackgroundColor(0xFFFF0000);
                            password.setText("");
                        }

                    } else {
                        //Toast.makeText(PLoginActivity.this, "User does not exist!", Toast.LENGTH_SHORT).show();
                        email.setError("User does not exist!");
                        //email.setBackgroundColor(0xFFFF0000);
                        password.setText("");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }
    }
}