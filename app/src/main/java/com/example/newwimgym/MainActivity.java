package com.example.newwimgym;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.SensorEventListener;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private Button Login,SignUp,bug;
    private String GuestUserName;
    private SensorManager sensormanager;
    private Sensor temperature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        Login =(Button) findViewById(R.id.LoginBtn);
        SignUp =(Button) findViewById(R.id.SignUpBtn);
        bug = (Button) findViewById(R.id.guestbtn);
        CreateNotificationChannel();
        SetNotification();
        GuestUserName= "guest123";
        sensormanager = (SensorManager)getSystemService(SENSOR_SERVICE);
        temperature= sensormanager.getDefaultSensor(TYPE_AMBIENT_TEMPERATURE);

    }
    @Override
    protected void onResume() {
        super.onResume();
        sensormanager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensormanager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_AMBIENT_TEMPERATURE) {
            return;
        }

        float temperature = event.values[0];

        if (temperature > 40) {
            Toast.makeText(MainActivity.this, "Temperature is over 40 degrees!", Toast.LENGTH_SHORT).show();
        }
    }
   private void SetNotification ()// This method sets up an alarm to send a notification to the user.
   {
       Intent intent = new Intent(MainActivity.this,ReminderBroadCast.class);// get ReminderBroadCast intent
       PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,FLAG_IMMUTABLE);//This creates a pending intent that will be used to trigger the alarm. The getBroadcast() method creates a new broadcast intent that will be received by the ReminderBroadCast class. The FLAG_IMMUTABLE flag ensures that the pending intent cannot be modified.
       AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);//This creates an instance of the AlarmManager system service, which will be used to schedule the alarm.
       long currentTime = System.currentTimeMillis();//This gets the current system time in milliseconds.
       long tensecndsinMillis = 1000*10;
       alarmManager.set(AlarmManager.RTC_WAKEUP,currentTime+tensecndsinMillis,pendingIntent);//This schedules the alarm to go off in 10 seconds from the current time. The RTC_WAKEUP flag ensures that the alarm wakes up the device if it's in sleep mode. The pendingIntent specifies the broadcast receiver that will handle the alarm.
   }
   private void CreateNotificationChannel()//This method creates a notification channel that can be used to send notifications to the user.
   {

       if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)//if the device is running Android Oreo or later, because notification channels were introduced in Android Oreo.
       {
           CharSequence name = "LemubitReminderChannel";//This sets the name of the notification channel.
           String description = "Channel for reminder";//This sets the description of the notification channel.
           int importance = NotificationManager.IMPORTANCE_DEFAULT;//This sets the importance level of the notification channel to default.
           NotificationChannel channel = new NotificationChannel("notifylemubit",name,importance);//This creates a new notification channel object with the specified ID, name, and importance level.
           channel.setDescription(description);//This sets the description of the notification channel.
           NotificationManager notificationManager = getSystemService(NotificationManager.class);//gets an instance of the NotificationManager system service.
           notificationManager.createNotificationChannel(channel);//This creates the notification channel with the specified properties.
       }
   }

    public void onClick(View v) {
        if (v== Login )//transfer to login screen
        {
            Intent intent = new Intent(this, LoginScreen.class);
            startActivity(intent);
        }
        else if (v == SignUp)//transfer to signup screen
        {
            Intent intent2 = new Intent(this, SignUpScreen.class);
            startActivity(intent2);


        }
        else if (v == bug)//transfer to guest screen
        {
            Intent intent3 = new Intent(this, MainScreen.class);
            intent3.putExtra("username",GuestUserName);
            startActivity(intent3);


        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return  true;

    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();

        if(id==R.id.about)
        {
            Toast.makeText(this,"start training with this app", Toast.LENGTH_SHORT).show();


        }


        else if(R.id.ExitAppbtn==id)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit App");
            builder.setMessage("Are you sure you want to close the app?");
            builder.setCancelable(true);
            builder.setPositiveButton("yes", new HandleAlertDialogListener());
            builder.setNegativeButton("no", new HandleAlertDialogListener());
            AlertDialog dialog=builder.create();
            dialog.show();


        }

        return true;
    }
    public  class  HandleAlertDialogListener implements DialogInterface.OnClickListener
    {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            if(which == -1){
                finish();
                System.exit(0);
            }
        }
    }

}