package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class LocalRequestInfoActivity extends AppCompatActivity {

    private TextView requestTitle, requestDescription, city, suburb, streetName, streetNumber, buildingName,
                buildingNumber, floorNumber, apartmentNumber, seekerEmail;

    private Button submitBtn;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_request_info);

        requestTitle = findViewById(R.id.requestTitleValue);
        requestDescription = findViewById(R.id.requestDescriptionValue);
        city = findViewById(R.id.cityValue);
        suburb = findViewById(R.id.suburbValue);
        streetName = findViewById(R.id.streetNameValue);
        streetNumber = findViewById(R.id.streetNumberValue);
        buildingName = findViewById(R.id.buildingNameValue);
        buildingNumber = findViewById(R.id.buildingNumberValue);
        floorNumber = findViewById(R.id.floorNumberValue);
        apartmentNumber = findViewById(R.id.apartmentNumberValue);
        seekerEmail = findViewById(R.id.seekerEmailValue);

        submitBtn = findViewById(R.id.submitBtn);

        LocalRequest localRequest = (LocalRequest) getIntent().getSerializableExtra("Request info");

        requestTitle.setText(localRequest.getRequestTitle());
        requestDescription.setText(localRequest.getRequestDescription());
        city.setText(localRequest.getCity());
        suburb.setText(localRequest.getSuburb());
        streetName.setText(localRequest.getStreetName());
        streetNumber.setText(localRequest.getStreetNumber());
        buildingName.setText(localRequest.getBuildingName());
        buildingNumber.setText(localRequest.getBuildingNumber());
        floorNumber.setText(localRequest.getFloorNumber());
        apartmentNumber.setText(localRequest.getApartmentNumber());
        seekerEmail.setText(localRequest.getSeekerEmail());


    }
}