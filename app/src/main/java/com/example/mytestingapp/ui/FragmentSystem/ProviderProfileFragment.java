package com.example.mytestingapp.ui.FragmentSystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytestingapp.Classes.Provider;
import com.example.mytestingapp.Classes.Seeker;
import com.example.mytestingapp.ProviderEditProfileActivity;
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


public class ProviderProfileFragment extends Fragment {

    private EditText username,jobDescription,gender,birthDate,id,email,phoneNumber;
    private CircleImageView profilePic;
    private Button editBtn;

    private Provider provider = new Provider();

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Bitmap[] bitmap;
    private Uri imageUri;


    private String userID;
    




    public ProviderProfileFragment( ) {
        // Required empty public constructor
    }

    private void getProfilePic(){
        storageReference = FirebaseStorage.getInstance().getReference().child("images/"+userID);
        bitmap = new Bitmap[1];
        try{
            File localfile = File.createTempFile(userID,".jpg");
            storageReference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmap[0] = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            profilePic.setImageBitmap(bitmap[0]);
                            provider.setImageBitmap(bitmap[0]);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    bitmap[0] = BitmapFactory.decodeFile("defaultProfilePic.jpeg");
                    profilePic.setImageBitmap(bitmap[0]);
                    provider.setImageBitmap(bitmap[0]);
                }
            });

        }
        catch (Exception e){
            bitmap[0] = BitmapFactory.decodeFile("app/defaultProfilePic.jpeg");
            profilePic.setImageBitmap(bitmap[0]);
            provider.setImageBitmap(bitmap[0]);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_provider_profile, container, false);

        

        username = view.findViewById(R.id.username);
        jobDescription = view.findViewById(R.id.job_description);
        gender = view.findViewById(R.id.gender);
        birthDate = view.findViewById(R.id.BirthDate4);
        id = view.findViewById(R.id.id);
        email = view.findViewById(R.id.email);
        phoneNumber = view.findViewById(R.id.phone_number);
        profilePic = view.findViewById(R.id.profilePic);
        editBtn = view.findViewById(R.id.editBtn);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Providers");

        Query checkUser = reference.orderByChild("userID").equalTo(userID);
        
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    provider = snapshot.child(userID).getValue(Provider.class);
                    getProfilePic();

                    String birthDay = provider.getBirthDay();
                    String birthMonth = provider.getBirthMonth();
                    String birthYear = provider.getBirthYear();

                    username.setText(provider.getUserName());
                    gender.setText(provider.getGender());
                    jobDescription.setText(provider.getJobDesc());
                    birthDate.setText(birthDay+"/"+birthMonth+"/"+birthYear);
                    id.setText(provider.getId());
                    email.setText(provider.getEmail());
                    phoneNumber.setText(provider.getPhoneNumber());
                    profilePic.setImageBitmap(provider.getImageBitmap());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"user not found", Toast.LENGTH_LONG).show();
            }
        });


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProviderEditProfileActivity.class);
                startActivity(intent);
            }
        });




        return view;
    }


}