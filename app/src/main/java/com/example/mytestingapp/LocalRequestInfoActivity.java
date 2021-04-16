package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mytestingapp.Classes.LocalRequest;
import com.example.mytestingapp.Classes.LocalRequestApplicant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocalRequestInfoActivity extends AppCompatActivity {

    private TextView requestTitle, requestDescription, city, suburb, streetName, streetNumber, buildingName,
                buildingNumber, floorNumber, apartmentNumber, seekerEmail;

    private Button submitBtn,submitBtn2;
    private LinearLayout hiddenLayout;
    private ScrollView scrollable;

    private EditText minPriceValue, maxPriceValue, estimatedTime;

    private String providerEmail;


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

        minPriceValue = findViewById(R.id.minPriceValue);
        maxPriceValue = findViewById(R.id.maxPriceValue);
        estimatedTime = findViewById(R.id.estimatedTimeValue);

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

        providerEmail = getIntent().getStringExtra("Provider email");

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

                float MinPriceValue;
                float MaxPriceValue;
                int EstimatedTime;

                try{
                     MinPriceValue = Float.parseFloat(minPriceValue.getText().toString().trim());
                     MaxPriceValue = Float.parseFloat(maxPriceValue.getText().toString().trim());
                     EstimatedTime = Integer.parseInt(estimatedTime.getText().toString().trim());
                }
                catch(Exception e){
                     MinPriceValue = 0;
                     MaxPriceValue = 10;
                     EstimatedTime = 24;
                }


                LocalRequestApplicant localRequestApplicant = new LocalRequestApplicant(MinPriceValue,MaxPriceValue,EstimatedTime,providerEmail+".com");

                String[] temp = localRequest.getSeekerEmail().split(".com");

                reference = FirebaseDatabase.getInstance().getReference().child("LocalRequestsProposals");
                reference.child(temp[0]).child(providerEmail).setValue(localRequestApplicant);

                finish();


            }
        });


    }
}