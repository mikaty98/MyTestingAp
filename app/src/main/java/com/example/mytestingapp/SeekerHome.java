package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SeekerHome extends AppCompatActivity {

    public static final String TEXT1 = "com.example.mytestingapp";

    private Button localRequest;
    private Button overboardRequest;
    private Button officialRequest;
    public String inputEmail, userPassword, userName, age, id, phoneNumber, gender;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_home);



        Intent intent = getIntent();
        String text = intent.getStringExtra("seeker email");


        localRequest = findViewById(R.id.localService);
        overboardRequest = findViewById(R.id.overboardService);
        officialRequest = findViewById(R.id.officialService);



        localRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(SeekerHome.this, SeekerLocalRequest.class);
                intent.putExtra("seeker email", text);
                startActivity(intent);


            }
        });

       // overboardRequest.setOnClickListener(new View.OnClickListener(){
         //   @Override
           // public void onClick(View v){

             //   startActivity(new Intent(getApplicationContext(), PRegisterActivity.class));
            //}
        //});


       // officialRequest.setOnClickListener(new View.OnClickListener(){
         //   @Override
           // public void onClick(View v){

             //   startActivity(new Intent(getApplicationContext(), PRegisterActivity.class));
            //}
        //});





        String parts [] = text.split(".com");
        inputEmail = parts[0];



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Seekers");
        Query checkUser = ref.orderByChild("email").equalTo(text);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                 userName = snapshot.child(inputEmail).child("userName").getValue(String.class);
                 userPassword = snapshot.child(inputEmail).child("password").getValue(String.class);
                 age = snapshot.child(inputEmail).child("age").getValue(String.class);
                 gender = snapshot.child(inputEmail).child("gender").getValue(String.class);
                 id = snapshot.child(inputEmail).child("id").getValue(String.class);
                 phoneNumber = snapshot.child(inputEmail).child("phoneNumber").getValue(String.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}