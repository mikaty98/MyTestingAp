package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytestingapp.Classes.LocalRequest;
import com.example.mytestingapp.Classes.LocalRequestApplicant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LocalRequestInfoActivity extends AppCompatActivity {

    private TextView requestTitle, requestDescription, city, suburb, streetName, streetNumber, buildingName,
                buildingNumber, floorNumber, apartmentNumber, seekerEmail;

    private Button submitBtn,submitBtn2;
    private LinearLayout hiddenLayout;
    private ScrollView scrollable;

    private EditText priceValue, estimatedArrivalTime, estimatedCompletionTime;

    private String providerID,providerEmail,seekerID;
    private SharedPreferences sp;


    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_request_info);


        //SharedPreferences mySharedPreferences = getSharedPreferences("intent", Context.MODE_PRIVATE);
        //int intent2 = mySharedPreferences.getInt("intent", 1);


        //Toast.makeText(LocalRequestInfoActivity.this,"INTENT DONEEEE"+ "   "+ intent2,Toast.LENGTH_LONG).show();

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
        seekerID = localRequest.getSeekerID();

        providerID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Providers");
        Query checkuser = reference.orderByChild("userID").equalTo(providerID);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    providerEmail = snapshot.child(providerID).child("email").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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


                LocalRequestApplicant localRequestApplicant = new LocalRequestApplicant(PriceValue,EstimatedArrivalTime,EstimatedCompletionTime, providerID, providerEmail);

                String temp[] = localRequest.getSeekerEmail().split(".com");

                reference = FirebaseDatabase.getInstance().getReference().child("LocalRequestsProposals");
                reference.child(temp[0]).child(providerID).setValue(localRequestApplicant);

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
                startActivity(intent);
                finish();


//                String one = "dF4CifhWkvTQ9ZZoEhbaPDbmKeI3";
//
//                userType = "provider";
//
//                Intent intent = new Intent(LocalRequestInfoActivity.this, ChatRoom.class);
//                intent.putExtra("receiver id", one);
//                intent.putExtra("user type", userType);
//                intent.putExtra("arrival time", EstimatedArrivalTime);
//                intent.putExtra("completion time", EstimatedCompletionTime);
//                intent.putExtra("price", PriceValue);
//
//                startActivity(intent);






            }
        });


    }
}