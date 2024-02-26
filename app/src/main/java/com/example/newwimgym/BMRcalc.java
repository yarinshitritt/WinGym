package com.example.newwimgym;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BMRcalc extends AppCompatActivity {
    public Button ResetValues, CalculateBmr;
    public EditText Weight, Height,Age;
    public TextView BmrReultText;
    public RadioButton Rdmale, Rdfemale;
    double Bmr=0, CurrentWeightKg=0, CurrentHeightCM=0, CurrentAge= 0;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmrcalc);
        db = FirebaseFirestore.getInstance();

        ResetValues = (Button) findViewById(R.id.bmrReCalculate);
        Rdfemale = (RadioButton) findViewById(R.id.radioFemale);
        Rdmale = (RadioButton) findViewById(R.id.radioMale);
        CalculateBmr = (Button) findViewById(R.id.bmrcalculate_btn);
        Weight = (EditText) findViewById(R.id.bmrWeight);
        Height = (EditText) findViewById(R.id.bmrHeight);
        Age =  (EditText) findViewById(R.id.bmrAge);
        BmrReultText = (TextView) findViewById(R.id.bmrValueText);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        db.collection("users")
                .whereEqualTo("username", username.toLowerCase())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Get the user document
                    DocumentSnapshot userDoc = queryDocumentSnapshots.getDocuments().get(0);

                    // Get the user's age, weight, and height (if they exist)
                    String age = userDoc.getString(  "age") != null ? userDoc.getString("age"): null;
                    String weight = userDoc.getString("weight") != null ? userDoc.getString("weight") : null;
                    String height = userDoc.getString("height") != null ? userDoc.getString("height") : null;
                    String calorieGoal = userDoc.getString("calorieGoal") != null ? userDoc.getString("calorieGoal") : null;
                    String Gender = userDoc.getString("gender") != null ? userDoc.getString("gender") : null;

                    // Set the UI components to display the user's data
                    if (age != null) {
                        Age.setText((age));
                    }
                    if (weight != null) {
                        Weight.setText((weight));
                    }
                    if (height != null) {
                        Height.setText((height));
                    }

                    if (Gender != null) {

                        if(Gender.equals("female"))
                        {
                            Rdfemale.setChecked(true);
                        }
                        else
                        {
                            Rdmale.setChecked(true);

                        }

                    }


                })
                .addOnFailureListener(e ->

                        Log.e(TAG, "Error getting user document.", e));
    }
    public void onClick(View v)
    {
        if(v==CalculateBmr) {
            if (Weight.getText().length() > 0 && Height.getText().length() > 0 && Age.getText().length() > 0
                    &&(Rdfemale.isChecked()||Rdmale.isChecked())) {
                CurrentWeightKg = Double.parseDouble(Weight.getText().toString());
                CurrentHeightCM = Double.parseDouble(Height.getText().toString());
                CurrentAge = Double.parseDouble(Age.getText().toString());

                double CurrentHeightMeter = (CurrentHeightCM / 100);
                if (Rdfemale.isChecked()) {
                    Bmr = 447.593+ (3.098 * CurrentHeightCM) + (CurrentWeightKg * 9.247) -(CurrentAge * 6.8);

                } else {
                    Bmr =  (4.799 * CurrentHeightCM) + (CurrentWeightKg * 13.397) +88.362-(CurrentAge * 5.677) ;

                }
                String BMRString = String.format("%.2f", Bmr);
                BmrReultText.setText(String.valueOf("Your BMR is: " + BMRString+" calories"));
            }
            else {
                Toast.makeText(this, " Fill in all text boxes", Toast.LENGTH_LONG).show();
            }
        }

        if(v == ResetValues)
        {

            Weight.setText("");
            Height.setText("");
            Age.setText("");
            BmrReultText.setText("");


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
                    .setTitle("BMR")
                    .setMessage("\n" +
                            "The Basal Metabolic Rate (BMR) Calculator estimates your basal metabolic rate—the amount of energy expended while at rest in a neutrally temperate environment, and in a post-absorptive state (meaning that the digestive system is inactive, which requires about 12 hours of fasting). ")
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