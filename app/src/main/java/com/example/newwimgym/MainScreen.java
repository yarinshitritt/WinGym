package com.example.newwimgym;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreen extends AppCompatActivity {
    Button DailyTrainingBtn, supbtn,CalculatorsBtn,CalorieIntakebtnmain, RecommendedExercisesBtn,CompareWithFriendsBtn;
    String username ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        username = intent.getStringExtra("username");
        Toast.makeText(this, "Welcome back " + username + "!", Toast.LENGTH_SHORT).show();
        RecommendedExercisesBtn = (Button) findViewById(R.id.recommendedExercisesBtn);
        DailyTrainingBtn= (Button) findViewById(R.id.DailyTrainig);
        CalculatorsBtn= (Button) findViewById(R.id.calculatorsBtn);
        CalorieIntakebtnmain = (Button) findViewById(R.id.calorieIntakeBtn);
        supbtn=(Button) findViewById(R.id.mainmenubtn);
        CompareWithFriendsBtn = (Button) findViewById(R.id.compareWithFriendsBtn);

    }
    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.DailyTrainig:
                Intent intent = new Intent(this, DailyTraining.class);
                intent.putExtra("username",username);
                startActivity(intent);
                break;
            case R.id.calculatorsBtn:
                Intent intent2 = new Intent(this, Calculators.class);
                intent2.putExtra("username",username);
                startActivity(intent2);
                break;
            case R.id.calorieIntakeBtn:
                Intent intent3 = new Intent(this, CalorieIntake.class);
                intent3.putExtra("username",username);
                startActivity(intent3);
                break;
            case R.id.recommendedExercisesBtn:
                Intent intent4 = new Intent(this, RecomendedEX.class);
                startActivity(intent4);
                break;
            case R.id.AboutMeBtn:
                Intent intent5 = new Intent(this, AboutMe.class);
                intent5.putExtra("username",username);
                startActivity(intent5);
                break;
            case R.id.compareWithFriendsBtn:
                Intent intent6 = new Intent(this, CompareWithFriends.class);
                intent6.putExtra("username",username);
                startActivity(intent6);
                break;


        }
    }


}