package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mytestingapp.Adapters.ProposalAdaptor;
import com.example.mytestingapp.Classes.LocalRequestApplicant;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SeekerLocalRequestWaitingList extends AppCompatActivity {
    private String seekerEmail;


    ListView listView;
    List<LocalRequestApplicant> localRequestApplicantList = new ArrayList<>();

    ProposalAdaptor proposalAdaptor;

    DatabaseReference reference;

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            LocalRequestApplicant localRequestApplicant = snapshot.getValue(LocalRequestApplicant.class);
            localRequestApplicantList.add(localRequestApplicant);
            proposalAdaptor.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            proposalAdaptor.notifyDataSetChanged();
            //localRequestApplicantList.clear();

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_local_request_waiting_list);

        proposalAdaptor = new ProposalAdaptor(this,localRequestApplicantList);
        listView = findViewById(R.id.requestProposalsList);
        listView.setAdapter(proposalAdaptor);

        seekerEmail = getIntent().getStringExtra("seeker email");
        String temp[] = seekerEmail.split(".com");
        String semail = temp[0];

        reference = FirebaseDatabase.getInstance().getReference("LocalRequestsProposals").child(semail);

        reference.addChildEventListener(childEventListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String providerEmail = localRequestApplicantList.get(position).getProviderEmail();
                int estimatedArrivalTime = localRequestApplicantList.get(position).getEstimatedArrivalTime();
                int estimatedCompletionTime = localRequestApplicantList.get(position).getEstimatedCompletionTime();
                int price = localRequestApplicantList.get(position).getPriceValue();
                String userID = localRequestApplicantList.get(position).getUserID();



                Intent intent2 = new Intent(SeekerLocalRequestWaitingList.this, ChosenProviderProfile.class);
                intent2.putExtra("provider email", providerEmail);
                intent2.putExtra("estimatedArrivalTime", estimatedArrivalTime);
                intent2.putExtra("estimatedCompletionTime", estimatedCompletionTime);
                intent2.putExtra("price", price);
                intent2.putExtra("seeker email", seekerEmail);
                intent2.putExtra("userID",userID);
                startActivity(intent2);




                //NotificationAlert exampleDialog = new NotificationAlert();
                //exampleDialog.show(getSupportFragmentManager(), "example dialog");

            }
        });



    }
}