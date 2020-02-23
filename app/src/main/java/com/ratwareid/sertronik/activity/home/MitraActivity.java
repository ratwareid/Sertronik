package com.ratwareid.sertronik.activity.home;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.activity.auth.PhoneAuthActivity;
import com.ratwareid.sertronik.activity.register.RegisterActivity;
import com.ratwareid.sertronik.helper.UniversalKey;
import com.ratwareid.sertronik.model.Mitradata;
import com.ratwareid.sertronik.model.Userdata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MitraActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, GoogleMap.OnMyLocationButtonClickListener {

    private SupportMapFragment mapFragment;
    private GoogleApiClient mGoogleApiClient;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;
    private GoogleMap map;
    private double latitude;
    private double longitude;
    private EditText inputLongitude,inputLatitude,inputNamaToko,inputAlamatToko;
    private int REQUEST_ALAMAT = 101;
    private MarkerOptions markerAlamat;
    private String prevName,prevEmail,prevPhone;
    private EditText inputFullname,inputEmail,inputNoTelephone;
    private Button btnSimpan;
    private DatabaseReference databaseMitra;
    private DatabaseReference databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mitra);
        initWidget();
        getAllData();
    }

    private void initWidget() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        buildGoogleApiClient();
        mapFragment.getMapAsync(this);
        inputLatitude = findViewById(R.id.inputLatitude);
        inputLongitude = findViewById(R.id.inputLongitude);
        inputAlamatToko = findViewById(R.id.inputAlamatToko);
        inputNamaToko = findViewById(R.id.inputNamaToko);
        inputAlamatToko.setOnClickListener(this);
        inputFullname = findViewById(R.id.inputFullName);
        inputEmail = findViewById(R.id.inputEmail);
        inputNoTelephone = findViewById(R.id.inputPhoneNumber);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(this);
        Places.initialize(getApplicationContext(), UniversalKey.API_KEY);
        databaseUser = FirebaseDatabase.getInstance().getReference(UniversalKey.USERDATA_PATH);
        databaseMitra = FirebaseDatabase.getInstance().getReference(UniversalKey.MITRADATA_PATH);
    }

    private void getAllData(){
        prevEmail = getIntent().getStringExtra("email");
        prevName = getIntent().getStringExtra("fullname");
        prevPhone = getIntent().getStringExtra("nomortelephone");

        inputFullname.setText(prevName);
        inputNoTelephone.setText(prevPhone);
        inputEmail.setText(prevEmail);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    @Override
    public void onLocationChanged(Location location) { }

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

    private void changeCurrentLocation(double latitude,double longitude){
        //Get Current Full Address using latlang
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            Address fulladdress = geocoder.getFromLocation(latitude, longitude, 1).get(0);
            inputAlamatToko.setText(fulladdress.getAddressLine(0));
            inputLatitude.setText(String.valueOf(latitude));
            inputLongitude.setText(String.valueOf(longitude));

            LatLng latLng = new LatLng(latitude, longitude);
            map.clear();
            markerAlamat = new MarkerOptions();
            markerAlamat.position(latLng);
            markerAlamat.title("Lokasi Kamu Disini");
            markerAlamat.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = map.addMarker(markerAlamat);
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

    @Override
    public void onClick(View view) {
        if (view.equals(inputAlamatToko)){
            showPlaceAutoComplete(REQUEST_ALAMAT);
        }
        if (view.equals(btnSimpan)){
            daftarMitra();
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
            if(requestCode == REQUEST_ALAMAT){
                //inputAlamatToko.setText(place.getAddress());
                longitude = place.getLatLng().longitude;
                latitude = place.getLatLng().latitude;
                inputLongitude.setText(String.valueOf(longitude));
                inputLatitude.setText(String.valueOf(latitude));

                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
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
    public boolean onMyLocationButtonClick() {
        if (map != null && map.getMyLocation() != null){
            longitude = map.getMyLocation().getLongitude();
            latitude = map.getMyLocation().getLatitude();
            changeCurrentLocation(latitude,longitude);
        }
        return false;
    }

    private void daftarMitra(){
        Mitradata mitra = new Mitradata(inputNamaToko.getText().toString(),inputAlamatToko.getText().toString(),
                inputLatitude.getText().toString(),inputLongitude.getText().toString());
        String mitraID = databaseMitra.push().getKey();
        databaseUser.child(prevPhone).child("mitraID").setValue(mitraID);
        databaseMitra.child(mitraID).setValue(mitra);
        finish();
    }
}
