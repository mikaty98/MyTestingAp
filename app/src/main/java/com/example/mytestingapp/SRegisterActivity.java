package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.example.mytestingapp.Classes.Seeker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class SRegisterActivity extends AppCompatActivity {

    private EditText userName,gender,age,phoneNumber,id,email,password,confirmPassword;
    private Button registerbtn;
    private TextView loginbtn,provider;

    private ImageView profilePic;
    public Uri imageUri;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private FirebaseStorage storage;
    private StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_register);

        id = findViewById(R.id.id_reg);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        userName = findViewById(R.id.username_reg);
        gender = findViewById(R.id.gender_reg);
        age = findViewById(R.id.age_reg);
        phoneNumber = findViewById(R.id.phone_number_reg);
        confirmPassword = findViewById(R.id.editTextTextPassword2);
        registerbtn = findViewById(R.id.RegisterButton);
        loginbtn = findViewById(R.id.textView);
        provider = findViewById(R.id.provider);

        profilePic = findViewById(R.id.profilePic);

        storage = FirebaseStorage.getInstance();
        ref = storage.getReference();

        profilePic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                choosePicture();
                finish();
            }


        });


        loginbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startActivity(new Intent(getApplicationContext(), SLoginActivity.class));
                finish();
            }


        });

        provider.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startActivity(new Intent(getApplicationContext(), PRegisterActivity.class));
                finish();
            }


        });

        registerbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (register()){
                    startActivity(new Intent(getApplicationContext(), SeekerHome.class));
                    finish();
                }

            }
        });


    }
    private void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            uploadPic();
        }
    }

    private void uploadPic() {

        StorageReference riversRef = ref.child("images/"+id.getText().toString().trim());

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Snackbar.make(findViewById(android.R.id.content),"Image Uploaded.",Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(),"Failed to upload", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean register(){
        String Id = id.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String ConPassword = confirmPassword.getText().toString().trim();
        String UserName = userName.getText().toString().trim();
        String Gender = gender.getText().toString().trim();
        String Agetxt = age.getText().toString().trim();
        Integer Age;
        if (Agetxt.equals(""))
        {
            Age = null;
        }
        else{
            Age = Integer.parseInt(Agetxt);
        }
        String Pnumber = phoneNumber.getText().toString().trim();

        boolean errorFlag = false;

        if (TextUtils.isEmpty(UserName)){
            userName.setError("*");
            errorFlag = true;
        }

        if (TextUtils.isEmpty(Gender)){
            gender.setError("*");
            errorFlag = true;
        }

        if (!(Gender.equals("Male") || Gender.equals("male") || Gender.equals("Female") || Gender.equals("female"))){
            gender.setError("Invalid input");
            errorFlag = true;
        }

        if (Age == null){
            age.setError("*");
            errorFlag = true;
        }

        if (Age < 18 || Age > 100){
            age.setError("Age is Innapropriate");
            errorFlag = true;
        }

        if (TextUtils.isEmpty(Id) || Id.length() != 14){
            id.setError("*");
            errorFlag = true;
        }

        if (TextUtils.isEmpty(Pnumber) || Pnumber.length() <= 10 || Pnumber.length() > 11 || !Pnumber.startsWith("01")){
            phoneNumber.setError("*");
            errorFlag = true;
        }

        if (TextUtils.isEmpty(Email)){
            email.setError("*");
            errorFlag = true;
        }

        if (!Email.contains("@") || !Email.contains(".com")){
            email.setError("wrong format");
            errorFlag = true;
        }
        if (TextUtils.isEmpty(Password)){
            password.setError("*");
            errorFlag = true;
        }
        if (TextUtils.isEmpty(ConPassword)){
            confirmPassword.setError("*");
            errorFlag = true;
        }
        if (!TextUtils.equals(Password,ConPassword)){
            password.setText("");
            confirmPassword.setText("");
            confirmPassword.setError("Passwords don't match");
            errorFlag = true;
        }

        if (errorFlag){

            return false;
        }
        else {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference().child("Seekers");
            Seeker s = new Seeker(UserName,Gender,Age.toString(),Id,Pnumber,Email,Password);
            try {
                String[] parts = Email.split(".com",2);
                String uniqueEmail = parts[0];
                reference.child(uniqueEmail).setValue(s);
            }
            catch(Exception e)
            {
                System.out.println("email not split");
            }



            return true;
        }
        


    }


}

