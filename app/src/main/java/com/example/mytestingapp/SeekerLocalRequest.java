package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mytestingapp.Classes.LocalRequest;
import com.example.mytestingapp.ml.FaceDetection;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.hdodenhof.circleimageview.CircleImageView;

public class SeekerLocalRequest extends AppCompatActivity {


    private Button currentLocation, confrim, optional;
    private EditText requestTitle, requestDescription, city, suburb, streetName, streetNumber, buildingName, buildingNumber, floorNumber, apartmentNumber;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private FirebaseStorage storage;
    private StorageReference ref;



    public Uri imageUri;

    private ImageView optionalImage;

    private FirebaseAuth auth;




    public static final String TEXT2 = "com.example.mytestingapp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_local_request);


        currentLocation = findViewById(R.id.currentLocation);

        optionalImage = findViewById(R.id.optionalImage);


        requestTitle = findViewById(R.id.requestTitle);
        requestDescription = findViewById(R.id.requestDescription);
        city = findViewById(R.id.city);
        suburb = findViewById(R.id.suburb);
        streetName = findViewById(R.id.streetName);
        streetNumber = findViewById(R.id.streetNumber);
        buildingName = findViewById(R.id.buildingName);
        buildingNumber = findViewById(R.id.buildingNumber);
        floorNumber = findViewById(R.id.floor);
        apartmentNumber = findViewById(R.id.apartment);
        confrim = findViewById(R.id.confirm);
        optional = findViewById(R.id.optional);


        optional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                choosePicture();
            }
        });


        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent1 = new Intent(SeekerLocalRequest.this, SeekerLocalRequestAutoMap.class);
                startActivity(intent1);


                //startActivity(new Intent(getApplicationContext(), SeekerLocalRequestAutoMap.class));
            }
        });


        confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String SeekerEmail = text;
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                reference = FirebaseDatabase.getInstance().getReference("Seekers");
                Query checkUser = reference.orderByChild("userID").equalTo(userID);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String seekerName = snapshot.child(userID).child("userName").getValue(String.class);
                            confirmSeeker(seekerName);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


    }

    private void confirmSeeker(String seekerName) {

        String RequestTitle = requestTitle.getText().toString().trim();
        String RequestDes = requestDescription.getText().toString().trim();
        String City = city.getText().toString().trim();
        String Suburb = suburb.getText().toString().trim();
        String StreetName = streetName.getText().toString().trim();
        String StreetNumber = streetNumber.getText().toString().trim();
        String BuildingName = buildingName.getText().toString().trim();
        String BuildingNumber = buildingNumber.getText().toString().trim();
        String FloorNumber = floorNumber.getText().toString().trim();
        String ApartmentNumber = apartmentNumber.getText().toString().trim();

        boolean errorFlag = false;

        if (TextUtils.isEmpty(RequestTitle))
        {
            requestTitle.setError("This field can not be left empty");
            errorFlag = true;
        }

        if (TextUtils.isEmpty(RequestDes))
        {
            requestDescription.setError("This field can not be left empty");
            errorFlag = true;
        }

        if (TextUtils.isEmpty(City))
        {
            city.setError("This field can not be left empty");
            errorFlag = true;
        }

        if (TextUtils.isEmpty(Suburb))
        {
            suburb.setError("This field can not be left empty");
            errorFlag = true;
        }

        if (TextUtils.isEmpty(StreetName))
        {
            streetName.setError("This field can not be left empty");
            errorFlag = true;
        }
        if (TextUtils.isEmpty(StreetNumber))
        {
            streetNumber.setError("This field can not be left empty");
            errorFlag = true;
        }
        if (TextUtils.isEmpty(BuildingName))
        {
            buildingName.setError("This field can not be left empty");
            errorFlag = true;
        }
        if (TextUtils.isEmpty(BuildingNumber))
        {
            buildingNumber.setError("This field can not be left empty");
            errorFlag = true;
        }

        if (TextUtils.isEmpty(FloorNumber))
        {
            floorNumber.setError("This field can not be left empty");
            errorFlag = true;
        }
        if (TextUtils.isEmpty(ApartmentNumber))
        {
            apartmentNumber.setError("This field can not be left empty");
            errorFlag = true;
        }


        if (errorFlag)
        {
            return;
        }

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("LocalRequests");

        LocalRequest l = new LocalRequest(RequestTitle, RequestDes, City, Suburb, StreetName, StreetNumber.toString(), BuildingName, BuildingNumber.toString(), FloorNumber.toString(), ApartmentNumber.toString(), seekerName
                , FirebaseAuth.getInstance().getUid(), "no");


        reference.child(FirebaseAuth.getInstance().getUid()).setValue(l);

        uploadImage();

        Intent intent2 = new Intent(SeekerLocalRequest.this, SeekerLocalRequestWaitingList.class);
        intent2.putExtra("seeker name", seekerName);
        startActivity(intent2);
        finish();


    }


    private void choosePicture()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 100 && resultCode == RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri = data.getData();
            optionalImage.setImageURI(imageUri);
        }
    }

    private void uploadImage()
    {
        auth = FirebaseAuth.getInstance();


        String userID = auth.getCurrentUser().getUid();


        if(imageUri != null)
        {


            storage = FirebaseStorage.getInstance();

            ref = storage.getReference();

            StorageReference riversRef = ref.child("images/"+userID+"item");

            riversRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Snackbar.make(findViewById(android.R.id.content),"Image Uploaded.",Snackbar.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(),"Failed to upload", Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }

}