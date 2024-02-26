package com.example.newwimgym;

import static android.content.ContentValues.TAG;
//import androidx.annotation.NonNull;
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
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class CompareWithFriends extends AppCompatActivity {
    String UserNameLoggedIn;
    private FirebaseFirestore db;
    FriendsAdapter friendsAdapter;
    ListView lv;
    Button search;
    EditText findfriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_with_friends);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        lv=(ListView)findViewById(R.id.FriendsListView);
        search=(Button) findViewById(R.id.searchbtn);
        findfriend=(EditText) findViewById(R.id.namesearch);
        UserNameLoggedIn = intent.getStringExtra("username");
        getFriendsList();


    }

    public void onClick(View v) {
        ArrayList<UserData> friendsList = new ArrayList<>();
        ArrayList<UserData> filteredList = new ArrayList<>(); // Create a new list for filtered results
        String searchQuery = findfriend.getText().toString(); // Get the search query from the user

        if (v == search) {
            db.collection("users")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot userDoc : task.getResult()) {
                                String username = userDoc.getString("username");
                                String email = userDoc.getString("Email");
                                String age = userDoc.getString("age");
                                String weight = userDoc.getString("weight");
                                String height = userDoc.getString("height");
                                boolean visibility = false;

                                if (userDoc.contains("visibility")) {
                                    visibility = userDoc.getBoolean("visibility");
                                }

                                if (username != null && !username.equals(UserNameLoggedIn) && !username.equals("guest123") && visibility) {
                                    UserData friend = new UserData(username, Integer.parseInt(age), Float.parseFloat(weight), Float.parseFloat(height));
                                    friendsList.add(friend);
                                }
                            }

                            if (friendsList.size() >= 1) {
                                for (int i = 0; i < friendsList.size(); i++) {
                                    if (friendsList.get(i).getName().equals(searchQuery)) {
                                        filteredList.add(friendsList.get(i)); // Add matching user to the filtered list
                                    }
                                }

                                friendsAdapter = new FriendsAdapter(this, 0, 0, filteredList);
                                Toast.makeText(getApplicationContext(), "Num of friends: " + filteredList.size(), Toast.LENGTH_SHORT).show();
                                lv.setAdapter(friendsAdapter);
                            }
                        } else {
                            Log.d(TAG, "Error getting friends list: ", task.getException());
                        }
                    });
        }
    }



    public void getFriendsList() {
        ArrayList<UserData> friendsList = new ArrayList<>();

        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot userDoc : task.getResult()) {
                            String username = userDoc.getString("username");
                            String email = userDoc.getString("Email");
                            String age = userDoc.getString("age");
                            String weight = userDoc.getString("weight");
                            String height = userDoc.getString("height");
                            boolean visibility=false;// automatic dont show your information
                            if (userDoc.contains("visibility"))
                            {
                                 visibility = userDoc.getBoolean("visibility");
                            }

                            if (username != null && !username.equals(UserNameLoggedIn) && !username.equals("guest123")&& visibility) // if exist, not logged user,not guest and visibility = false then show info.
                            {
                                UserData friend = new UserData(username, Integer.parseInt(age), Float.parseFloat(weight), Float.parseFloat(height));
                                friendsList.add(friend);
                            }
                        }
                        if (friendsList.size()>=1) {// show num of friends (size of the list)
                            friendsAdapter = new FriendsAdapter(this, 0, 0, friendsList);
                            Toast.makeText(getApplicationContext(), "num of friends : " + friendsList.size(), Toast.LENGTH_SHORT).show();
                            lv.setAdapter(friendsAdapter);
                        }
                    } else
                    {
                        Log.d(TAG, "Error getting friends list: ", task.getException());
                    }
                });
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
                    .setTitle("Trainers list")
                    .setMessage("See other trainers stats!")
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