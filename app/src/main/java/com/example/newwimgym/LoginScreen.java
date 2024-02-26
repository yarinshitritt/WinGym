package com.example.newwimgym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;

public class LoginScreen extends AppCompatActivity {
    Button login;
    EditText username,password;
    SharedPreferences sp;
    FirebaseFirestore firebase;
    Boolean userIsFound;
    CheckBox stayConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        firebase=FirebaseFirestore.getInstance();
        username = findViewById(R.id.loginname);
        password = findViewById(R.id.loginpassword);
        login = findViewById(R.id.signin);
        stayConnected = findViewById(R.id.stayConnectedCheckbox);
        userIsFound = false;

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//gets name and password
                String usernameString = username.getText().toString().trim();
                String passwordString = password.getText().toString().trim();

                if (TextUtils.isEmpty(usernameString) || TextUtils.isEmpty(passwordString)) {
                    Toast.makeText(LoginScreen.this, "Fill UserName and Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebase.collection("users")// run on users collection and find the user
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        String dbUsernameString = doc.getString("username");
                                        String dbPasswordString = doc.getString("password");

                                        if (usernameString.equalsIgnoreCase(dbUsernameString) && passwordString.equalsIgnoreCase(dbPasswordString)) {
                                            userIsFound = true;

                                            // Save username and password to SharedPreferences if "stay connected" is checked
                                            if (stayConnected.isChecked()) {
                                                SharedPreferences.Editor editor = getSharedPreferences("loginPrefs", MODE_PRIVATE).edit();
                                                editor.putString("username", usernameString);
                                                editor.putString("password", passwordString);
                                                editor.apply();
                                            }

                                            Intent home = new Intent(getApplicationContext(), MainScreen.class);
                                            home.putExtra("username", dbUsernameString);
                                            startActivity(home);
                                            Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }

                                    if (!userIsFound) {
                                        Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Check SharedPreferences for saved login credentials
        sp = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String savedUsername = sp.getString("username", "");
        String savedPassword = sp.getString("password", "");

        if (!TextUtils.isEmpty(savedUsername) && !TextUtils.isEmpty(savedPassword)) {
            username.setText(savedUsername);
            password.setText(savedPassword);
            stayConnected.setChecked(true);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activityies_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();

        if (id == R.id.about) {
            Toast.makeText(this, "sign in to your user", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.returnbtn) {
            finish();
            System.exit(0);
        }

        return true;
    }
}
