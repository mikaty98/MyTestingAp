package com.example.mytestingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mytestingapp.Classes.LocalRequest;

import java.util.List;

public class ServiceAdaptor extends ArrayAdapter {

   /* List<String> titleList;
    List<String> locationList;
    List<String> seekerEmailList;*/

    List<LocalRequest> localRequestList;
    Context context;


    public ServiceAdaptor(@NonNull Context context,List<LocalRequest> localRequestList) {
        super(context, R.layout.service_list_items,localRequestList);


        this.context = context;
        /*this.titleList = titleList;
        this.suburbList = suburbList;
        this.seekerEmailList = seekerEmailList;*/
        this.localRequestList = localRequestList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_list_items,parent,false);

        TextView textView1 = view.findViewById(R.id.titleValue);
        TextView textView2 = view.findViewById(R.id.suburbValue);
        TextView textView3 = view.findViewById(R.id.emailValue);

        /*textView1.setText(titleList.get(position));
        textView2.setText(suburbList.get(position));
        textView3.setText(seekerEmailList.get(position));*/

        textView1.setText(localRequestList.get(position).getRequestTitle());
        textView2.setText(localRequestList.get(position).getSuburb());
        textView3.setText(localRequestList.get(position).getSeekerEmail());

        return view;
    }
}
