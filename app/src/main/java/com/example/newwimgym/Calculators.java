package com.example.newwimgym;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Calculators extends AppCompatActivity {
    Button bmi,bmr,calorie,CalorieIntakebtncalc;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculators);
        bmi = (Button) findViewById(R.id.bmibtn);
        bmr = (Button) findViewById(R.id.bmrbtn);
        calorie = (Button) findViewById(R.id.caloriebtn);
        CalorieIntakebtncalc = (Button) findViewById(R.id.calorieIntakeBtn);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");



    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bmibtn:
                Intent intent = new Intent(this, BMIcalc.class);
                intent.putExtra("username",username);

                startActivity(intent);
                break;
            case R.id.bmrbtn:
                Intent intent2 = new Intent(this, BMRcalc.class);
                intent2.putExtra("username",username);

                startActivity(intent2);
                break;

            case R.id.caloriebtn:
                Intent intent3 = new Intent(this, CalorieCalc.class);
                startActivity(intent3);
                break;


        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activityies_menu, menu);
        return  true;

    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();

        if(id==R.id.about)
        {


            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Calculators")
                    .setMessage("Check your stats!")
                    .setNegativeButton("Return", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .show();


        }


        else if(R.id.returnbtn==id)
        {

            finish();
            System.exit(0);
        }

        return true;
    }
}