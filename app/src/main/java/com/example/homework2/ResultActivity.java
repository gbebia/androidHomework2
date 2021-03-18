package com.example.homework2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    ListView listView;
    ArrayList arrayList;
    ArrayAdapter arrayAdapter;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result2);

        listView = findViewById(R.id.listView);

        //Runtime permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ResultActivity.this, new String[]{Manifest.permission.SEND_SMS},1);
        }

        Intent intent = getIntent();
        ArrayList<String> data = intent.getStringArrayListExtra("data");
        arrayList = new ArrayList();
        for (String object: data) {
            //Toast.makeText(this, object, Toast.LENGTH_LONG).show();
            arrayList.add(object);
        }

        arrayAdapter = new ArrayAdapter(ResultActivity.this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                sharedPreferences = getSharedPreferences("SendSMS", MODE_PRIVATE);

                String message = sharedPreferences.getString("message","empty message");
                String mobile = sharedPreferences.getString("mobile","empty mobile");

                sendSMS(mobile, message);

                //Toast.makeText(ResultActivity.this, message+" "+mobile, Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void sendSMS(String mobile, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(mobile,null,message,null,null);
        //Toast.makeText(this, "SMS Sent", Toast.LENGTH_SHORT).show();

        NotificationManager notificationManager = (NotificationManager) ResultActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "send sms-01";
        String channelName = "Send SMS";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ResultActivity.this, channelId)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle("Successfully sent")
                .setContentText( "Mobile: " + mobile + " ; " + "Text: " + message);

        notificationManager.notify(notificationId, mBuilder.build());
    }
}