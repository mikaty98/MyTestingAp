package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytestingapp.Classes.LocalRequest;
import com.example.mytestingapp.Classes.LocalRequestApplicant;
import com.example.mytestingapp.Classes.Provider;
import com.example.mytestingapp.Classes.Seeker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LocalRequestInfoActivity extends AppCompatActivity {

    private TextView requestTitle, requestDescription, city, suburb, streetName, streetNumber,seekerNameTxt;

    private Button submitBtn,submitBtn2,seekerReviewBtn, viewImage;
    private LinearLayout hiddenLayout;
    private ScrollView scrollable;

    private EditText priceValue, estimatedArrivalTime, estimatedCompletionTime;

    private String providerID,seekerID;
    private Provider provider;
    private String seekerName;
    private SharedPreferences sp;

    private Bitmap[] bitmap;


    private ImageView viewImage2;


    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_request_info);


        //SharedPreferences mySharedPreferences = getSharedPreferences("intent", Context.MODE_PRIVATE);
        //int intent2 = mySharedPreferences.getInt("intent", 1);


        //Toast.makeText(LocalRequestInfoActivity.this,"INTENT DONEEEE"+ "   "+ intent2,Toast.LENGTH_LONG).show();

        requestTitle = findViewById(R.id.requestTitleValue);

        viewImage2 = findViewById(R.id.viewImage2);
        viewImage = findViewById(R.id.viewImage);
        requestDescription = findViewById(R.id.requestDescriptionValue);
        city = findViewById(R.id.cityValue);
        suburb = findViewById(R.id.suburbValue);
        streetName = findViewById(R.id.streetNameValue);
        streetNumber = findViewById(R.id.streetNumberValue);
        seekerNameTxt = findViewById(R.id.seekerEmailValue);

        submitBtn = findViewById(R.id.submitBtn);
        submitBtn2 = findViewById(R.id.submitBtn2);
        seekerReviewBtn = findViewById(R.id.seekerReviewBtn);

        priceValue = findViewById(R.id.priceValue);
        estimatedArrivalTime = findViewById(R.id.estimatedArrivalTime);
        estimatedCompletionTime = findViewById(R.id.estimatedCompletionTime);

        hiddenLayout = findViewById(R.id.hiddenLayout);
        scrollable = findViewById(R.id.scrollView);

        LocalRequest localRequest = (LocalRequest) getIntent().getSerializableExtra("Request info");

        requestTitle.setText(localRequest.getRequestTitle());
        requestDescription.setText(localRequest.getRequestDescription());
        city.setText(localRequest.getCity());
        suburb.setText(localRequest.getSuburb());
        streetName.setText(localRequest.getStreetName());
        streetNumber.setText(localRequest.getStreetNumber());
        seekerNameTxt.setText(localRequest.getseekerName());
        seekerID = localRequest.getSeekerID();

        providerID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Providers");
        Query checkuser = reference.orderByChild("userID").equalTo(providerID);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    provider = snapshot.child(providerID).getValue(Provider.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Seekers");
        checkuser = reference.orderByChild("userID").equalTo(seekerID);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    seekerName = snapshot.child(seekerID).child("userName").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        seekerReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewintent = new Intent(LocalRequestInfoActivity.this, SeekerReviewListActivity.class);
                reviewintent.putExtra("seeker id", seekerID);
                reviewintent.putExtra("seeker Name", seekerName);
                startActivity(reviewintent);
            }
        });

        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference pathReference = storageRef.child("images/"+seekerID+"item");

                bitmap = new Bitmap[1];


                try{
                    File localfile = File.createTempFile(seekerID,".jpg");
                    pathReference.getFile(localfile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    bitmap[0] = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                    viewImage2.setImageBitmap(bitmap[0]);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            bitmap[0] = BitmapFactory.decodeFile("defaultProfilePic.jpeg");
                            viewImage2.setImageBitmap(bitmap[0]);

                            Toast.makeText(LocalRequestInfoActivity.this, "No image by the seeker", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                catch (Exception e){
                    bitmap[0] = BitmapFactory.decodeFile("defaultProfilePic.jpeg");
                    viewImage2.setImageBitmap(bitmap[0]);

                    Toast.makeText(LocalRequestInfoActivity.this, "No image by the seeker", Toast.LENGTH_SHORT).show();


                }

            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenLayout.setVisibility(View.VISIBLE);
                scrollable.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollable.fullScroll(View.FOCUS_DOWN);
                    }
                });
                submitBtn.setEnabled(false);

            }
        });

        submitBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean errorflag = false;


                int PriceValue;
                int EstimatedArrivalTime;
                int EstimatedCompletionTime;

                String priceValuee = priceValue.getText().toString().trim();
                String arrivalTime = estimatedArrivalTime.getText().toString().trim();
                String completionTime = estimatedCompletionTime.getText().toString().trim();

                if (TextUtils.isEmpty(priceValuee))
                {
                    priceValue.setError("This field can not be left empty");
                    errorflag = true;
                }

                if (TextUtils.isEmpty(arrivalTime))
                {
                    estimatedArrivalTime.setError("This field can not be left empty");
                    errorflag = true;
                }

                if (TextUtils.isEmpty(completionTime))
                {
                    estimatedCompletionTime.setError("This field can not be left empty");
                    errorflag = true;
                }


                if(errorflag)
                {
                    return;
                }
                else
                {
                    try{

                        PriceValue = Integer.parseInt(priceValue.getText().toString().trim());
                        EstimatedArrivalTime = Integer.parseInt(estimatedArrivalTime.getText().toString().trim());
                        EstimatedCompletionTime= Integer.parseInt(estimatedCompletionTime.getText().toString().trim());
                    }
                    catch(Exception e){

                        PriceValue = 0;
                        EstimatedArrivalTime = 24;
                        EstimatedCompletionTime = 24;
                    }



                    LocalRequestApplicant localRequestApplicant = new LocalRequestApplicant(PriceValue,EstimatedArrivalTime,EstimatedCompletionTime, providerID, provider.getUserName(), "no");

                    reference = FirebaseDatabase.getInstance().getReference("Providers");
                    reference.child(providerID).child("sentProposal").setValue(true);


                    reference = FirebaseDatabase.getInstance().getReference().child("LocalRequestsProposals");
                    reference.child(seekerID).child(providerID).setValue(localRequestApplicant);

                    /*After submitting proposal, Provider will be sent to a waiting room waiting for the seeker to accept his proposal*/

                    Intent intent = new Intent(LocalRequestInfoActivity.this, ProviderWaitingRoomActivity.class);
                    sp = getSharedPreferences("DataSentToChatRoom", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("receiver id", seekerID);
                    editor.putString("user type","provider");
                    editor.putInt("arrival time", EstimatedArrivalTime);
                    editor.putInt("completion time", EstimatedCompletionTime);
                    editor.putInt("price", PriceValue);
                    editor.commit();
                    sp = getSharedPreferences("DatasentToPLogin", Context.MODE_PRIVATE);
                    editor = sp.edit();
                    editor.putString("seeker id", localRequest.getSeekerID());
                    editor.commit();
                    startActivity(intent);
                    finish();

                }


            }
        });

    }



}