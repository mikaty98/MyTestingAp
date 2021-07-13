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

    private EditText suburbEditText,requestTitleEditText;
    private Button filterBtn;


    ListView listView;
    List<LocalRequest> localRequestList = new ArrayList<>();

    ServiceAdaptor serviceAdaptor;


    DatabaseReference reference;

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            LocalRequest localRequest = snapshot.getValue(LocalRequest.class);
            String picked = localRequest.getPicked();
            String suburbFilter = suburbEditText.getText().toString().trim();
            String titleFilter = requestTitleEditText.getText().toString().trim();
            if ((!suburbFilter.equals(""))&&(!titleFilter.equals(""))) { //both are not empty
                suburbEditText.setText("");
                requestTitleEditText.setText("");
                if (equalIgnoreCase(suburbFilter,localRequest.getSuburb())&&equalIgnoreCase(titleFilter,localRequest.getRequestTitle())) {
                    if (picked.equals("no")) {
                        localRequestList.add(localRequest);
                        serviceAdaptor.notifyDataSetChanged();
                    }
                }
            }
            else if ((!suburbFilter.equals(""))&&titleFilter.equals("")){ //suburb not empty and title is empty
                suburbEditText.setText("");
                if (equalIgnoreCase(suburbFilter,localRequest.getSuburb())) {
                    if (picked.equals("no")) {
                        localRequestList.add(localRequest);
                        serviceAdaptor.notifyDataSetChanged();
                    }
                }
            }
            else if (suburbFilter.equals("")&&(!titleFilter.equals(""))){ //suburb is empty and title not empty
                requestTitleEditText.setText("");
                if (equalIgnoreCase(titleFilter,localRequest.getRequestTitle())) {
                    if (picked.equals("no")) {
                        localRequestList.add(localRequest);
                        serviceAdaptor.notifyDataSetChanged();
                    }
                }
            }
            else { // both are empty
                if (picked.equals("no")) {
                    localRequestList.add(localRequest);
                    serviceAdaptor.notifyDataSetChanged();
                }

            }


        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            LocalRequest localRequest = snapshot.getValue(LocalRequest.class);
            localRequestList.remove(localRequest);
            serviceAdaptor.notifyDataSetChanged();

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            LocalRequest localRequest = snapshot.getValue(LocalRequest.class);
            localRequestList.remove(localRequest);
            serviceAdaptor.notifyDataSetChanged();


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
        requestTitleEditText = view.findViewById(R.id.requestTitleEditText);

        filterBtn = view.findViewById(R.id.filterBtn);

        localRequestList.clear();

        serviceAdaptor = new ServiceAdaptor(getActivity(), localRequestList);
        listView = view.findViewById(R.id.serviceList);
        listView.setAdapter(serviceAdaptor);


        reference = FirebaseDatabase.getInstance().getReference("LocalRequests");
        reference.addChildEventListener(childEventListener);


        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localRequestList.clear();
                serviceAdaptor.notifyDataSetChanged();
                reference.addChildEventListener(childEventListener);

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

    static boolean equalIgnoreCase(String str1, String str2)
    {
        int i = 0;

        // length of first string
        int len1 = str1.length();

        // length of second string
        int len2 = str2.length();

        // if length is not same
        // simply return false since both string
        // can not be same if length is not equal
        if (len1 != len2)
            return false;

        // loop to match one by one
        // all characters of both string
        while (i < len1)
        {

            // if current characters of both string are same,
            // increase value of i to compare next character
            if (str1.charAt(i) == str2.charAt(i))
            {
                i++;
            }

            else if (!((str1.charAt(i) >= 'a' && str1.charAt(i) <= 'z')
                    || (str1.charAt(i) >= 'A' && str1.charAt(i) <= 'Z')))
            {
                return false;
            }

            // do the same for second string
            else if (!((str2.charAt(i) >= 'a' && str2.charAt(i) <= 'z')
                    || (str2.charAt(i) >= 'A' && str2.charAt(i) <= 'Z')))
            {
                return false;
            }

            // this block of code will be executed
            // if characters of both strings
            // are of different cases
            else
            {
                // compare characters by ASCII value
                if (str1.charAt(i) >= 'a' && str1.charAt(i) <= 'z')
                {
                    if (str1.charAt(i) - 32 != str2.charAt(i))
                        return false;
                }

                else if (str1.charAt(i) >= 'A' && str1.charAt(i) <= 'Z')
                {
                    if (str1.charAt(i) + 32 != str2.charAt(i))
                        return false;
                }

                // if characters matched,
                // increase the value of i to compare next char
                i++;

            } // end of outer else block

        } // end of while loop


        return true;

    }

}