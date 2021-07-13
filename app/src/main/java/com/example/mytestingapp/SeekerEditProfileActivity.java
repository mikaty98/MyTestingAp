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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytestingapp.Classes.Seeker;
import com.example.mytestingapp.ml.FaceDetection;
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
import com.google.firebase.storage.UploadTask;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class SeekerEditProfileActivity extends AppCompatActivity {
    private String userID;
    private EditText username, gender, id, phoneNumber;
    private CircleImageView profilePic;
    private Uri imageUri;
    private Bitmap[] bitmap;
    private Bitmap img;
    private Button saveBtn;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private Seeker seeker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_edit_profile);

        profilePic = findViewById(R.id.profilePic);
        username = findViewById(R.id.username_reg);
        id = findViewById(R.id.id_reg);
        phoneNumber = findViewById(R.id.phone_number_reg);

        saveBtn = findViewById(R.id.saveBtn);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference = FirebaseDatabase.getInstance().getReference("Seekers");
        Query checkUser = reference.orderByChild("userID").equalTo(userID);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    seeker = snapshot.child(userID).getValue(Seeker.class);

                    getProfilePic();
                    profilePic.setImageBitmap(seeker.getImageBitmap());


                    username.setText(seeker.getUserName());
                    id.setText(seeker.getId());
                    phoneNumber.setText(seeker.getPhoneNumber());

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
            public void onClick(View v) { register(); }
        });
    }
    private void register() {
        String Id = id.getText().toString().trim();
        String UserName = username.getText().toString().trim();
        String PhoneNumber = phoneNumber.getText().toString().trim();

        boolean errorFlag = false;

        if (TextUtils.isEmpty(Id))
        {
            id.setError("ID number can not be left empty");
            errorFlag = true;
        }

        if(Id.length() != 14)
        {
            id.setError("Id Number is incorrect or incomplete");
            errorFlag = true;
        }
        if (TextUtils.isEmpty(UserName))
        {
            username.setError("User name can not be left empty");
            errorFlag = true;
        }

        if (TextUtils.isEmpty(PhoneNumber))
        {
            phoneNumber.setError("Phone number can not be empty");
            errorFlag = true;
        }

        if(PhoneNumber.length() != 11)
        {
            phoneNumber.setError("Phone number is incomplete");
            errorFlag = true;
        }


        if(!PhoneNumber.startsWith("010"))
        {
            if(!PhoneNumber.startsWith("011"))
            {
                if(!PhoneNumber.startsWith("012"))
                {
                    if(!PhoneNumber.startsWith("015"))
                    {
                        phoneNumber.setError("Phone number is incorrect");
                        errorFlag = true;

                    }
                }
            }

        }


        if (imageUri == null)
        {
            Toast.makeText(getApplicationContext(), "Profile picture required", Toast.LENGTH_LONG).show();
            errorFlag = true;
        }

        if (errorFlag)
        {
            return;
        }

        else
        {
            reference = FirebaseDatabase.getInstance().getReference("Seekers").child(userID);
            reference.child("id").setValue(Id);
            reference.child("userName").setValue(UserName);
            reference.child("phoneNumber").setValue(PhoneNumber);
            uploadPic();

            Intent intent = new Intent(SeekerEditProfileActivity.this,SeekerMainHomeActivity.class);
            startActivity(intent);

        }




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
                            seeker.setImageBitmap(bitmap[0]);
                            imageUri = bitmapToUriConverter(bitmap[0]);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    bitmap[0] = BitmapFactory.decodeFile("defaultProfilePic.jpeg");
                    profilePic.setImageBitmap(bitmap[0]);
                    seeker.setImageBitmap(bitmap[0]);
                    imageUri = bitmapToUriConverter(bitmap[0]);
                }
            });

        } catch (Exception e) {
            bitmap[0] = BitmapFactory.decodeFile("app/defaultProfilePic.jpeg");
            profilePic.setImageBitmap(bitmap[0]);
            seeker.setImageBitmap(bitmap[0]);
            imageUri = bitmapToUriConverter(bitmap[0]);
        }

    }

    public Uri bitmapToUriConverter(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(SeekerEditProfileActivity.this.getContentResolver(), inImage, File.separator + "IMG_" + ".png", null);
        return Uri.parse(path);
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
                        FaceDetection model = FaceDetection.newInstance(SeekerEditProfileActivity.this);

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
                        //Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
                    }
                });
    }

}