package com.example.mytestingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mytestingapp.Classes.LocalRequestApplicant;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProposalAdaptor extends ArrayAdapter{

    List<LocalRequestApplicant> localRequestApplicantList;
    Context context;

    public ProposalAdaptor(@NonNull Context context,List<LocalRequestApplicant> localRequestApplicantList){
        super(context,R.layout.proposal_list_items,localRequestApplicantList);

        this.context = context;
        this.localRequestApplicantList = localRequestApplicantList;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NotNull ViewGroup parent){
        View view = LayoutInflater.from(context).inflate(R.layout.proposal_list_items,parent,false);

        TextView textView1 = view.findViewById(R.id.PriceValue);
        TextView textView2 = view.findViewById(R.id.EstimatedArrivalTime);
        TextView textView3 = view.findViewById(R.id.EstimatedCompletionTime);
        TextView textView4 = view.findViewById(R.id.providerEmail);


        textView1.setText(String.valueOf(localRequestApplicantList.get(position).getPriceValue())+" EGP");
        textView2.setText(String.valueOf(localRequestApplicantList.get(position).getEstimatedArrivalTime())+" Hrs");
        textView3.setText(String.valueOf(localRequestApplicantList.get(position).getEstimatedCompletionTime())+" Hrs");
        textView4.setText(localRequestApplicantList.get(position).getProviderEmail());

        return view;
    }
}
