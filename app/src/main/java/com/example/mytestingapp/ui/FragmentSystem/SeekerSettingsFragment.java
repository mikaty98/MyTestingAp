package com.example.mytestingapp.ui.FragmentSystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mytestingapp.PLoginActivity;
import com.example.mytestingapp.ProviderChangePasswordActivity;
import com.example.mytestingapp.R;
import com.example.mytestingapp.SLoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SeekerSettingsFragment extends Fragment {

    private Button signOut, deleteBtn, changePasswordBtn, termsBtn;
    private FirebaseUser user;

    public SeekerSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seeker_settings, container, false);

        signOut = view.findViewById(R.id.signOut);
        deleteBtn = view.findViewById(R.id.deleteBtn);
        changePasswordBtn = view.findViewById(R.id.changePasswordBtn);
        termsBtn = view.findViewById(R.id.termsBtn);
        user = FirebaseAuth.getInstance().getCurrentUser();

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens").child(user.getUid());
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

        termsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!getActivity().isFinishing()){
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Terms And Conditions")
                                    .setMessage("1- You can only request one service at a time."+"\n\n"
                                            +"2- You can accept only one proposal from the waiting list of proposals for a given service request."
                                            +"\n\n"+"3- Once there is a connection established between you and the selected service provider, you can't" +
                                            " go back until the transaction is complete." +"\n\n" +
                                            "4- Arrival time policy: Each 3-minute period past the arrival time will automatically deduct 1 EGP " +
                                            "from the price you will pay to the service provider."
                                            +"\n\n" +"5- Service completion time policy: Each 5-minute period past the service completion time will" +
                                            " automatically deduct 1 EGP from the price you will pay to the service provider."
                                    )
                                    .setCancelable(false)
                                    .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {

                                        }
                                    }).show();
                        }
                    }
                });


            }
        });



        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deleting Account")
                        .setMessage("Are you sure you want to delete your account permanently?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //1. delete from auth.
                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            //2. delete from database (also delete local requests if any).
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Seekers").child(user.getUid());
                                            reference.removeValue();
                                            reference = FirebaseDatabase.getInstance().getReference("LocalRequests").child(user.getUid());
                                            reference.removeValue();
                                            //3. delete image from storage.
                                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/"+ user.getUid());
                                            storageReference.delete();
                                            //4.delete token.
                                            reference = FirebaseDatabase.getInstance().getReference("Tokens").child(user.getUid());
                                            reference.removeValue();
                                            //-------------------
                                            startActivity(new Intent(getContext(), SLoginActivity.class));
                                            getActivity().finish();
                                        }
                                        else {
                                            Toast.makeText(getContext(), "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProviderChangePasswordActivity.class));
                getActivity().finish();
            }
        });

        return view;
    }
}