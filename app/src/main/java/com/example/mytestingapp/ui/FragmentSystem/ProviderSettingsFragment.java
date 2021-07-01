package com.example.mytestingapp.ui.FragmentSystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.mytestingapp.ChatRoom;
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

public class ProviderSettingsFragment extends Fragment {

    private Button signOut,deleteBtn,changePasswordBtn, termsBtn;
    private FirebaseUser user;

    public ProviderSettingsFragment( ) {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider_settings,container,false);

        signOut = view.findViewById(R.id.signOut);
        deleteBtn = view.findViewById(R.id.deleteBtn);
        changePasswordBtn = view.findViewById(R.id.changePasswordBtn);
        termsBtn = view.findViewById(R.id.termsBtn);
        user = FirebaseAuth.getInstance().getCurrentUser();

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(v.getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Sign Out")
                        .setMessage("Are you sure you want to sign out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getUid());
                                reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(getActivity(), PLoginActivity.class));
                                        getActivity().finish();
                                    }
                                });

                            }

                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .show();

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
                                    .setMessage("1- You can only propose for one service at a time."+"\n\n"
                                            +"2- Once there is a connection established between you and the selected service seeker, you can't" +
                                            " go back until the transaction is complete." +"\n\n" +
                                            "3- Arrival time policy: Each 3-minute period past the arrival time will automatically deduct 1 EGP " +
                                            "from the price you will receive from the service seeker."
                                            +"\n\n" +"4- Service completion time policy: Each 5-minute period past the service completion time will" +
                                            " automatically deduct 1 EGP from the price you will receive from the service seeker."
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
                        .setTitle("Delete Account")
                        .setMessage("Are you sure you want to delete your account permanently?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //1. delete from auth.
                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            //2. delete from database.
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Providers").child(user.getUid());
                                            reference.removeValue();
                                            //3. delete image from storage.
                                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/"+ user.getUid());
                                            storageReference.delete();
                                            //4.delete token.
                                            reference = FirebaseDatabase.getInstance().getReference("Tokens").child(user.getUid());
                                            reference.removeValue();
                                            //-----------------
                                            startActivity(new Intent(getContext(), PLoginActivity.class));
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