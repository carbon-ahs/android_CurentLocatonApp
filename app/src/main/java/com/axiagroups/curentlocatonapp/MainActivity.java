package com.axiagroups.curentlocatonapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private int LOCATION_REQUEST_CODE = 101;
    private double lati = 24.3454523d;
    private double longi = 10.123450d;

    private String syncTime, address, coordinate;
    private Button syncBtn;


    private TextView helloTV, addressTv, coordinateTv, syncTimeTv;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        helloTV = findViewById(R.id.helloTV);
        syncTimeTv = findViewById(R.id.syncTimeTv);
        addressTv = findViewById(R.id.addressTv);
        coordinateTv = findViewById(R.id.coordinateTv);
        syncBtn = findViewById(R.id.syncBtn);

        syncTime = String.valueOf(Calendar.getInstance().getTime());
        address = "Sync Failed, try again";
        coordinate = "Sync Failed, try again";





        dynamicPermissionCheck();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        @SuppressLint("MissingPermission") Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);// fine location
        // locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); // coars location
//        Log.d("TAG", "onCreate: "+ locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));

        if (lastKnownLocation != null) {
            coordinate = lastKnownLocation.getLatitude() + ", " + lastKnownLocation.getLongitude();
            lati = lastKnownLocation.getLatitude();
            longi = lastKnownLocation.getLongitude();
        }



        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocation(lati, longi, 1);
            address = addressList.get(0).getAddressLine(0) ; //+ ", "  + addressList.get(0).getLocality();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncTimeTv.setText(syncTime);
                addressTv.setText(address);
                coordinateTv.setText(coordinate);
            }
        });

    }

    private void dynamicPermissionCheck() {
        int fineLocationPermission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);

        if(coarseLocationPermission != PackageManager.PERMISSION_GRANTED
                && fineLocationPermission != PackageManager.PERMISSION_GRANTED){
            String[] permissionList = new String[2];
            permissionList[0] = Manifest.permission.ACCESS_COARSE_LOCATION;
            permissionList[1] = Manifest.permission.ACCESS_FINE_LOCATION;
            requestPermissions(permissionList, LOCATION_REQUEST_CODE);
        }
    }

}