package com.example.newwimgym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewEx extends AppCompatActivity {
   public  Button btnAccept, btnCancel;
    public EditText name, reps,sets,weight,Waiting_Time_Sec,notes;
    StrengthExercise ex, currentEx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_ex);
        name = (EditText) findViewById(R.id.new_Name);
        reps = (EditText) findViewById(R.id.new_Reps);
        sets = (EditText) findViewById(R.id.new_Sets);
        weight = (EditText) findViewById(R.id.new_Weight);
        Waiting_Time_Sec = (EditText) findViewById(R.id.new_waiting_time_sec);
        notes = (EditText) findViewById(R.id.new_Notes);
        btnAccept = (Button) findViewById(R.id.new_btnSave);
        btnCancel = (Button) findViewById(R.id.new_btnCancel);


    }
    public void onClick(View v) {
        if(btnAccept==v)//option 2 - save the data and go to first screen
        {
            String New_Name = name.getText().toString();
            String New_Reps= reps.getText().toString();
            String New_Sets=sets.getText().toString();
            String New_Weight = weight.getText().toString();
            String New_Waiting_time_sec = Waiting_Time_Sec.getText().toString();
            String New_Notes=notes.getText().toString();

            if(New_Name.length()==0)
                Toast.makeText(this,"Enter Name", Toast.LENGTH_SHORT).show();
            else if(New_Reps.length()==0)
                Toast.makeText(this,"Enter Reps", Toast.LENGTH_SHORT).show();
            else if(New_Sets.length()==0)
                Toast.makeText(this,"Enter Sets", Toast.LENGTH_SHORT).show();
            else if(New_Weight.length()==0)
                Toast.makeText(this,"Enter Weight", Toast.LENGTH_SHORT).show();
            else if(New_Waiting_time_sec .length()==0)
                Toast.makeText(this,"Enter Rest Time", Toast.LENGTH_SHORT).show();
            else
            {
                //add new exercise
                ex = new StrengthExercise(New_Name,Integer.parseInt(New_Reps),Integer.parseInt(New_Sets)
                        ,Integer.parseInt(New_Weight),Integer.parseInt(New_Waiting_time_sec),New_Notes);
                Intent intent = new Intent();
                intent.putExtra("Exercise2",ex);
                setResult(RESULT_OK, intent);
                Toast.makeText(this,"Information saved", Toast.LENGTH_SHORT).show();

                finish();
            }

        }
        else if (btnCancel==v)//option 3 - cancel-  and go to first screen
        {
            //  setResult(RESULT_CANCELED,null);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}