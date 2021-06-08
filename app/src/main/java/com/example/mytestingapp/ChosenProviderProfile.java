package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytestingapp.Classes.Provider;
import com.example.mytestingapp.SendNotificationPack.APIService;
import com.example.mytestingapp.SendNotificationPack.Client;
import com.example.mytestingapp.SendNotificationPack.Data;
import com.example.mytestingapp.SendNotificationPack.MyResponse;
import com.example.mytestingapp.SendNotificationPack.NotificationSender;
import com.example.mytestingapp.SendNotificationPack.Token;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChosenProviderProfile extends AppCompatActivity {

    private EditText username,jobDescription,gender,age,id,email,phoneNumber;
    private CircleImageView profilePic;

    private Button acceptButton, backButton;

    private String userType;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Provider provider = new Provider();

    private APIService apiService;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_provider_profile);

        Intent intent = getIntent();
        String providerEmail = intent.getStringExtra("provider email");
        String seekerEmail = intent.getStringExtra("seeker email");
        String providerUserID = intent.getStringExtra("userID");

       // SharedPreferences mySharedPreferences = getSharedPreferences("intent", Context.MODE_PRIVATE);
       // int intent2 = mySharedPreferences.getInt("intent", 1);


        //Toast.makeText(ChosenProviderProfile.this,"INTENT DONEEEE"+ "   "+ intent2,Toast.LENGTH_LONG).show();




        int estimatedArrivaltime = intent.getIntExtra("estimatedArrivalTime", 60);
        int estimatedCompletionTime = intent.getIntExtra("estimatedCompletionTime", 60);
        int price = intent.getIntExtra("price", 0);



        String parts[] = providerEmail.split(".com");
        String trimmedEmail = parts[0];

        username = findViewById(R.id.username);
        jobDescription = findViewById(R.id.job_description);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        id = findViewById(R.id.id);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phone_number);
        profilePic = findViewById(R.id.profilePic);

        acceptButton = findViewById(R.id.acceptButton);
        backButton = findViewById(R.id.backButton);



        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Providers");

        Query checkUser = reference.orderByChild("userID").equalTo(providerUserID);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    provider.setEmail(snapshot.child(providerUserID).child("email").getValue().toString());
                    provider.setId(snapshot.child(providerUserID).child("id").getValue(String.class));
                    provider.setUserName(snapshot.child(providerUserID).child("userName").getValue(String.class));
                    provider.setJobDesc(snapshot.child(providerUserID).child("jobDesc").getValue(String.class));
                    provider.setGender(snapshot.child(providerUserID).child("gender").getValue(String.class));
                    provider.setAge(snapshot.child(providerUserID).child("age").getValue(String.class));
                    provider.setPhoneNumber(snapshot.child(providerUserID).child("phoneNumber").getValue(String.class));


                    storageReference = FirebaseStorage.getInstance().getReference().child("images/"+providerUserID);
                    final Bitmap[] bitmap = new Bitmap[1];
                    try{
                        File localfile = File.createTempFile( provider.getId(),".jpg");
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

                    }
                    catch (Exception e){
                        bitmap[0] = BitmapFactory.decodeFile("app/defaultProfilePic.jpeg");
                        profilePic.setImageBitmap(bitmap[0]);
                        provider.setImageBitmap(bitmap[0]);
                    }


                    username.setText("User Name:   "+provider.getUserName());
                    jobDescription.setText("Job Description:   "+provider.getJobDesc());
                    gender.setText("Gender:   "+provider.getGender());


                    age.setText("Age:   "+provider.getAge());
                    id.setText("ID Number:   "+provider.getId());
                    email.setText("Email:   "+provider.getEmail());
                    phoneNumber.setText("Phone Number:   "+provider.getPhoneNumber());

                    profilePic.setImageBitmap(provider.getImageBitmap());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sending notification to provider part
                FirebaseDatabase.getInstance().getReference("Tokens").child(providerUserID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String userToken= snapshot.getValue(String.class);
                        sendNotifications(userToken,"Accepted",seekerEmail+" has accepted your proposal!");

                        userType = "seeker";

                        Intent intent = new Intent(ChosenProviderProfile.this, LocalRequestEnd1.class);
                        intent.putExtra("receiver id", providerUserID);
                        intent.putExtra("arrival time", estimatedArrivaltime);
                        intent.putExtra("completion time", estimatedCompletionTime);
                        intent.putExtra("price", price);
                        intent.putExtra("user type", userType);
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //Notification part ends here
            }
        });
        UpdateToken();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });



    }

    private void UpdateToken(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid()).setValue(token);
    }

    public void sendNotifications(String userToken,String title,String message) {
        Data data = new Data(title,message);
        NotificationSender sender = new NotificationSender(data, userToken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200){
                    if (response.body().success != 1){
                        Toast.makeText(ChosenProviderProfile.this,"Failed",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(ChosenProviderProfile.this,"Notification sent",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });

    }






}