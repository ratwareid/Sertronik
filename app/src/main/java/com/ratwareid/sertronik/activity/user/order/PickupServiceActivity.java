package com.ratwareid.sertronik.activity.user.order;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.activity.home.HomeActivity;
import com.ratwareid.sertronik.helper.UniversalKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PickupServiceActivity extends AppCompatActivity implements
        View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMyLocationButtonClickListener {

    private EditText inputNamaBarang,inputBrandBarang,inputUkuranBarang,inputKerusakan,inputAlamatPenjemputan,
                        inputLatitude,inputLongitude;
    private Button btnNext;
    private GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private LocationRequest mLocationRequest;
    private double latitude;
    private double longitude;
    private int REQUEST_ALAMAT_JEMPUT = 104;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_service);
        initialize();
    }

    public void initialize(){
        inputNamaBarang = findViewById(R.id.inputNamaBarang);
        inputBrandBarang = findViewById(R.id.inputBrandBarang);
        inputUkuranBarang = findViewById(R.id.inputUkuranBarang);
        inputKerusakan = findViewById(R.id.inputKerusakan);
        inputAlamatPenjemputan = findViewById(R.id.inputAlamatPenjemputan);
        inputAlamatPenjemputan.setOnClickListener(this);
        inputLatitude = findViewById(R.id.inputLatitude);
        inputLongitude = findViewById(R.id.inputLongitude);
        btnNext = findViewById(R.id.btnNextOrder);
        btnNext.setOnClickListener(this);

        inputNamaBarang.setText(getIntent().getStringExtra("categoryName"));
        Places.initialize(getApplicationContext(), UniversalKey.API_KEY);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        buildGoogleApiClient();
        mapFragment.getMapAsync(this);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnNext)){
            startActivity(new Intent(this, ChooseTeknisiActivity.class)
                .putExtra("namabarang",inputNamaBarang.getText().toString())
                .putExtra("brandbarang",inputBrandBarang.getText().toString())
                .putExtra("ukuranbarang",inputUkuranBarang.getText().toString())
                .putExtra("kerusakanbarang",inputKerusakan.getText().toString())
                .putExtra("alamatpickup",inputAlamatPenjemputan.getText().toString())
                .putExtra("latitude",inputLatitude.getText().toString())
                .putExtra("longitude",inputLongitude.getText().toString())
            );
            finish();
        }
        if (view.equals(inputAlamatPenjemputan)){
            showPlaceAutoComplete(REQUEST_ALAMAT_JEMPUT);
        }
    }

    private void showPlaceAutoComplete(int REQUEST_CODE) {
        List<Place.Field> placeFields = new ArrayList<>(Arrays.asList(Place.Field.values()));
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596));
        Intent autocompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, placeFields)
                .setLocationBias(bounds)
                .build(this);
        startActivityForResult(autocompleteIntent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            if(requestCode == REQUEST_ALAMAT_JEMPUT){
                //inputAlamatToko.setText(place.getAddress());
                longitude = place.getLatLng().longitude;
                latitude = place.getLatLng().latitude;
                inputLongitude.setText(String.valueOf(longitude));
                inputLatitude.setText(String.valueOf(latitude));
                changeCurrentLocation(latitude,longitude);
            }
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            // TODO: Handle the error.
            Status status = Autocomplete.getStatusFromIntent(data);
            Log.d("ERRORMAPS",status.getStatusMessage());
        } else if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        mGoogleApiClient.connect();

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setOnMyLocationButtonClickListener(this);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
            }
        } else {
            map.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (map != null && map.getMyLocation() != null){
            longitude = map.getMyLocation().getLongitude();
            latitude = map.getMyLocation().getLatitude();
            changeCurrentLocation(latitude,longitude);
        }
        return false;
    }

    private void changeCurrentLocation(double latitude,double longitude){
        //Get Current Full Address using latlang
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            Address fulladdress = geocoder.getFromLocation(latitude, longitude, 1).get(0);
            inputAlamatPenjemputan.setText(fulladdress.getAddressLine(0));
            inputLatitude.setText(String.valueOf(latitude));
            inputLongitude.setText(String.valueOf(longitude));

            LatLng latLng = new LatLng(latitude, longitude);
            map.clear();
            MarkerOptions markerAlamat = new MarkerOptions();
            markerAlamat.position(latLng);
            markerAlamat.title("Lokasi Kamu Disini");
            markerAlamat.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            map.addMarker(markerAlamat);
            //move map camera
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            LatLngBounds.Builder latLongBuilder = new LatLngBounds.Builder();
            latLongBuilder.include(latLng);
            LatLngBounds bounds = latLongBuilder.build();

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int paddingMap = (int) (width * 0.2); //jarak dari
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, paddingMap);
            map.animateCamera(cu);

            //stop location updates
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                Log.d("onLocationChanged", "Removing Location Updates");
            }
            Log.d("onLocationChanged", "Exit");
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
