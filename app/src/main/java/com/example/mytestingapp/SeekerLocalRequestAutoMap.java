package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.app.AlertDialog;
import android.content.DialogInterface;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

import com.example.mytestingapp.Classes.LocalRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class SeekerLocalRequestAutoMap extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;

    Button getlocationBtn, confirm;
    EditText requestTitle, requestDescription, city, suburb, streetName, streetNumber, buildingName, buildingNumber, floorNumber, apartmentNumber;

    LocationManager locationManager;
    String latitude, longitude;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private FirebaseStorage storage;
    private StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_local_request_auto_map);


        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        getlocationBtn = findViewById(R.id.getLocation);


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

    }

    private void getLocation() {


        if (ActivityCompat.checkSelfPermission(SeekerLocalRequestAutoMap.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SeekerLocalRequestAutoMap.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {


            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            requestTitle = findViewById(R.id.requestTitle);
            requestDescription = findViewById(R.id.requestDescription);

            confirm = findViewById(R.id.confirm);

            city = findViewById(R.id.city);
            suburb = findViewById(R.id.suburb);
            streetName = findViewById(R.id.streetName);
            streetNumber = findViewById(R.id.streetNumber);
            buildingName = findViewById(R.id.buildingName);
            buildingNumber = findViewById(R.id.buildingNumber);
            floorNumber = findViewById(R.id.floor);
            apartmentNumber = findViewById(R.id.apartment);

            if (LocationGps != null) {
                double lat = LocationGps.getLatitude();
                double longi = LocationGps.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                // showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);


                Geocoder geocoder = new Geocoder(SeekerLocalRequestAutoMap.this, Locale.getDefault());
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


                Geocoder geocoder = new Geocoder(SeekerLocalRequestAutoMap.this, Locale.getDefault());
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


                Geocoder geocoder = new Geocoder(SeekerLocalRequestAutoMap.this, Locale.getDefault());
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


            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = getIntent();
                    String text = intent.getStringExtra(SeekerLocalRequest.TEXT2);

                    String SeekerEmail = text;
                    confirmSeeker(SeekerEmail);

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

    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length(); ) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }

    private void confirmSeeker(String SeekerEmail) {

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

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("LocalRequests");

        LocalRequest l = new LocalRequest(RequestTitle, RequestDes, City, Suburb, StreetName, StreetNumber.toString(), BuildingName, BuildingNumber.toString(), FloorNumber.toString(), ApartmentNumber.toString(), SeekerEmail);

        reference.child(RequestTitle).setValue(l);

        startActivity(new Intent(getApplicationContext(), SeekerLocalRequestWaitingList.class));


    }

}