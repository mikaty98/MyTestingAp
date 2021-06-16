package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mytestingapp.Classes.GlobalRequest;
import com.example.mytestingapp.Classes.LocalRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SeekerGlobalRequestAutoMap extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;

    Button getlocationBtn, confirm, itemImage, itemImageUpload;
    EditText requestTitle,requestDescription,itemName, itemBrandName, itemSize, itemCategory, city,suburb,streetName,streetNumber,buildingName,buildingNumber,floorNumber,apartmentNumber;

    ImageView imageView;


    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;

    LocationManager locationManager;
    String latitude, longitude;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_global_request_auto_map);

        ActivityCompat.requestPermissions(SeekerGlobalRequestAutoMap.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        itemImage = findViewById(R.id.itemImage);
        itemImageUpload = findViewById(R.id.itemImageUpload);
        imageView = findViewById(R.id.imgView);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        getlocationBtn = findViewById(R.id.getLocation);

        requestTitle = findViewById(R.id.requestTitle);
        requestDescription = findViewById(R.id.requestDescription);

        itemName = findViewById(R.id.itemName);
        itemBrandName = findViewById(R.id.itemBrandName);
        itemSize = findViewById(R.id.itemSize);
        itemCategory = findViewById(R.id.itemCategory);


        city = findViewById(R.id.city);
        suburb = findViewById(R.id.suburb);
        streetName = findViewById(R.id.streetName);
        streetNumber = findViewById(R.id.streetNumber);
        buildingName = findViewById(R.id.buildingName);
        buildingNumber = findViewById(R.id.buildingNumber);
        floorNumber = findViewById(R.id.floor);
        apartmentNumber = findViewById(R.id.apartment);
        confirm = findViewById(R.id.confirm);

        itemImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                SelectImage();
            }
        });


        itemImageUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                uploadImage();

            }
        });


        getlocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                //Check gps is enable or not

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //Write Function To enable gps

                    OnGPS();
                } else {
                    //GPS is already On then

                    getLocation();
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = getIntent();
                String text = intent.getStringExtra("seeker email");


                String SeekerEmail = text;
                confirmSeeker(SeekerEmail);

                Intent intent2 = new Intent(SeekerGlobalRequestAutoMap.this, SeekerGlobalRequestWaitingList.class);
                intent2.putExtra("seeker email", SeekerEmail);
                startActivity(intent2);
                finish();
            }
        });

    }


    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }



    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + FirebaseAuth.getInstance().getCurrentUser().getUid()+"item");

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(SeekerGlobalRequestAutoMap.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(SeekerGlobalRequestAutoMap.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }


    private void OnGPS() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    private void getLocation() {


        if (ActivityCompat.checkSelfPermission(SeekerGlobalRequestAutoMap.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SeekerGlobalRequestAutoMap.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {


            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps != null) {
                double lat = LocationGps.getLatitude();
                double longi = LocationGps.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                // showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);


                Geocoder geocoder = new Geocoder(SeekerGlobalRequestAutoMap.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                    Address obj = addresses.get(0);
                    String add = obj.getAddressLine(0);

                    add = add + "\n" + obj.getCountryName();
                    add = add + "\n" + obj.getCountryCode();
                    add = add + "\n" + obj.getAdminArea();
                    add = add + "\n" + obj.getPostalCode();
                    add = add + "\n" + obj.getSubAdminArea();
                    add = add + "\n" + obj.getLocality();
                    add = add + "\n" + obj.getSubThoroughfare();

                    //  showLocationTxt.setText(add);

                    city.setText(obj.getAdminArea());
                    suburb.setText(obj.getLocality() + ", " + obj.getSubAdminArea());

                    String[] one = obj.getAddressLine(0).split(",");

                    if (isProbablyArabic(one[0])) {


                        String[] two = one[0].split("،");

                        String[] three = two[0].split(" ");

                        streetNumber.setText(three[0]);

                        streetName.setText(three[1]);


                        for (int i = 2; i < three.length; i++) {
                            streetName.append(" ");
                            streetName.append(three[i]);

                        }


                    } else {
                        String[] two = one[0].split(",");

                        String[] three = two[0].split(" ");

                        streetNumber.setText(three[0]);

                        streetName.setText(three[1]);


                        for (int i = 2; i < three.length; i++) {
                            streetName.append(" ");
                            streetName.append(three[i]);

                        }


                    }


                } catch (IOException e) {

                }


            } else if (LocationNetwork != null) {
                double lat = LocationNetwork.getLatitude();
                double longi = LocationNetwork.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                //  showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);


                Geocoder geocoder = new Geocoder(SeekerGlobalRequestAutoMap.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                    Address obj = addresses.get(0);
                    String add = obj.getAddressLine(0);

                    add = add + "\n" + obj.getCountryName();
                    add = add + "\n" + obj.getCountryCode();
                    add = add + "\n" + obj.getAdminArea();
                    add = add + "\n" + obj.getPostalCode();
                    add = add + "\n" + obj.getSubAdminArea();
                    add = add + "\n" + obj.getLocality();
                    add = add + "\n" + obj.getSubThoroughfare();

                    // showLocationTxt.setText(add);

                    city.setText(obj.getAdminArea());
                    suburb.setText(obj.getLocality() + ", " + obj.getSubAdminArea());

                    String[] one = obj.getAddressLine(0).split(",");

                    if (isProbablyArabic(one[0])) {


                        String[] two = one[0].split("،");

                        String[] three = two[0].split(" ");

                        streetNumber.setText(three[0]);

                        streetName.setText(three[1]);


                        for (int i = 2; i < three.length; i++) {
                            streetName.append(" ");
                            streetName.append(three[i]);

                        }


                    } else {
                        String[] two = one[0].split(",");

                        String[] three = two[0].split(" ");

                        streetNumber.setText(three[0]);

                        streetName.setText(three[1]);


                        for (int i = 2; i < three.length; i++) {
                            streetName.append(" ");
                            streetName.append(three[i]);

                        }


                    }

                } catch (IOException e) {

                }


            } else if (LocationPassive != null) {
                double lat = LocationPassive.getLatitude();
                double longi = LocationPassive.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                //  showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);


                Geocoder geocoder = new Geocoder(SeekerGlobalRequestAutoMap.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                    Address obj = addresses.get(0);
                    String add = obj.getAddressLine(0);

                    add = add + "\n" + obj.getCountryName();
                    add = add + "\n" + obj.getCountryCode();
                    add = add + "\n" + obj.getAdminArea();
                    add = add + "\n" + obj.getPostalCode();
                    add = add + "\n" + obj.getSubAdminArea();
                    add = add + "\n" + obj.getLocality();
                    add = add + "\n" + obj.getSubThoroughfare();


                    //  showLocationTxt.setText(add);

                    city.setText(obj.getAdminArea());
                    suburb.setText(obj.getLocality() + ", " + obj.getSubAdminArea());

                    String[] one = obj.getAddressLine(0).split(",");

                    if (isProbablyArabic(one[0])) {


                        String[] two = one[0].split("،");

                        String[] three = two[0].split(" ");

                        streetNumber.setText(three[0]);

                        streetName.setText(three[1]);


                        for (int i = 2; i < three.length; i++) {
                            streetName.append(" ");
                            streetName.append(three[i]);

                        }


                    } else {
                        String[] two = one[0].split(",");

                        String[] three = two[0].split(" ");

                        streetNumber.setText(three[0]);

                        streetName.setText(three[1]);


                        for (int i = 2; i < three.length; i++) {
                            streetName.append(" ");
                            streetName.append(three[i]);

                        }


                    }

                } catch (IOException e) {

                }


            } else {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

        }

    }


    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length(); ) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }


    private void confirmSeeker(String SeekerEmail)
    {

        String RequestTitle = requestTitle.getText().toString().trim();
        String RequestDes = requestDescription.getText().toString().trim();
        String City = city.getText().toString().trim();
        String Suburb = suburb.getText().toString().trim();
        String StreetName = streetName.getText().toString().trim();
        String StreetNumber = streetNumber.getText().toString().trim();
        String BuildingName = buildingName.getText().toString().trim();
        String BuildingNumber = buildingNumber.getText().toString().trim();
        String FloorNumber = floorNumber.getText().toString().trim();
        String ApartmentNumber = apartmentNumber.getText().toString().trim();
        String itemName1 = itemName.getText().toString().trim();
        String itemBrandName1 = itemBrandName.getText().toString().trim();
        String itemSize1 = itemSize.getText().toString().trim();
        String itemCategory1 = itemCategory.getText().toString().trim();




        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("GlobalRequests");

        GlobalRequest g = new GlobalRequest(RequestTitle,RequestDes, itemCategory1, itemName1, itemSize1, itemBrandName1, City,Suburb,StreetName,StreetNumber.toString(),BuildingName,BuildingNumber.toString(),FloorNumber.toString(),ApartmentNumber.toString(),SeekerEmail
                , FirebaseAuth.getInstance().getUid());


        reference.child(RequestTitle).setValue(g);


        //startActivity(new Intent(getApplicationContext(), SeekerLocalRequestWaitingList.class));



    }



}