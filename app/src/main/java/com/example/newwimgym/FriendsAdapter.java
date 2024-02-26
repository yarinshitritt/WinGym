package com.example.newwimgym;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FriendsAdapter extends ArrayAdapter<UserData> {

    Context context;
    List<UserData> objects;

    public FriendsAdapter(Context context, int resource, int textViewResourceId, List<UserData> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;
    }
    //The purpose of the adapter is a bridge between a graphic element and an information source that fills the element. FriendsAdapter is designed to display the users in a list (listview) in the CompareWithFriends screen as desired.

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.friendslistcustomlayout,parent,false);
        TextView name = (TextView)view.findViewById(R.id.Nametv);
        TextView Age = (TextView)view.findViewById(R.id.AgeTv);
        TextView Weight = (TextView)view.findViewById(R.id.weighTtv);
        TextView Height = (TextView)view.findViewById(R.id.HeightTv);

        UserData temp = objects.get(position);


        name.setText(temp.getName());
        Age.setText(String.valueOf(temp.getAge()));
        Weight.setText(String.valueOf(temp.getWeight()));
        Height.setText(String.valueOf(temp.getHeight()));

        return view;
    }
}
