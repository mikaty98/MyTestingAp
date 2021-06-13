package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Script;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.mytestingapp.Classes.ProviderRatings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProviderRating extends AppCompatActivity {

    Intent intent;

    String receiverId;
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
        setContentView(R.layout.activity_provider_rating);

        intent = getIntent();

        receiverId = intent.getStringExtra("receiver id");
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

                ProviderRatings providerRatings = new ProviderRatings(receiverId, userId, one, two);

                providerRatings.setSeekerId(receiverId);
                providerRatings.setProviderId(userId);
                providerRatings.setStarNumber(two);
                providerRatings.setReview(one);

                reference = FirebaseDatabase.getInstance().getReference().child("ProviderRatings");
                reference.child(receiverId).setValue(providerRatings);


            }
        });









    }
}