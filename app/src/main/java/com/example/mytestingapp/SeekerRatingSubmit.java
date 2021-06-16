package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.mytestingapp.Classes.SeekerRating;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SeekerRatingSubmit extends AppCompatActivity {


    Intent intent;

    String providerId;
    String userType;
    String price;


    Button submitBtn;

    EditText price_value, ratingNote, review;

    RatingBar ratingBar;

    FirebaseUser firebaseUser;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_rating_submit);


        intent = getIntent();

        providerId = intent.getStringExtra("receiver id");
        userType = intent.getStringExtra("user type");
        price = intent.getStringExtra("price");


        ratingNote = findViewById(R.id.ratingNote);
        price_value = findViewById(R.id.price_value);
        review = findViewById(R.id.review);

        submitBtn = findViewById(R.id.submitButton);

        ratingBar = findViewById(R.id.rating_bar);

        price_value.setText("Final price to be paid by the seeker to the provider: "+ price);

        ratingNote.setText("Rate the seeker");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                String userId = firebaseUser.getUid();

                String one = review.getText().toString().trim();

                float two = ratingBar.getRating();

                SeekerRating seekerRating = new SeekerRating(userId, providerId, one, two);


                reference = FirebaseDatabase.getInstance().getReference().child("Providers")
                        .child(providerId)
                        .child("ratings");
                reference.child(userId).setValue(seekerRating);

                //TODO remove local request, local request proposals, arrival and completion flags.

                Intent intent = new Intent(SeekerRatingSubmit.this,SeekerMainHomeActivity.class);
                startActivity(intent);
                finish();


            }
        });



    }
}