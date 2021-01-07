package com.example.mytestingapp;


import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.GmsClientEventManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadImage extends AppCompatActivity{

    Button ch,up;
    ImageView img;
    StorageReference mStorageRef;
    public Uri imguri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mStorageRef= FirebaseStorage.getInstance().getReference( "images");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ch=(Button)findViewById(R.id.choosePicture);
        up=(Button)findViewById(R.id.buttonLoadPicture);
        img=(ImageView)findViewById(R.id.imgView);
        ch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Filechooser();
            }

        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUploader();
            }
        });
    }

    private String getExtension(Uri uri) {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return MimeTypeMap.getFileExtensionFromUrl(cr.getType(uri));
    }

    private void FileUploader(){
        StorageReference Ref=mStorageRef.child(System.currentTimeMillis()+"."+getExtension(imguri));

        Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(UploadImage.this,"image uploaded succesdully",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    private void Filechooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imguri=data.getData();
        }
    }

}
