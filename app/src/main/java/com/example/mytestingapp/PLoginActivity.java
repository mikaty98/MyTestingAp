package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytestingapp.Classes.Provider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class PLoginActivity extends AppCompatActivity {

    private EditText email,password;
    private TextView registerbtn,seekerbtn,forgetPassword;
    private Button loginbtn;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Providers");

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mauth;

    private String userID;
    private SharedPreferences sp;

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
                        Provider provider = snapshot.child(userID).getValue(Provider.class);
                        if ((!provider.isSentProposal()) && (!provider.isGotAccepted())){
                            String email = snapshot.child(userID).child("email").getValue(String.class);
                            Intent intent = new Intent(PLoginActivity.this,ProviderHomeActivity.class);
                            intent.putExtra("provider email", email);
                            startActivity(intent);
                            finish();
                        }
                        else if ((provider.isSentProposal())&&(!provider.isGotAccepted())){
                            Intent intent = new Intent(PLoginActivity.this,ProviderWaitingRoomActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else if ((provider.isSentProposal())&&(provider.isGotAccepted())){
                            Intent intent = new Intent(PLoginActivity.this,ProviderConfirmationActivity.class);
                            intent.putExtra("flag", true);
                            startActivity(intent);
                            finish();
                        }


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
        setContentView(R.layout.activity_p_login);

        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);

        forgetPassword = findViewById(R.id.forget_password);
        registerbtn = findViewById(R.id.textView);
        seekerbtn = findViewById(R.id.textView2);
        loginbtn = findViewById(R.id.button);

        mauth = FirebaseAuth.getInstance();

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
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
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
           mauth.signInWithEmailAndPassword(inputEmail,inputPassword).addOnCompleteListener(PLoginActivity.this, new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if (task.isSuccessful()){ //email and passowrd found
                       userID = task.getResult().getUser().getUid();
                       mauth.updateCurrentUser(task.getResult().getUser());
                       reference = rootNode.getReference().child("Providers");
                       Query checkuser = reference.orderByChild("userID").equalTo(userID);
                       checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               if (snapshot.exists()){ //email and password found in Provider database
                                   String text = inputEmail;
                                   Intent intent = new Intent(PLoginActivity.this,ProviderHomeActivity.class);
                                   intent.putExtra("provider email", text);
                                   startActivity(intent);
                                   finish();
                               }
                               else{
                                   Toast.makeText(PLoginActivity.this,"User not registered",Toast.LENGTH_LONG).show();

                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });
                   }
                   else {
                       Toast.makeText(PLoginActivity.this,"User not registered",Toast.LENGTH_LONG).show();

                   }
               }
           });
        }
    }
}