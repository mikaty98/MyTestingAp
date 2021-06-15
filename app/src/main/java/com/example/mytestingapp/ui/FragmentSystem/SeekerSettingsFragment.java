package com.example.mytestingapp.ui.FragmentSystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mytestingapp.PLoginActivity;
import com.example.mytestingapp.R;
import com.example.mytestingapp.SLoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SeekerSettingsFragment extends Fragment {

    private Button signOut;

    public SeekerSettingsFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seeker_settings,container,false);

        signOut = view.findViewById(R.id.signOut);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getUid());
                reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getActivity(), SLoginActivity.class));
                        getActivity().finish();
                    }
                });

            }
        });

        return view;
    }
}