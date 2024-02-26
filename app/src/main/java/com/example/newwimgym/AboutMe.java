package com.example.newwimgym;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android  .view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AboutMe extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "AboutMeActivity";
    private EditText ageEditText;
    private EditText weightEditText;
    private EditText heightEditText;
    private Switch visibilitySwitch;
    private EditText calorieGoalEditText;
    private Button saveButton;
    String[] GenderArr = { "male","female"};
    String GenderSelected;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        db = FirebaseFirestore.getInstance();
        Spinner spin = (Spinner) findViewById(R.id.GenderSpinner);
        spin.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,GenderArr);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        spin.setSelection(0);
        visibilitySwitch = findViewById(R.id.visibility_switch);
        // Get the current user's username from the intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        // Set up the UI components
        EditText ageEditText = findViewById(R.id.age_input);
        EditText weightEditText = findViewById(R.id.weight_input);
        EditText heightEditText = findViewById(R.id.height_input);
        EditText calorieGoalEditText =findViewById(R.id.calorieinput);

        // Get the user document from Firestore
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

                    // Check if the "visibility" field exists in the document
                    if (userDoc.contains("visibility")) {
                        boolean visibility = userDoc.getBoolean("visibility");
                        // Set the switch button to the value of the user's visibility field
                        visibilitySwitch.setChecked(visibility);
                    }
                    // Set the UI components to display the user's data
                    if (age != null) {
                        ageEditText.setText((age));
                    }
                    if (weight != null) {
                        weightEditText.setText((weight));
                    }
                    if (height != null) {
                        heightEditText.setText((height));
                    }
                    if (calorieGoal != null) {
                        calorieGoalEditText.setText((calorieGoal));
                    }
                    if (Gender != null) {

                        GenderSelected=Gender;
                        if(Gender.equals("female"))
                        {
                            spin.setSelection(1);
                        }
                    }
                    else
                    {
                        GenderSelected= "male";

                    }


                })
                .addOnFailureListener(e ->

                        Log.e(TAG, "Error getting user document.", e));

        // Set up the Save button
        Button saveButton = findViewById(R.id.SaveBtn);
        saveButton.setOnClickListener(view -> {
            // Get the user's new age, weight, and height from the UI components
            Integer newAge = ageEditText.getText().toString().isEmpty() ? null : Integer.parseInt(ageEditText.getText().toString());
            Integer newWeight = weightEditText.getText().toString().isEmpty() ? null : Integer.parseInt(weightEditText.getText().toString());
            Integer newHeight = heightEditText.getText().toString().isEmpty() ? null : Integer.parseInt(heightEditText.getText().toString());
            Integer newCalorieGoal = calorieGoalEditText.getText().toString().isEmpty() ? null : Integer.parseInt(calorieGoalEditText.getText().toString());
            String newGender = GenderSelected;
            boolean newVisibility = visibilitySwitch.isChecked();
            UpdateUserDetails(username,newAge,newWeight,newHeight,newCalorieGoal,newGender,newVisibility);

        });

    }
    public void UpdateUserDetails(String username , Integer newAge,Integer newWeight,Integer newHeight,Integer newCalorieGoal,String  newGender,boolean newVisibility)
    {

        Map<String, Object> newData = new HashMap<>();
        newData.put("age", String.valueOf(newAge));
        newData.put("weight",  String.valueOf(newWeight));
        newData.put("height",   String.valueOf(newHeight));
        newData.put("calorieGoal",   String.valueOf(newCalorieGoal));
        newData.put("gender",   newGender);
        newData.put("visibility",newVisibility);
        db.collection("users").whereEqualTo("username", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful())
                {
                    // If the username was found, get the document
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);

                    // Update the document with the new exercise list
                    db.collection("users").document(document.getId()).update(newData).addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // The update was successful
                            Toast.makeText(getApplicationContext(), "Successfully updated user info: " + username, Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            // The update failed
                            Toast.makeText(getApplicationContext(), "Failed updated user info: " + username, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                else
                {
                    // If the username was not found, log an error message
                    Toast.makeText(getApplicationContext(), "Error getting documents", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        GenderSelected = GenderArr[position];

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                    .setTitle("About me")
                    .setMessage("Enter information about you!")
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