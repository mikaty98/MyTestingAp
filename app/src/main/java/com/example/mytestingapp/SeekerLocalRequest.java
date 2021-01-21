package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SeekerLocalRequest extends AppCompatActivity {


    private Button currentLocation, confrim;
    private EditText request,city,suburb,streetName,streetNumber,buildingName,buildingNumber,floorNumber,apartmentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_local_request);

        currentLocation = findViewById(R.id.currentLocation);


        request = findViewById(R.id.request);
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

                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });

        //confirm.setOnClickListener(new View.OnClickListener(){
          //  @Override
            //public void onClick(View v){

            //    startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            //}
        //});


    }
}