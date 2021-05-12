package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mytestingapp.Classes.LocalRequest;
import com.example.mytestingapp.Classes.LocalRequestApplicant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocalRequestInfoActivity extends AppCompatActivity {

    private TextView requestTitle, requestDescription, city, suburb, streetName, streetNumber, buildingName,
                buildingNumber, floorNumber, apartmentNumber, seekerEmail;

    private Button submitBtn,submitBtn2;
    private LinearLayout hiddenLayout;
    private ScrollView scrollable;

    private EditText priceValue, estimatedArrivalTime, estimatedCompletionTime;

    private String userID;


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
        submitBtn2 = findViewById(R.id.submitBtn2);

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
        buildingName.setText(localRequest.getBuildingName());
        buildingNumber.setText(localRequest.getBuildingNumber());
        floorNumber.setText(localRequest.getFloorNumber());
        apartmentNumber.setText(localRequest.getApartmentNumber());
        seekerEmail.setText(localRequest.getSeekerEmail());

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

                int PriceValue;
                int EstimatedArrivalTime;
                int EstimatedCompletionTime;

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


                LocalRequestApplicant localRequestApplicant = new LocalRequestApplicant(PriceValue,EstimatedArrivalTime,EstimatedCompletionTime, userID);

                String temp[] = localRequest.getSeekerEmail().split(".com");

                reference = FirebaseDatabase.getInstance().getReference().child("LocalRequestsProposals");
                reference.child(temp[0]).child(userID).setValue(localRequestApplicant);

                finish();



            }
        });


    }
}