package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mytestingapp.Classes.ConnectedSandP;
import com.example.mytestingapp.Classes.Provider;
import com.example.mytestingapp.SendNotificationPack.FcmNotificationsSender;
import com.example.mytestingapp.SendNotificationPack.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChosenProviderProfile extends AppCompatActivity {


    private EditText username, jobDescription, gender, birthDate;
    private CircleImageView profilePic;

    private Button acceptButton, backButton,providerReviewBtn;

    private String userType, providerUserID;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Provider provider = new Provider();
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_provider_profile);

        UpdateToken();
        Intent intent = getIntent();
        providerUserID = intent.getStringExtra("userID");

        // SharedPreferences mySharedPreferences = getSharedPreferences("intent", Context.MODE_PRIVATE);
        // int intent2 = mySharedPreferences.getInt("intent", 1);


        //Toast.makeText(ChosenProviderProfile.this,"INTENT DONEEEE"+ "   "+ intent2,Toast.LENGTH_LONG).show();


        int estimatedArrivaltime = intent.getIntExtra("estimatedArrivalTime", 60);
        int estimatedCompletionTime = intent.getIntExtra("estimatedCompletionTime", 60);
        int price = intent.getIntExtra("price", 0);




        username = findViewById(R.id.username);
        jobDescription = findViewById(R.id.job_description);
        gender = findViewById(R.id.gender);
        birthDate = findViewById(R.id.BirthDate2);
        profilePic = findViewById(R.id.profilePic);

        acceptButton = findViewById(R.id.acceptButton);
        backButton = findViewById(R.id.backButton);
        providerReviewBtn = findViewById(R.id.providerReviewBtn);


        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Providers");


        Query checkUser = reference.orderByChild("userID").equalTo(providerUserID);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    provider.setEmail(snapshot.child(providerUserID).child("email").getValue().toString());
                    provider.setId(snapshot.child(providerUserID).child("id").getValue(String.class));
                    provider.setUserName(snapshot.child(providerUserID).child("userName").getValue(String.class));
                    provider.setJobDesc(snapshot.child(providerUserID).child("jobDesc").getValue(String.class));
                    provider.setGender(snapshot.child(providerUserID).child("gender").getValue(String.class));

                    String birthDay = snapshot.child(providerUserID).child("birthDay").getValue(String.class);
                    String birthMonth = snapshot.child(providerUserID).child("birthMonth").getValue(String.class);
                    String birthYear = snapshot.child(providerUserID).child("birthYear").getValue(String.class);

                    provider.setBirthDay(birthDay);
                    provider.setBirthMonth(birthMonth);
                    provider.setBirthYear(birthYear);

                    provider.setPhoneNumber(snapshot.child(providerUserID).child("phoneNumber").getValue(String.class));


                    storageReference = FirebaseStorage.getInstance().getReference().child("images/" + providerUserID);
                    final Bitmap[] bitmap = new Bitmap[1];
                    try {
                        File localfile = File.createTempFile(provider.getId(), ".jpg");
                        storageReference.getFile(localfile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        bitmap[0] = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                        profilePic.setImageBitmap(bitmap[0]);
                                        provider.setImageBitmap(bitmap[0]);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                bitmap[0] = BitmapFactory.decodeFile("defaultProfilePic.jpeg");
                                profilePic.setImageBitmap(bitmap[0]);
                                provider.setImageBitmap(bitmap[0]);
                            }
                        });

                    } catch (Exception e) {
                        bitmap[0] = BitmapFactory.decodeFile("app/defaultProfilePic.jpeg");
                        profilePic.setImageBitmap(bitmap[0]);
                        provider.setImageBitmap(bitmap[0]);
                    }


                    username.setText("USER NAME:   " + provider.getUserName());
                    jobDescription.setText("JOB DESCRIPTION:   " + provider.getJobDesc());
                    gender.setText("GENDER:   " + provider.getGender());


                    birthDate.setText("BIRTH DATE: "+birthDay+"/"+birthMonth+"/"+birthYear);

                    profilePic.setImageBitmap(provider.getImageBitmap());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        providerReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChosenProviderProfile.this, ProviderReviewListActivity.class);
                intent.putExtra("provider id", providerUserID);
                intent.putExtra("provider Name", provider.getUserName());
                startActivity(intent);
            }
        });


        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(ChosenProviderProfile.this);
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.progress_dialog);
                        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    }
                });



                //Sending notification to provider part
                FirebaseDatabase.getInstance().getReference("Tokens").child(providerUserID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String userToken = snapshot.getValue(String.class);


                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(userToken, "Good news!", "Your proposal got accepted!", getApplicationContext(), ChosenProviderProfile.this);
                        notificationsSender.SendNotifications();

                        ConnectedSandP connectedSandP = new ConnectedSandP(FirebaseAuth.getInstance().getUid(), providerUserID, estimatedCompletionTime, "provider", estimatedArrivaltime, price);
                        reference = FirebaseDatabase.getInstance().getReference().child("StartingConnections");
                        reference.child(FirebaseAuth.getInstance().getUid() + providerUserID).setValue(connectedSandP);



                        reference.child(FirebaseAuth.getInstance().getUid() + providerUserID).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                int flag = snapshot.getValue(Integer.class);
                                if (flag == 1)
                                {

                                    Toast.makeText(ChosenProviderProfile.this,"Provider accepted",Toast.LENGTH_LONG).show();

                                    userType = "seeker";

                                    Intent intent = new Intent(ChosenProviderProfile.this, LocalRequestEnd1.class);
                                    intent.putExtra("receiver id", providerUserID);
                                    intent.putExtra("arrival time", estimatedArrivaltime);
                                    intent.putExtra("completion time", estimatedCompletionTime);
                                    intent.putExtra("price", price);
                                    intent.putExtra("user type", userType);
                                   // progressDialog.dismiss();
                                    startActivity(intent);
                                    finish();
                                }
                                else if(flag==2)
                                {

                                    Toast.makeText(ChosenProviderProfile.this,"Provider declined. Please choose another provider to perform your service.",Toast.LENGTH_LONG).show();


                                    Intent failed = new Intent(ChosenProviderProfile.this, SeekerLocalRequestWaitingList.class);
                                    startActivity(failed);
                                    finish();
                                }

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //Notification part ends here
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


    }

    private void UpdateToken() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token userToken = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid()).setValue(userToken);
    }


}