package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SeekerGlobalRequest extends AppCompatActivity {


    private Button currentLocation, confrim, itemFrontImage, itemBackImage, itemSideImage;
    private EditText requestTitle,requestDescription,itemName, itemBrandName, itemSize, itemCategory, city,suburb,streetName,streetNumber,buildingName,buildingNumber,floorNumber,apartmentNumber;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private FirebaseStorage storage;
    private StorageReference ref;

    public Uri imageUriFront;


    public static final String TEXT2 = "com.example.mytestingapp";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_global_request);

        itemFrontImage = findViewById(R.id.itemFrontImage);
        itemBackImage = findViewById(R.id.itemBackImage);
        itemSideImage = findViewById(R.id.itemSideImage);

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


        itemFrontImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {

            }
        });

        itemFrontImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {



            }
        });

        itemFrontImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {



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