package com.example.newwimgym;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CalorieCalc extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] ActivityLevel = { "No physical activity", "1 - 3 trainings pew week", "3 - 5 trainings pew week", "6 - 7 trainings pew week", "Strong physical work"};
    String SelectedActivityLevel = "No physical activity";
    Button CalculateCalories,ResetValues;
    EditText BMR_Result;
    TextView CalorieCalcResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_calc);

        Spinner spin = (Spinner) findViewById(R.id.Caloriecalcspinner);
        CalculateCalories = (Button) findViewById(R.id.CalorieCalcBbtn);
        ResetValues = (Button) findViewById(R.id.CalorieCalcResetValues);
        BMR_Result = (EditText)findViewById(R.id.CaloriecalcBmr);
        CalorieCalcResult= (TextView)findViewById(R.id.CalorieCalculateResult);
        spin.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,ActivityLevel);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        spin.setSelection(0);

    }
    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        SelectedActivityLevel = ActivityLevel[position];
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    public void onClick (View v)
    {
        if (v==CalculateCalories)
        {
            if (BMR_Result.getText().length()>0)
            {
                double BMR =Double.parseDouble( BMR_Result.getText().toString());
                String ActivityLevel = SelectedActivityLevel;
                double CalorieCalc = 0;
                switch (ActivityLevel)
                {
                    case "No physical activity":
                        CalorieCalc=BMR * 1.2;
                        break;
                    case "1 - 3 trainings pew week":
                        CalorieCalc=BMR * 1.375;
                        break;
                    case "3 - 5 trainings pew week":
                        CalorieCalc=BMR * 1.55;
                        break;
                    case  "6 - 7 trainings pew week":
                        CalorieCalc=BMR * 1.725;
                        break;
                    case "Strong physical work":
                        CalorieCalc=BMR * 1.9;
                        break;
                    default:
                        CalorieCalc=0;
                        break;
                }
                CalorieCalc = CalorieCalc * 1.1;
                String CalorieCalcString = String.format("%.2f", CalorieCalc);

                CalorieCalcResult.setText("You Need to Consume "+CalorieCalcString+" Calories To Reach Caloric Balance");

            }
            else
            {
                Toast.makeText(this," Fill Values",Toast.LENGTH_LONG).show();

            }

        }
        if (v== ResetValues)
        {
            BMR_Result.setText("");
            CalorieCalcResult.setText("");
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
                    .setTitle("Calorie calculator")
                    .setMessage("Using your BMR result and your activity level, this calculator will tell you how many calories you need to consume a day to reach calorie balance  ")
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