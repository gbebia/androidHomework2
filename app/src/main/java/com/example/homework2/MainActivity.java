package com.example.homework2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    EditText message,mobile, lalo;
    CheckBox location,startAudio,stopAudio;
    LocationManager locationManager;

    ImageView imageView;
    Button cameraBtn,submitBtn;
    Bitmap bitmap;

    MediaPlayer mediaPlayer;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static final int CAMERA_ACTION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        message = findViewById(R.id.message);
        mobile = findViewById(R.id.mobile);
        lalo = findViewById(R.id.latlon);
        location = findViewById(R.id.location);
        imageView = findViewById(R.id.imageView);
        cameraBtn = findViewById(R.id.cameraBtn);
        submitBtn = findViewById(R.id.submitBtn);

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.survivor);

        sharedPreferences = getSharedPreferences("SendSMS", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Runtime permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA
            },101);
        }

        location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    getLocaion();
                }

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = message.getText().toString();
                String destination = mobile.getText().toString();

                editor.putString("message",text);
                editor.putString("mobile",destination);
                editor.commit();

                String sendSmsView = " Get Data From Shared Preferences And Send SMS";

                ArrayList<String> data = new ArrayList<String>();
                data.add(sendSmsView);

                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putStringArrayListExtra("data",(ArrayList)data);
                startActivity(intent);

            }
        });

    }

    @SuppressLint("MissingPermission")
    private void getLocaion(){
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,MainActivity.this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        String address = "";

        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        lalo.setText(address+" : "+location.getLatitude()+" : "+location.getLongitude());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.aboutMenuId:
                Intent intent = new Intent(MainActivity.this, WebSiteActivity.class);
                this.startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void cameraBtn(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,1);
        }
        else{
            Toast.makeText(this, "not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_ACTION_CODE){
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");
            imageView.setImageBitmap(bitmap);
        }

    }

    public void startAudio(View view) {
        mediaPlayer.start();
    }

    public void stopAudio(View view) {
        mediaPlayer.stop();
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.survivor);
    }


}