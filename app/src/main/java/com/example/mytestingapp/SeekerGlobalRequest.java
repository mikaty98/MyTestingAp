package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytestingapp.Classes.GlobalRequest;
import com.example.mytestingapp.Classes.LocalRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.inappmessaging.FirebaseInAppMessagingDismissListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class SeekerGlobalRequest extends AppCompatActivity {


    private Button currentLocation, confrim, itemImage, itemImageUpload;
    private EditText requestTitle,requestDescription,itemName, itemBrandName, itemSize, itemCategory, city,suburb,streetName,streetNumber,buildingName,buildingNumber,floorNumber,apartmentNumber;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;



    public static final String TEXT2 = "com.example.mytestingapp";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_global_request);

        itemImage = findViewById(R.id.itemImage);
        itemImageUpload = findViewById(R.id.itemImageUpload);
        imageView = findViewById(R.id.imgView);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        currentLocation = findViewById(R.id.currentLocation);

        requestTitle = findViewById(R.id.requestTitle);
        requestDescription = findViewById(R.id.requestDescription);

        itemName = findViewById(R.id.itemName);
        itemBrandName = findViewById(R.id.itemBrandName);
        itemSize = findViewById(R.id.itemSize);
        itemCategory = findViewById(R.id.itemCategory);


        city = findViewById(R.id.city);
        suburb = findViewById(R.id.suburb);
        streetName = findViewById(R.id.streetName);
        streetNumber = findViewById(R.id.streetNumber);
        buildingName = findViewById(R.id.buildingName);
        buildingNumber = findViewById(R.id.buildingNumber);
        floorNumber = findViewById(R.id.floor);
        apartmentNumber = findViewById(R.id.apartment);
        confrim = findViewById(R.id.confirm);


        itemImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                SelectImage();
            }
        });


        itemImageUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                uploadImage();

            }
        });




        currentLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // Intent intent1 = getIntent();
                //String text = intent1.getStringExtra(SLoginActivity.TEXT);

                //Intent intent2 = new Intent(SeekerLocalRequest.this, SeekerLocalRequestAutoMap.class);
                //intent2.putExtra(TEXT2, text);
                //startActivity(intent2);

                Intent intent = getIntent();
                String text = intent.getStringExtra("seeker email");

                Intent intent1 = new Intent(SeekerGlobalRequest.this, SeekerGlobalRequestAutoMap.class);
                intent1.putExtra("seeker email", text);
                startActivity(intent1);








                //startActivity(new Intent(getApplicationContext(), SeekerLocalRequestAutoMap.class));
            }
        });


        confrim.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = getIntent();
                String text = intent.getStringExtra("seeker email");


                String SeekerEmail = text;
                confirmSeeker(SeekerEmail);

                Intent intent2 = new Intent(SeekerGlobalRequest.this, SeekerGlobalRequestWaitingList.class);
                intent2.putExtra("seeker email", SeekerEmail);
                startActivity(intent2);
                finish();
            }
        });


    }



    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }



    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + FirebaseAuth.getInstance().getCurrentUser().getUid()+"item");

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(SeekerGlobalRequest.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(SeekerGlobalRequest.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }


    private void confirmSeeker(String SeekerEmail)
    {

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
        String itemName1 = itemName.getText().toString().trim();
        String itemBrandName1 = itemBrandName.getText().toString().trim();
        String itemSize1 = itemSize.getText().toString().trim();
        String itemCategory1 = itemCategory.getText().toString().trim();




        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("GlobalRequests");

        GlobalRequest g = new GlobalRequest(RequestTitle,RequestDes, itemCategory1, itemName1, itemSize1, itemBrandName1, City,Suburb,StreetName,StreetNumber.toString(),BuildingName,BuildingNumber.toString(),FloorNumber.toString(),ApartmentNumber.toString(),SeekerEmail
                , FirebaseAuth.getInstance().getUid());


        reference.child(RequestTitle).setValue(g);


        //startActivity(new Intent(getApplicationContext(), SeekerLocalRequestWaitingList.class));



    }


}