package com.example.newwimgym;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DailyTraining extends AppCompatActivity {
    // timer values
    // Is the stopwatch running?
    private boolean running;
    private boolean wasRunning;
    private int seconds = 0;

    private Button AddNewEx;

    // list values
    private ArrayList<StrengthExercise> ExList;
    private ExAdapter exAdapter;
    StrengthExercise lastSelected;
    ListView lv;
    int Itemposition;

    String username;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_training);
        AddNewEx= (Button) findViewById(R.id.AddNewExBtn);
        lv=(ListView)findViewById(R.id.ListView);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);



        firestore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        username =  intent.getStringExtra("username");
        //Toast.makeText(this,username, Toast.LENGTH_SHORT).show();

        runTimer();

        getExList2();
        ExList = new ArrayList<StrengthExercise>();
        lv.setOnItemClickListener((parent, view, position, id) -> {//This sets an OnItemClickListener on the ListView object referred to by the variable lv.
            lastSelected = exAdapter.getItem(position);// retrieves the item that was clicked in the ListView, using the exAdapter object to get the item at the specified position. The retrieved item is then stored in the lastSelected variable.
            Itemposition=position;//sets the Itemposition variable to the position of the clicked item in the ListView.
            Intent intent2= new Intent(DailyTraining.this, EditActivity.class);// call the edit activity intent
            intent2.putExtra("Exercise",lastSelected);// put the item from EditActivity in exercise
            startActivityForResult(intent2, 0);// 0 means edit mode, starts the EditActivity with the specified Intent, and passes a request code of 0 to indicate that the activity should be started in "edit mode". The startActivityForResult method starts the EditActivity and expects a result to be returned from it. When the EditActivity finishes and returns a result, the onActivityResult method will be called to handle the result.

        });


    }




    // If the activity is paused,
    // stop the stopwatch.
    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    // If the activity is resumed,
    // start the stopwatch
    // again if it was running previously.
    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    // Start the stopwatch running
    // when the Start button is clicked.
    // Below method gets called
    // when the Start button is clicked.
    public void onClickStart(View view) {
        running = true;
    }

    // Stop the stopwatch running
    // when the Stop button is clicked.
    // Below method gets called
    // when the Stop button is clicked.
    public void onClickStop(View view) {
        running = false;
    }

    // Reset the stopwatch when
    // the Reset button is clicked.
    // Below method gets called
    // when the Reset button is clicked.
    public void onClickReset(View view) {
        running = false;
        seconds = 0;
    }

    // Sets the NUmber of seconds on the timer.
    // The runTimer() method uses a Handler
    // to increment the seconds and
    // update the text view.
    private void runTimer() {

        // Get the text view.
        final TextView timeView
                = (TextView) findViewById(
                R.id.time_view);

        // Creates a new Handler
        final Handler handler
                = new Handler();

        // Call the post() method,
        // passing in a new Runnable.
        // The post() method processes
        // code without a delay,
        // so the code in the Runnable
        // will run almost immediately.
        handler.post(new Runnable() {
            @Override

            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                // Format the seconds into hours, minutes,
                // and seconds.
                String time
                        = String
                        .format(Locale.getDefault(),
                                "%d:%02d:%02d", hours,
                                minutes, secs);

                // Set the text view text.
                timeView.setText(time);

                // If running is true, increment the
                // seconds variable.
                if (running) {
                    seconds++;
                }

                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000);
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== 0 ) {// activity_edit
            if(resultCode==5)// delete ex
            {
                ExList.remove(Itemposition);
                exAdapter = new ExAdapter(this, 0, 0, ExList);
                lv.setAdapter(exAdapter);
                Toast.makeText(getApplicationContext(), "deleted", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = data;//initializes an Intent object  with the data returned from the EditActivity.
                Bundle b = intent.getExtras();
                if (b != null) {
                    StrengthExercise currentEx = (StrengthExercise) intent.getSerializableExtra("Exercise2");
                    ExList.set(Itemposition, currentEx);//replaces the item at the specified position (Itemposition) in the ExList with the updated currentEx.
                    exAdapter = new ExAdapter(this, 0, 0, ExList);//creates a new instance of the ExAdapter using the updated ExList.
                    //phase 4 reference to listview
                    lv = (ListView) findViewById(R.id.ListView);
                    lv.setAdapter(exAdapter);
                }
            }
        }
        else if (requestCode ==1) // new exercise
        {
            Intent intent = data;
            Bundle b = intent.getExtras();

            if (b != null) {
                StrengthExercise currentEx = (StrengthExercise) intent.getSerializableExtra("Exercise2");
                if (ExList.size() >= 1000) {
                    Log.e("DailyTraining", "ExList has exceeded the maximum capacity");
                }
                if (currentEx != null) {
                    ExList.add(currentEx);
                }

                exAdapter = new ExAdapter(this, 0, 0, ExList);
                //phase 4 reference to listview
                lv.setAdapter(exAdapter);

            }
        }

        updateExerciseList(ExList);
    }

    public void updateExerciseList(ArrayList<StrengthExercise> exList) {

        // Find the document with the matching username
            firestore.collection("users").whereEqualTo("username", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // If the username was found, get the document
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                   // Toast.makeText(getApplicationContext(), task.getResult().toString(), Toast.LENGTH_SHORT).show();

                    // Update the document with the new exercise list
                    firestore.collection("users").document(document.getId()).update("ExList", exList).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // The update was successful
                            Toast.makeText(getApplicationContext(), "Successfully updated exercise list for user: " + username, Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // The update failed
                            Toast.makeText(getApplicationContext(), "Failed updated exercise list for user: " + username, Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    // If the username was not found, log an error message
                    Toast.makeText(getApplicationContext(), "Error getting documents", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    public ArrayList<StrengthExercise> getExList() {
        final ArrayList<StrengthExercise> exList1 = new ArrayList<>();

        firestore.collection("users").whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                Object exListObject = querySnapshot.getDocuments().get(0).get("ExList");
                                if (exListObject instanceof ArrayList) {
                                    ArrayList exList = (ArrayList) exListObject;
                                    for (Object exercise : exList) {
                                        if (exercise instanceof Map) {
                                            Map<String, Object> exerciseMap = (Map<String, Object>) exercise;


                                            String name = (String) exerciseMap.get("name");
                                            int reps = ((Long) exerciseMap.get("reps")).intValue();
                                            int sets = ((Long) exerciseMap.get("sets")).intValue();
                                            int weight = ((Long) exerciseMap.get("weight")).intValue();
                                            int waitingTimeSec = ((Long) exerciseMap.get("waiting_Time_Sec")).intValue();
                                            String notes = (String) exerciseMap.get("notes");

                                            StrengthExercise exercise2 = new StrengthExercise(name, reps, sets, weight, waitingTimeSec, notes);
                                            // Add the Exercise instance to your ArrayList
                                            exList1.add(exercise2);
                                        } else {
                                            Log.e("ClassCastException", "Element in ExList is not a Map");
                                        }

                                    }
                                  //  Toast.makeText(getApplicationContext(), "num of Ex : " + exList1.size(), Toast.LENGTH_SHORT).show();

                                    if(exList1.size()>0) {
                                        exAdapter = new ExAdapter(getApplicationContext(), 0, 0, exList1);
                                        lv.setAdapter(exAdapter);
                                    }

                                    //phase 4 reference to listview
                                } else {
                                    Log.e("ClassCastException", "ExList is not an ArrayList");
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Cannot read ExList", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return exList1;
    }
    public void getExList2() {
        final ArrayList<StrengthExercise> exList1 = new ArrayList<>();
        firestore.collection("users").whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            Object exListObject = querySnapshot.getDocuments().get(0).get("ExList");
                            if (exListObject instanceof ArrayList) {
                                ArrayList exList = (ArrayList) exListObject;
                                for (Object exercise : exList) {
                                    if (exercise instanceof Map) {
                                        Map<String, Object> exerciseMap = (Map<String, Object>) exercise;

                                        String name = (String) exerciseMap.get("name");
                                        int reps = ((Long) exerciseMap.get("reps")).intValue();
                                        int sets = ((Long) exerciseMap.get("sets")).intValue();
                                        int weight = ((Long) exerciseMap.get("weight")).intValue();
                                        int waitingTimeSec = ((Long) exerciseMap.get("waiting_Time_Sec")).intValue();
                                        String notes = (String) exerciseMap.get("notes");

                                        StrengthExercise exercise2 = new StrengthExercise(name, reps, sets, weight, waitingTimeSec, notes);
                                        // Add the Exercise instance to your ArrayList
                                        exList1.add(exercise2);
                                    } else {
                                        Log.e("ClassCastException", "Element in ExList is not a Map");
                                    }
                                }
                            }
                            if (exList1.size() > 0) {
                                exAdapter = new ExAdapter(this, 0, 0, exList1);
                                lv.setAdapter(exAdapter);
                                ExList = exList1;
                            }
                        }
                    } else {
                        Log.d(TAG, "Error getting friends list: ", task.getException());
                    }
                });
    }
    public void onClick(View v)
    {
        if (v == AddNewEx)
        {
            Intent intent = new Intent(DailyTraining.this, AddNewEx.class);
            startActivityForResult(intent, 1);// 0 means edit mode
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
                    .setTitle("Daily Training")
                    .setMessage("In this screen you can prepare your own organized training table and measure your training time! ")
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
