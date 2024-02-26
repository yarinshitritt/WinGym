package com.example.newwimgym;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CalorieIntake extends AppCompatActivity {
    Button AddCalories,DecreaseCalories, ResetCurrentCalories,ciinfo;
    EditText AddCaloriesAmount;
    TextView TotalCaloriesToConsume;
    TextView CurrentCalorieIntake;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_intake);
        db = FirebaseFirestore.getInstance();
        AddCalories = (Button) findViewById(R.id.CalorieIntakePlus);
        DecreaseCalories = (Button) findViewById(R.id.CalorieIntakeMinus);
        AddCaloriesAmount = (EditText) findViewById(R.id.CalorieIntakeAdd);
        CurrentCalorieIntake = (TextView) findViewById(R.id.CurrentCalorireIntakeTv);
        ResetCurrentCalories = (Button) findViewById(R.id.CalorieIntakeResetValues);
        TotalCaloriesToConsume = (TextView) findViewById(R.id.Total_Calories_To_Consume);
        Intent intent = getIntent();


        String username = intent.getStringExtra("username");



        db.collection("users")
                .whereEqualTo("username", username.toLowerCase())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Get the user document
                    DocumentSnapshot userDoc = queryDocumentSnapshots.getDocuments().get(0);

                    String calorieGoal = userDoc.getString("calorieGoal") != null ? userDoc.getString("calorieGoal") : null;
                    String CurrentCalorie = userDoc.getString("CurrentCalorieIntake") != null ? userDoc.getString("CurrentCalorieIntake") : null;

                    // Set the UI components to display the user's data
                    if (calorieGoal != null) {
                        TotalCaloriesToConsume.setText(calorieGoal);
                    } else {
                        TotalCaloriesToConsume.setText("");
                    }
                    if (calorieGoal != null) {
                        CurrentCalorieIntake.setText(CurrentCalorie);
                    } else {
                        CurrentCalorieIntake.setText("");
                    }


                })
                .addOnFailureListener(e -> Log.e(TAG, "Error getting user document.", e));
    }
    public void onClick(View v) {
        if (v == AddCalories) {
            if (AddCaloriesAmount.getText().toString().length() > 0) {
                int caloriesToAdd = Integer.parseInt(AddCaloriesAmount.getText().toString());
                int currentCalories = Integer.parseInt(CurrentCalorieIntake.getText().toString());
                int newcalorie = currentCalories + caloriesToAdd;
                CurrentCalorieIntake.setText(Integer.toString(newcalorie));
                saveCurrentCalorieIntakeToFirebase(newcalorie);
            }
        } else if (v == DecreaseCalories) {
            if (AddCaloriesAmount.getText().toString().length() > 0) {
                int caloriesToDec = Integer.parseInt(AddCaloriesAmount.getText().toString());
                int currentCalories = Integer.parseInt(CurrentCalorieIntake.getText().toString());
                int newcalorie = currentCalories - caloriesToDec;
                CurrentCalorieIntake.setText(Integer.toString(newcalorie));
                saveCurrentCalorieIntakeToFirebase(newcalorie);
            }
        } else if (v == ResetCurrentCalories) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(2000);
            CurrentCalorieIntake.setText("0");
            saveCurrentCalorieIntakeToFirebase(0);
        }
    }
    //save the current calorie on the firebase and display it on the screen
    private void saveCurrentCalorieIntakeToFirebase(int currentCalorieIntake) {
        String username = getIntent().getStringExtra("username");
        db.collection("users")
                .whereEqualTo("username", username.toLowerCase())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Get the user document
                    DocumentSnapshot userDoc = queryDocumentSnapshots.getDocuments().get(0);

                    // Update the user document with the new calorie intake value
                    userDoc.getReference().update("CurrentCalorieIntake", Integer.toString( currentCalorieIntake))
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "CurrentCalorieIntake updated successfully!"))
                            .addOnFailureListener(e -> Log.e(TAG, "Error updating CurrentCalorieIntake", e));
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error getting user document.", e));
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
                    .setTitle("Calorie Intake")
                    .setMessage("Follow your calorie intake to get to your goal ")
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

