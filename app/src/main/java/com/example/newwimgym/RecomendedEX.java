package com.example.newwimgym;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class RecomendedEX extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private TableLayout tableLayout;

    // Define a list of muscles
    private String[] muscles = {"Biceps", "Triceps", "Chest", "Abs", "Quadriceps", "Hamstrings", "Glutes"};

    // Define a 2D array of exercises and URLs for each muscle
    private String[][] exercises = {
            {"Bicep curls", "Hammer curls", "Concentration curls"},
            {"Tricep dips", "Tricep pushdowns", "Skull crushers"},
            {"Push-ups", "Bench press", "Cable flys"},
            {"Crunches", "Leg raises", "Plank"},
            {"Squats", "Lunges", "Leg press"},
            {"Deadlifts", "Leg curls", "Good mornings"},
            {"Squats", "Hip thrusts", "Lunges"}
    };
    private String[][] urls = {
            {"https://www.youtube.com/watch?v=ykJmrZ5v0Oo", "https://www.youtube.com/watch?v=TwD-YGVP4Bk", "https://www.youtube.com/watch?v=0AUGkch3tzc"},
            {"https://www.youtube.com/watch?v=0326dy_-CzM", "https://www.youtube.com/watch?v=2-LAMcpzODU", "https://www.youtube.com/watch?v=d_KZxkY_0cM"},
            {"https://www.youtube.com/watch?v=IODxDxX7oi4", "https://www.youtube.com/watch?v=4Y2ZdHCOXok", "https://www.youtube.com/watch?v=Iwe6AmxVf7o"},
            {"https://www.youtube.com/watch?v=5ER5Of4MOPI", "https://www.youtube.com/watch?v=JB2oyawG9KI", "https://www.youtube.com/watch?v=F-nQ_KJgfCY"},
            {"https://www.youtube.com/watch?v=gcNh17Ckjgg", "https://www.youtube.com/watch?v=wrwwXE_x-pQ", "https://www.youtube.com/watch?v=IZxyjW7MPJQ"},
            {"https://www.youtube.com/watch?v=XxWcirHIwVo", "https://www.youtube.com/watch?v=dY7BmNR7RJk", "https://www.youtube.com/watch?v=f23vXjoG2e8"},
            {"https://www.youtube.com/watch?v=gcNh17Ckjgg", "https://www.youtube.com/watch?v=xDmFkJxPzeM", "https://www.youtube.com/watch?v=wrwwXE_x-pQ"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomended_ex);
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        spinner = findViewById(R.id.spinner);
        tableLayout = findViewById(R.id.tableLayout);

        // Create an ArrayAdapter using the muscles array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, muscles);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Set the spinner listener
        spinner.setOnItemSelectedListener(this);
    }
    //Performing action onItemSelected and onNothing selected

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Clear the table layout
        tableLayout.removeAllViews();

        // Get the exercises for the selected muscle
        String[] selectedExercises = exercises[position];
        String[] selectedUrls = urls[position];

        // Add a row to the table layout for each exercise
        for (int i = 0; i < selectedExercises.length; i++) {
            TableRow row = new TableRow(this);

            // Add the exercise name to the row
            TextView exerciseNameView = new TextView(this);
            exerciseNameView.setText(selectedExercises[i]);

            // Create a clickable hyperlink for the exercise URL
            SpannableStringBuilder ssb = new SpannableStringBuilder(selectedExercises[i] + " (video)");
            int finalI = i;
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    // Open the exercise video in a web browser
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedUrls[finalI]));
                    startActivity(intent);
                }
            };
            ssb.setSpan(clickableSpan, selectedExercises[i].length() + 1, selectedExercises[i].length() + 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            exerciseNameView.setText(ssb);
            exerciseNameView.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

            // Add the exercise name to the row
            row.addView(exerciseNameView);

            // Add the row to the table layout
            tableLayout.addView(row);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
                    .setTitle("Recommended exercises")
                    .setMessage("In this screen you can choose a muscle and see training videos that works on this muscle")
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