package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SRegisterActivity extends AppCompatActivity {

    private EditText userName,gender,age,phoneNumber,id,email,password,confirmPassword;
    private Button registerbtn;
    private TextView loginbtn,provider;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

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

        loginbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startActivity(new Intent(getApplicationContext(), SLoginActivity.class));
            }


        });

        provider.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startActivity(new Intent(getApplicationContext(), PRegisterActivity.class));
            }


        });

        registerbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (register()){
                    startActivity(new Intent(getApplicationContext(), SLoginActivity.class));
                }

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
            id.setError("*");
            errorFlag = true;
        }

        if (TextUtils.isEmpty(Gender)){
            id.setError("*");
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
        if (TextUtils.isEmpty(Password)){
            email.setError("*");
            errorFlag = true;
        }
        if (TextUtils.isEmpty(ConPassword)){
            email.setError("*");
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

