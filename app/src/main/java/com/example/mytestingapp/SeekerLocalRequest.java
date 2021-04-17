package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mytestingapp.Classes.LocalRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SeekerLocalRequest extends AppCompatActivity {


    private Button currentLocation, confrim;
    private EditText requestTitle,requestDescription,city,suburb,streetName,streetNumber,buildingName,buildingNumber,floorNumber,apartmentNumber;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private FirebaseStorage storage;
    private StorageReference ref;

    public static final String TEXT2 = "com.example.mytestingapp";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_local_request);




        currentLocation = findViewById(R.id.currentLocation);


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

                Intent intent1 = new Intent(SeekerLocalRequest.this, SeekerLocalRequestAutoMap.class);
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

                Intent intent2 = new Intent(SeekerLocalRequest.this, SeekerLocalRequestWaitingList.class);
                intent2.putExtra("seeker email", SeekerEmail);
                startActivity(intent2);
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

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("LocalRequests");

        LocalRequest l = new LocalRequest(RequestTitle,RequestDes,City,Suburb,StreetName,StreetNumber.toString(),BuildingName,BuildingNumber.toString(),FloorNumber.toString(),ApartmentNumber.toString(),SeekerEmail);

        reference.child(RequestTitle).setValue(l);

        //startActivity(new Intent(getApplicationContext(), SeekerLocalRequestWaitingList.class));










    }
}