package com.example.newwimgym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpScreen extends AppCompatActivity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private EditText repassword;
    private EditText email;
    private Button signup;
    private Button backbtn;
    Boolean UserNameExist;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        firestore = FirebaseFirestore.getInstance();
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.password2);
        email = (EditText) findViewById(R.id.Email);
        signup = (Button) findViewById(R.id.reg);
        signup.setOnClickListener(this);


    }

    public static boolean isvalidemalil(String email) {//checking if email pattern is appropriate
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        } else {
            return true;
        }
    }


    public void onClick(View v) {
        if (signup == v) {
            UserNameExist = false;
            if (username.getText().toString().length() < 6) {//name length bigger then six
                Toast.makeText(this, "UserName should be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else if (password.getText().toString().length() < 6) {// password length bigger then six
                Toast.makeText(this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else if (!password.getText().toString().equals(repassword.getText().toString())) {// password no equal to repassword
                Toast.makeText(this, "Passwords does not match", Toast.LENGTH_SHORT).show();
            } else if (!isvalidemalil(email.getText().toString())) {// email is valid
                Toast.makeText(this, "InValid Email", Toast.LENGTH_SHORT).show();
            } else {// valid user information
                Map<String, Object> users = new HashMap<>();// update information for each user in the firebase
                users.put("username", username.getText().toString());
                users.put("password", password.getText().toString());
                users.put("Email", email.getText().toString());
                users.put("age", "0");
                users.put("weight", "0");
                users.put("height", "0");
                users.put("CurrentCalorieIntake","0");
                users.put("calorieGoal", "2000");
                users.put("gender", "male");
                users.put("visibility",false);
                List<Exercise> ls = Arrays.asList();
                users.put("ExList", ls);

                firestore.collection("users")// run on user collection
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {
                                if (task.isSuccessful())
                                {
                                    int count = 0 ;
                                    for (QueryDocumentSnapshot doc : task.getResult())
                                    {
                                        String a = doc.getString("username");
                                        String a1 = username.getText().toString().trim();
                                        if (a != null && a.equalsIgnoreCase(a1))// checking if current name is equal to an existing name. and sending massage if user exist
                                        {
                                            UserNameExist = true;
                                            Toast.makeText(getApplicationContext(), "username is used", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                        count=count+1;

                                    }
                                    if (!UserNameExist)// if user doesn't exist, add him to the collection.
                                    {

                                        firestore.collection("users").add(users)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "Failed To Register: "+e.getMessage() , Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    }

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Failed To Read documents from DB", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed To Read documents from DB", Toast.LENGTH_SHORT).show();
                    }
                });


            }

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
            Toast.makeText(this,"create a user", Toast.LENGTH_SHORT).show();


        }


        else if(R.id.returnbtn==id)
        {

            finish();
            System.exit(0);
        }

        return true;
    }
}
