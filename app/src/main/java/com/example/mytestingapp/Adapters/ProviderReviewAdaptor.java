package com.example.mytestingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mytestingapp.Classes.ProviderRating;
import com.example.mytestingapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProviderReviewAdaptor extends ArrayAdapter {
    List<ProviderRating> ProvidersRatingList;
    Context context;

    public ProviderReviewAdaptor(@NonNull Context context, List<ProviderRating> ProvidersRatingList) {
        super(context, R.layout.provider_review_list_item, ProvidersRatingList);

        this.context = context;
        this.ProvidersRatingList = ProvidersRatingList;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NotNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.provider_review_list_item, parent, false);

        TextView providerName = view.findViewById(R.id.providerName);
        RatingBar ratingBar = view.findViewById(R.id.raitingBar);
        TextView textView3 = view.findViewById(R.id.reviewPara);


        providerName.setText(String.valueOf(ProvidersRatingList.get(position).getproviderName()));
        ratingBar.setRating(ProvidersRatingList.get(position).getStarNumber());
        textView3.setText(ProvidersRatingList.get(position).getReview());

        return view;
    }
}
