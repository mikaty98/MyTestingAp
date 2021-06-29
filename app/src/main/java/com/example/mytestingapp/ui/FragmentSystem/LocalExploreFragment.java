package com.example.mytestingapp.ui.FragmentSystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mytestingapp.Classes.LocalRequest;
import com.example.mytestingapp.LocalRequestInfoActivity;
import com.example.mytestingapp.ProviderWaitingRoomActivity;
import com.example.mytestingapp.R;
import com.example.mytestingapp.Adapters.ServiceAdaptor;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;


public class LocalExploreFragment extends Fragment {

    private EditText suburbEditText;
    private Button filterBtn;

    private String providerEmail;


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
            serviceAdaptor.notifyDataSetChanged();

            localRequestList.clear();

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    public LocalExploreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_explore, container, false);

        suburbEditText = view.findViewById(R.id.suburbEditText);

        filterBtn = view.findViewById(R.id.filterBtn);

        localRequestList.clear();

        serviceAdaptor = new ServiceAdaptor(getActivity(), localRequestList);
        listView = view.findViewById(R.id.serviceList);
        listView.setAdapter(serviceAdaptor);

        if (getArguments() != null) {
            providerEmail = getArguments().getString("provider email");
        }


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
                } else {
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