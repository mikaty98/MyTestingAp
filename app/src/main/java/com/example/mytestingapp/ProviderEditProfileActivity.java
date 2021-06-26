package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytestingapp.Classes.Provider;
import com.example.mytestingapp.Classes.Seeker;
import com.example.mytestingapp.ml.FaceDetection;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
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
import com.google.firebase.storage.UploadTask;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProviderEditProfileActivity extends AppCompatActivity {
    private String userID;
    private EditText username, jobDescription, gender, age, id, phoneNumber;
    private CircleImageView profilePic;
    private Uri imageUri;
    private Bitmap[] bitmap;
    private Bitmap img;
    private Button saveBtn;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private Provider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_edit_profile);

        profilePic = findViewById(R.id.profilePic);
        username = findViewById(R.id.username_reg);
        jobDescription = findViewById(R.id.job_description_reg);
        gender = findViewById(R.id.gender_reg);
        age = findViewById(R.id.age_reg);
        id = findViewById(R.id.id_reg);
        phoneNumber = findViewById(R.id.phone_number_reg);

        saveBtn = findViewById(R.id.saveBtn);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        reference = FirebaseDatabase.getInstance().getReference("Providers");
        Query checkUser = reference.orderByChild("userID").equalTo(userID);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    provider = snapshot.child(userID).getValue(Provider.class);
                    getProfilePic();
                    username.setText(provider.getUserName());
                    jobDescription.setText(provider.getJobDesc());
                    gender.setText(provider.getGender());
                    age.setText(provider.getAge());
                    id.setText(provider.getId());
                    phoneNumber.setText(provider.getPhoneNumber());
                    profilePic.setImageBitmap(provider.getImageBitmap());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String Id = id.getText().toString().trim();
        String UserName = username.getText().toString().trim();
        String JobDesc = jobDescription.getText().toString().trim();
        String Gender = gender.getText().toString().trim();
        String Agetxt = age.getText().toString().trim();
        Integer Age;
        if (Agetxt.equals("")) {
            Age = null;
        } else {
            Age = Integer.parseInt(Agetxt);
        }
        String PhoneNumber = phoneNumber.getText().toString().trim();

        boolean errorFlag = false;

        if (TextUtils.isEmpty(Id) || Id.length() != 14) {
            id.setError("*");
            errorFlag = true;
        }
        if (TextUtils.isEmpty(UserName)) {
            username.setError("*");
            errorFlag = true;
        }
        if (TextUtils.isEmpty(JobDesc)) {
            jobDescription.setError("*");
            errorFlag = true;
        }
        if (TextUtils.isEmpty(Gender)) {
            gender.setError("*");
            errorFlag = true;
        }
        if (!(Gender.equals("Male") || Gender.equals("male") || Gender.equals("Female") || Gender.equals("female"))) {
            gender.setError("Invalid input");
            errorFlag = true;
        }
        if (Age == null) {
            age.setError("*");
            errorFlag = true;
        } else if (Age < 18 || Age > 100) {
            age.setError("Inappropriate age");
            errorFlag = true;
        }
        if (TextUtils.isEmpty(PhoneNumber) || PhoneNumber.length() != 11) {
            phoneNumber.setError("*");
            errorFlag = true;
        }
        if (imageUri == null) {
            Toast.makeText(getApplicationContext(), "Profile picture required", Toast.LENGTH_LONG).show();
            errorFlag = true;
        }

        if (errorFlag) {
            return;
        }

        reference = FirebaseDatabase.getInstance().getReference("Providers").child(userID);
        reference.child("id").setValue(Id);
        reference.child("userName").setValue(UserName);
        reference.child("jobDesc").setValue(JobDesc);
        reference.child("gender").setValue(Gender);
        reference.child("age").setValue(Age.toString());
        reference.child("phoneNumber").setValue(PhoneNumber);
        uploadPic();

        Intent intent = new Intent(ProviderEditProfileActivity.this,ProviderHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


    }

    private void getProfilePic() {
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + userID);
        bitmap = new Bitmap[1];
        try {
            File localfile = File.createTempFile(userID, ".jpg");
            storageReference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmap[0] = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            profilePic.setImageBitmap(bitmap[0]);
                            provider.setImageBitmap(bitmap[0]);
                            imageUri = getImageUri(ProviderEditProfileActivity.this,bitmap[0]);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    bitmap[0] = BitmapFactory.decodeFile("defaultProfilePic.jpeg");
                    profilePic.setImageBitmap(bitmap[0]);
                    provider.setImageBitmap(bitmap[0]);
                    imageUri = getImageUri(ProviderEditProfileActivity.this,bitmap[0]);
                }
            });

        } catch (Exception e) {
            bitmap[0] = BitmapFactory.decodeFile("app/defaultProfilePic.jpeg");
            profilePic.setImageBitmap(bitmap[0]);
            provider.setImageBitmap(bitmap[0]);
            imageUri = getImageUri(ProviderEditProfileActivity.this,bitmap[0]);
        }

    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            boolean containingAFace = false;

            if (requestCode == 100 && resultCode == RESULT_OK && data!=null && data.getData()!=null)
            {
                Uri uri = data.getData();
                try
                {
                    img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    Bitmap resized = Bitmap.createScaledBitmap(img, 224, 224, true);

                    try {
                        FaceDetection model = FaceDetection.newInstance(ProviderEditProfileActivity.this);

                        // Creates inputs for reference.
                        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);

                        TensorImage tensorImage = new TensorImage(DataType.UINT8);
                        tensorImage.load(resized);
                        ByteBuffer byteBuffer = tensorImage.getBuffer();

                        inputFeature0.loadBuffer(byteBuffer);

                        // Runs model inference and gets result.
                        FaceDetection.Outputs outputs = model.process(inputFeature0);
                        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                        if(outputFeature0.getFloatArray()[0] > outputFeature0.getFloatArray()[1]
                                && outputFeature0.getFloatArray()[0] > outputFeature0.getFloatArray()[2]
                                && outputFeature0.getFloatArray()[0] > outputFeature0.getFloatArray()[3]
                                && outputFeature0.getFloatArray()[0] > outputFeature0.getFloatArray()[4]
                        )
                        {
                            containingAFace = true;

                        }

                        else
                        {
                            containingAFace = false;
                        }


                        // Releases model resources if no longer used.
                        model.close();
                    } catch (IOException e) {
                        // TODO Handle the exception
                    }




                } catch (IOException e)
                {
                    e.printStackTrace();
                }


                if(containingAFace == true)
                {
                    imageUri = data.getData();
                    profilePic.setImageURI(imageUri);
                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Profile picture should show your face clearly", Toast.LENGTH_LONG).show();
                }

            }


        }
    }

    private void uploadPic() {

        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + userID);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


}