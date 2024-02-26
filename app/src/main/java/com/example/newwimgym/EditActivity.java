package com.example.newwimgym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import java.io.Serializable;

public class EditActivity extends AppCompatActivity  implements View.OnClickListener {
    Button btnAccept, btnCancel,btnDelete;
    EditText  name, reps,sets,weight,Waiting_Time_Sec,notes;
    StrengthExercise ex, currentEx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        name = (EditText) findViewById(R.id.edit_Name);
        reps = (EditText) findViewById(R.id.edit_Reps);
        sets = (EditText) findViewById(R.id.edit_Sets);
        weight = (EditText) findViewById(R.id.edit_Weight);
        Waiting_Time_Sec = (EditText) findViewById(R.id.edit_waiting_time_sec);
        notes = (EditText) findViewById(R.id.edit_Notes);

        btnAccept = (Button) findViewById(R.id.edit_btnSave);
        btnCancel = (Button) findViewById(R.id.edit_btnCancel);
        btnDelete = (Button) findViewById(R.id.edit_btndelete);

        btnAccept.setOnClickListener( this);
        btnCancel.setOnClickListener( this);
        btnDelete.setOnClickListener(this);

        //connect to intent if its edit mode
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b!=null) {// save the data from the previous screen
            currentEx = (StrengthExercise) intent.getSerializableExtra("Exercise");
            String Current_Name = currentEx.getName();
            String Current_Reps =Integer.toString( currentEx.getReps());
            String Current_Sets =Integer.toString( currentEx.getSets());
            String Current_Weight =Integer.toString( currentEx.getWeight());
            String Current_RestTime =Integer.toString( currentEx.getWaiting_Time_Sec());
            String Current_Notes =currentEx.getNotes();
            name.setText(Current_Name);
            reps.setText(Current_Reps);
            sets.setText(Current_Sets);
            weight.setText(Current_Weight);
            Waiting_Time_Sec.setText(Current_RestTime);
            notes.setText(Current_Notes);

        }

    }
    @Override
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
            intent.putExtra("Exercise2",currentEx);
            setResult(RESULT_OK, intent);
            finish();
        }
        else if (btnDelete==v)
        {
            Intent intent = new Intent();
            setResult(5, intent);
            finish();

        }


    }


}