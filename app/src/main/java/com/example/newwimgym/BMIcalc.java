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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BMIcalc extends AppCompatActivity {
    public Button ResetValues, CalculateBmi;
    public EditText Weight_Edit_Text, Height_Edit_Text;
    public TextView BmiResultText, BmiResultRange;
    double bmi=0, CurrentWeightKg=0, CurrentHeightCM=0;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmicalc);
        ResetValues = (Button) findViewById(R.id.BMIResetPrameters);
        CalculateBmi = (Button) findViewById(R.id.BMI_calculate_btn);
        Weight_Edit_Text = (EditText) findViewById(R.id.BMIWeight);
        Height_Edit_Text = (EditText) findViewById(R.id.BMIHeight);
        BmiResultText = (TextView) findViewById(R.id.BMIResultTextView);
        BmiResultRange = (TextView) findViewById(R.id.BMIRange);
        // get user info from db (about me screen)
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("username", username.toLowerCase())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Get the user document
                    DocumentSnapshot userDoc = queryDocumentSnapshots.getDocuments().get(0);

                    // Get the user's age, weight, and height (if they exist)
                    String weight = userDoc.getString("weight") != null ? userDoc.getString("weight") : null;
                    String height = userDoc.getString("height") != null ? userDoc.getString("height") : null;

                    // Set the UI components to display the user's data

                    if (weight != null) {
                        Weight_Edit_Text.setText((weight));
                    }
                    if (height != null) {
                        Height_Edit_Text.setText((height));
                    }
                })
                .addOnFailureListener(e ->

                        Log.e(TAG, "Error getting user document.", e));

    }

    public void onClick(View v)
    {
            if (v==CalculateBmi)
            {
                if (Weight_Edit_Text.getText().length() > 0 && Height_Edit_Text.getText().length() > 0)
                {
                    CurrentWeightKg = Double.parseDouble(Weight_Edit_Text.getText().toString());
                    CurrentHeightCM = Double.parseDouble(Height_Edit_Text.getText().toString());
                    double CurrentHeightMeter= (CurrentHeightCM/100);
                    bmi = (CurrentWeightKg / Math.pow(CurrentHeightMeter,2));
                    
                    String BMIString = String.format("%.2f", bmi);
                    BmiResultText.setText("Your BMI is: " + BMIString);
                    if (bmi < 18.5)
                        BmiResultRange.setText("BMI note - Rating: Underweight, there is a risk\n of malnutrition");
                    if (bmi < 25 && bmi >= 18.5)
                        BmiResultRange.setText("BMI note - Rating: Proper weight, there is no risk");
                    if (bmi < 30 && bmi >= 25)
                        BmiResultRange.setText("BMI note - Rating: Overweight, there is an increased risk\n when there are background diseases");
                    if (bmi < 35 && bmi >= 30)
                        BmiResultRange.setText("BMI note - Rating: Obesity level 1, there is a medium risk");
                    if (bmi < 40 && bmi >= 35)
                        BmiResultRange.setText("BMI note - Rating: Obesity level 2, there is a severe risk");
                    if (bmi >= 40)
                        BmiResultRange.setText("BMI note - Rating: Obesity level 3, there is an extreme risk");
                }
                else {
                    Toast.makeText(this," Fill in both text boxes",Toast.LENGTH_LONG).show();
                }
            }
            if(v == ResetValues)
            {

                Weight_Edit_Text.setText("");
                Height_Edit_Text.setText("");
                BmiResultRange.setText("");
                BmiResultText.setText("");


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
                    .setTitle("BMI")
                    .setMessage("Body Mass Index is a simple calculation using a person's height and weight. The formula is BMI = kg/m^2 where kg is a person's weight in kilograms and m^2 is their height in metres squared(in this calculator you need to put your height in cm). The BMI will tell you if you in normal weight(in average),OverWeight or UnderWeight. ")
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