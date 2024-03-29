package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.example.mytestingapp.Classes.Provider;
import com.example.mytestingapp.ml.FaceDetection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.hdodenhof.circleimageview.CircleImageView;

public class PRegisterActivity extends AppCompatActivity {

    private EditText userName,jobDesc,gender,birthDay, birthMonth, birthYear,id,phoneNumber,email,password,confirmPassword;
    private Button registerbtn;
    private TextView loginbtn,seeker;

    private CircleImageView profilePic;

    public Uri imageUri;
    private Bitmap img;


    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private FirebaseStorage storage;
    private StorageReference ref;

    private FirebaseAuth auth;

    private String userID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_register);

        id = findViewById(R.id.id_reg);
        userName = findViewById(R.id.username_reg);
        gender = findViewById(R.id.gender_reg);

        birthDay = findViewById(R.id.birthDay);
        birthMonth = findViewById(R.id.birthMonth);
        birthYear = findViewById(R.id.birthYear);

        phoneNumber = findViewById(R.id.phone_number_reg);
        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.password_reg);
        confirmPassword = findViewById(R.id.confirm_password_reg);
        jobDesc = findViewById(R.id.job_description_reg);
        registerbtn = findViewById(R.id.button_reg);
        loginbtn = findViewById(R.id.sign_in);
        seeker = findViewById(R.id.seeker);

        profilePic = findViewById(R.id.profilePic);


        storage = FirebaseStorage.getInstance();
        ref = storage.getReference();
        auth = FirebaseAuth.getInstance();



        profilePic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                choosePicture();

            }


        });

        loginbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startActivity(new Intent(getApplicationContext(), PLoginActivity.class));
            }

        });

        seeker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startActivity(new Intent(getApplicationContext(), SRegisterActivity.class));
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
        startActivityForResult(intent, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        boolean containingAFace = false;

        if (requestCode == 100 && resultCode == RESULT_OK && data!=null && data.getData()!=null)
        {

            Uri uri = data.getData();
            try
            {
                img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                Bitmap resized = Bitmap.createScaledBitmap(img, 224, 224, true);

                try {
                    FaceDetection model = FaceDetection.newInstance(PRegisterActivity.this);

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



    private void register() {
        String Id = id.getText().toString().trim();
        String UserName = userName.getText().toString().trim();
        String JobDesc = jobDesc.getText().toString().trim();
        String Gender = gender.getText().toString().trim();

        String BirthDay =  birthDay.getText().toString().trim();


        String BirthMonth = birthMonth.getText().toString().trim();

        String BirthYear = birthYear.getText().toString().trim();

        String Email = email.getText().toString().trim();
        String PhoneNumber = phoneNumber.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String ConPassword = confirmPassword.getText().toString().trim();

        boolean errorFlag = false;


        if (TextUtils.isEmpty(JobDesc))
        {
            jobDesc.setError("Job Description can not be left empty");
            errorFlag = true;
        }

        int flag = 0;
        int flag1 = 0;
        int flag2 = 0;

        if (TextUtils.isEmpty(BirthDay))
        {
            birthDay.setError("This field can not be left empty");
            flag = 1;
            errorFlag = true;
        }

        if(flag == 0)
        {
            if(Integer.valueOf(BirthDay) > 31)
            {
                birthDay.setError("Invalid Input");
                errorFlag = true;
            }

            if(Integer.valueOf(BirthDay) < 1)
            {
                birthDay.setError("Invalid Input");
                errorFlag = true;
            }

        }



        if (TextUtils.isEmpty(BirthMonth))
        {
            birthMonth.setError("This field can not be left empty");
            flag1 = 1;
            errorFlag = true;
        }

        if(flag1 == 0)
        {
            if(Integer.valueOf(BirthMonth) > 12)
            {
                birthMonth.setError("Invalid Input");
                errorFlag = true;
            }

            if(Integer.valueOf(BirthMonth) < 1)
            {
                birthMonth.setError("Invalid Input");
                errorFlag = true;
            }

        }


        if (TextUtils.isEmpty(BirthYear))
        {
            birthYear.setError("ID number can not be left empty");
            flag2 = 1;
            errorFlag = true;
        }

        if(flag2 == 0)
        {
            if(Integer.valueOf(BirthYear) < 1921)
            {
                birthYear.setError("Inappropriate Age");
                errorFlag = true;
            }

            if(Integer.valueOf(BirthDay) < 1)
            {
                birthYear.setError("Invalid Input");
                errorFlag = true;
            }

            if(Integer.valueOf(BirthYear) > 2003)
            {
                birthYear.setError("Inappropriate Age");
                errorFlag = true;
            }

        }


        if (TextUtils.isEmpty(Id))
        {
            id.setError("ID number can not be left empty");
            errorFlag = true;
        }

        if(Id.length() != 14)
        {
            id.setError("Id number is incorrect or incomplete");
            errorFlag = true;
        }

        if (TextUtils.isEmpty(UserName))
        {
            userName.setError("User name can not be left empty");
            errorFlag = true;
        }

        if (TextUtils.isEmpty(Gender))
        {
            gender.setError("Gender can not be left empty");
            errorFlag = true;
        }

        if (!(Gender.equals("Male") || Gender.equals("male") || Gender.equals("Female") || Gender.equals("female")))
        {
            gender.setError("Invalid input");
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

        if (TextUtils.isEmpty(Email))
        {
            email.setError("Email can not be left empty");
            errorFlag = true;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
        {
            email.setError("Wrong email format");
            errorFlag = true;
        }
        if (TextUtils.isEmpty(Password))
        {
            password.setError("Password can not be left empty");
            errorFlag = true;
        }
        if (Password.length()<6)
        {
            password.setError("Password must be more than 6 characters");
        }

        if (TextUtils.isEmpty(ConPassword))
        {
            confirmPassword.setError("This field can not be left empty");
            errorFlag = true;
        }
        if (!TextUtils.equals(Password, ConPassword)) {
            password.setText("");
            confirmPassword.setText("");
            confirmPassword.setError("Passwords don't match");
            errorFlag = true;
        }

        if (imageUri == null)
        {
            Toast.makeText(getApplicationContext(),"Profile picture required", Toast.LENGTH_LONG).show();
            errorFlag = true;
        }

        if (errorFlag) {
            return;
        } else {
            rootNode = FirebaseDatabase.getInstance();

            Provider p = new Provider(UserName,JobDesc,Gender,BirthDay.toString(),BirthMonth.toString(),BirthYear.toString(),Id,PhoneNumber,Email);

            auth.fetchSignInMethodsForEmail(p.getEmail()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {  //Checking id email already exists or not
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    boolean check = !task.getResult().getSignInMethods().isEmpty();
                    if (!check){ //email not found
                        auth.createUserWithEmailAndPassword(p.getEmail(),Password).addOnCompleteListener(PRegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(PRegisterActivity.this,"Registration successful",Toast.LENGTH_SHORT).show();
                                    userID = auth.getCurrentUser().getUid();
                                    auth.updateCurrentUser(task.getResult().getUser());
                                    p.setUserID(userID);

                                    reference = rootNode.getReference().child("Providers");
                                    reference.child(userID).setValue(p);

                                    uploadPic();
                                    startActivity(new Intent(getApplicationContext(), PLoginActivity.class));
                                    finish();                                }
                            }
                        });

                    }
                    else{ //email already exists
                       // Toast.makeText(PRegisterActivity.this,"Email already exists",Toast.LENGTH_SHORT).show();
                        auth.signInWithEmailAndPassword(p.getEmail(),Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){  //if user registered with the same email and password
                                    userID = task.getResult().getUser().getUid();
                                    auth.updateCurrentUser(task.getResult().getUser());
                                    p.setUserID(userID);
                                    reference = rootNode.getReference().child("Providers");
                                    Query checkuser = reference.orderByChild("userID").equalTo(userID);
                                    checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                Toast.makeText(PRegisterActivity.this,"User already registered as a Provider",Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                reference.child(userID).setValue(p);

                                                //uploadPic();  upload function commented because user has same id therefore same picture //TBD
                                                startActivity(new Intent(getApplicationContext(), PLoginActivity.class));
                                                finish();

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                                else{ // if user registered with same email but different password
                                    Toast.makeText(PRegisterActivity.this,"The email address you entered is associated with a different password. Change the email address or enter the same password.",Toast.LENGTH_LONG).show();
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