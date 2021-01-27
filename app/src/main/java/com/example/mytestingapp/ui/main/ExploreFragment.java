package com.example.mytestingapp.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.mytestingapp.LocalRequest;
import com.example.mytestingapp.LocalRequestInfoActivity;
import com.example.mytestingapp.R;
import com.example.mytestingapp.ServiceAdaptor;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


public class ExploreFragment extends Fragment {

    private EditText suburbEditText;
    private Button filterBtn;



    ListView listView;
    List<LocalRequest> localRequestList = new ArrayList<>();

    ServiceAdaptor serviceAdaptor;


    DatabaseReference reference;

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            LocalRequest localRequest = snapshot.getValue(LocalRequest.class);

            localRequestList.add(localRequest);

            serviceAdaptor.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            serviceAdaptor.notifyDataSetChanged();

            localRequestList.clear();

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



    public ExploreFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        suburbEditText = view.findViewById(R.id.suburbEditText);
        filterBtn = view.findViewById(R.id.filterBtn);

        serviceAdaptor = new ServiceAdaptor(getActivity(),localRequestList);
        listView = view.findViewById(R.id.serviceList);
        listView.setAdapter(serviceAdaptor);



        reference = FirebaseDatabase.getInstance().getReference("LocalRequests");


        reference.addChildEventListener(childEventListener);




        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceAdaptor.clear();
                String suburbFilter = suburbEditText.getText().toString().trim();
                suburbEditText.setText("");

                if (!suburbFilter.equals("")) {
                    Query query = reference.orderByChild("suburb").equalTo(suburbFilter);
                    query.addChildEventListener(childEventListener);
                }
                else{
                    Query query = reference.orderByChild("suburb");
                    query.addChildEventListener(childEventListener);
                }


            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), LocalRequestInfoActivity.class);
                intent.putExtra("Request info", localRequestList.get(position));
                startActivity(intent);
            }
        });


        return view;
    }





}