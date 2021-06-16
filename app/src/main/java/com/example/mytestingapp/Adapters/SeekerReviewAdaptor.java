package com.example.mytestingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mytestingapp.Classes.SeekerRating;
import com.example.mytestingapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SeekerReviewAdaptor extends ArrayAdapter {
    List<SeekerRating> SeekersRatingList;
    Context context;

    public SeekerReviewAdaptor(@NonNull Context context, List<SeekerRating> SeekersRatingList) {
        super(context, R.layout.provider_review_list_item, SeekersRatingList);

        this.context = context;
        this.SeekersRatingList = SeekersRatingList;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NotNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.seeker_review_list_item, parent, false);

        TextView seekerId = view.findViewById(R.id.seekerEmail);
        RatingBar ratingBar = view.findViewById(R.id.raitingBar);
        TextView textView3 = view.findViewById(R.id.reviewPara);


        seekerId.setText(String.valueOf(SeekersRatingList.get(position).getseekerEmail()));
        ratingBar.setRating(SeekersRatingList.get(position).getStarNumber());
        textView3.setText(SeekersRatingList.get(position).getReview());

        return view;
    }
}
