package com.example.mytestingapp.ui.FragmentSystem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytestingapp.Classes.Seeker;
import com.example.mytestingapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


public class SeekerProfileFragment extends Fragment {


    private EditText username,gender,age,id,email,phoneNumber;
    private CircleImageView profilePic;

    private Seeker seeker = new Seeker();

    private DatabaseReference reference;

    private StorageReference storageReference;

    private String userID;

    public SeekerProfileFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seeker_profile, container, false);

        username = view.findViewById(R.id.username);
        gender = view.findViewById(R.id.gender);
        age = view.findViewById(R.id.age);
        id = view.findViewById(R.id.id);
        email = view.findViewById(R.id.email);
        phoneNumber = view.findViewById(R.id.phone_number);
        profilePic = view.findViewById(R.id.profilePic);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference = FirebaseDatabase.getInstance().getReference("Seekers");

        Query checkuser = reference.orderByChild("userID").equalTo(userID);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    seeker = snapshot.child(userID).getValue(Seeker.class);
                    getProfilePic();

                    username.setText(seeker.getUserName());
                    gender.setText(seeker.getGender());
                    age.setText(seeker.getAge());
                    id.setText(seeker.getId());
                    email.setText(seeker.getEmail());
                    phoneNumber.setText(seeker.getPhoneNumber());
                    profilePic.setImageBitmap(seeker.getImageBitmap());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"user not found", Toast.LENGTH_LONG).show();
            }
        });

        return view;

    }

    private void getProfilePic(){
        storageReference = FirebaseStorage.getInstance().getReference().child("images/"+userID);
        final Bitmap[] bitmap = new Bitmap[1];
        try{
            File localfile = File.createTempFile(userID,".jpg");
            storageReference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmap[0] = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            profilePic.setImageBitmap(bitmap[0]);
                            seeker.setImageBitmap(bitmap[0]);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    bitmap[0] = BitmapFactory.decodeFile("defaultProfilePic.jpeg");
                    profilePic.setImageBitmap(bitmap[0]);
                    seeker.setImageBitmap(bitmap[0]);
                }
            });

        }
        catch (Exception e){
            bitmap[0] = BitmapFactory.decodeFile("app/defaultProfilePic.jpeg");
            profilePic.setImageBitmap(bitmap[0]);
            seeker.setImageBitmap(bitmap[0]);
        }

    }
}