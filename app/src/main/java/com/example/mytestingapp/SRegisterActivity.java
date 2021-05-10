package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.example.mytestingapp.Classes.Provider;
import com.example.mytestingapp.Classes.Seeker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;


public class SRegisterActivity extends AppCompatActivity {

    private EditText userName,gender,age,phoneNumber,id,email,password,confirmPassword;
    private Button registerbtn;
    private TextView loginbtn,provider;

    private CircleImageView profilePic;
    public Uri imageUri;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private FirebaseStorage storage;
    private StorageReference ref;
    private FirebaseAuth auth;
    private String userID;




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
        auth = FirebaseAuth.getInstance();



        profilePic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                choosePicture();
               // finish();
            }


        });


        loginbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startActivity(new Intent(getApplicationContext(), SLoginActivity.class));
                //finish();
            }


        });

        provider.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startActivity(new Intent(getApplicationContext(), PRegisterActivity.class));
                //finish();
            }


        });

        registerbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                register();

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

        }
    }

    private void uploadPic() {

        StorageReference riversRef = ref.child("images/"+userID);

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

    private void register(){

        String Id = id.getText().toString().trim();
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

        String Email = email.getText().toString().trim();
        String PhoneNumber = phoneNumber.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String ConPassword = confirmPassword.getText().toString().trim();

        boolean errorFlag = false;

        if (TextUtils.isEmpty(Id) || Id.length() != 14) {
            id.setError("*");
            errorFlag = true;
        }

        if (TextUtils.isEmpty(UserName)) {
            userName.setError("*");
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

        if (Age == null){
            age.setError("*");
            errorFlag = true;
        }

        else if (Age < 18 || Age > 100){
            age.setError("Inappropriate age");
            errorFlag = true;
        }

        if (TextUtils.isEmpty(PhoneNumber) || PhoneNumber.length() != 11){
            phoneNumber.setError("*");
            errorFlag = true;
        }

        if (TextUtils.isEmpty(Email)) {
            email.setError("*");
            errorFlag = true;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("wrong format");
            errorFlag = true;
        }
        if (TextUtils.isEmpty(Password)) {
            password.setError("*");
            errorFlag = true;
        }
        if (Password.length()<6){
            password.setError("password must be more than 6 characters");
        }

        if (TextUtils.isEmpty(ConPassword)) {
            confirmPassword.setError("*");
            errorFlag = true;
        }
        if (!TextUtils.equals(Password, ConPassword)) {
            password.setText("");
            confirmPassword.setText("");
            confirmPassword.setError("Passwords don't match");
            errorFlag = true;
        }

        if (imageUri == null){
            Toast.makeText(getApplicationContext(),"Profile picture required", Toast.LENGTH_LONG).show();
            errorFlag = true;
        }

        if (errorFlag) {
            return;
        } else {
            rootNode = FirebaseDatabase.getInstance();

            Seeker s = new Seeker(UserName,Gender,Age.toString(),Id,PhoneNumber,Email,Password);

            auth.fetchSignInMethodsForEmail(s.getEmail()).addOnCompleteListener(SRegisterActivity.this,new OnCompleteListener<SignInMethodQueryResult>() {  //Checking if email already exists or not
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    boolean check = !task.getResult().getSignInMethods().isEmpty();
                    if (!check){ //email not found
                        auth.createUserWithEmailAndPassword(s.getEmail(),s.getPassword()).addOnCompleteListener(SRegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(SRegisterActivity.this,"Registration successful",Toast.LENGTH_SHORT).show();
                                    userID = auth.getCurrentUser().getUid();
                                    auth.updateCurrentUser(task.getResult().getUser());
                                    s.setUserID(userID);

                                    reference = rootNode.getReference().child("Seekers");
                                    reference.child(userID).setValue(s);

                                    uploadPic();
                                    startActivity(new Intent(getApplicationContext(), SLoginActivity.class));
                                    finish();
                                }
                            }
                        });

                    }
                    else{ //email already exists
                        auth.signInWithEmailAndPassword(s.getEmail(),s.getPassword()).addOnCompleteListener(SRegisterActivity.this,new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){  //if user registered with the same email and password
                                    userID = task.getResult().getUser().getUid();
                                    auth.updateCurrentUser(task.getResult().getUser());
                                    s.setUserID(userID);
                                    reference = rootNode.getReference().child("Seekers");
                                    Query checkuser = reference.orderByChild("userID").equalTo(userID);
                                    checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                Toast.makeText(SRegisterActivity.this,"User already registered as a Seeker",Toast.LENGTH_LONG).show();
                                            }
                                            else{
                                                reference.child(userID).setValue(s);

                                                //uploadPic();  upload function commented because user has same id therefore same picture //TBD
                                                startActivity(new Intent(getApplicationContext(), SLoginActivity.class));
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                                else{ // if user registered with same email but different password

                                    Toast.makeText(SRegisterActivity.this,"The email address you entered is associated with a different password. Change the email address or enter the same password.",Toast.LENGTH_LONG).show();
                                    //registerSuccess = false;
                                }
                            }
                        });

                    }
                }
            });

        }

    }


}

