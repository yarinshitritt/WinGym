package com.example.newwimgym;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExAdapter extends ArrayAdapter<StrengthExercise> {

    Context context;
    List<StrengthExercise> objects;

    public ExAdapter(Context context, int resource, int textViewResourceId, List<StrengthExercise> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;
    }
    //The purpose of the adapter is a bridge between a graphic element and an information source that fills the element. ExAdapter is designed to display the exercises (exercise) in a list (listview) on the dailytraining screen in the desired way.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.customlayout,parent,false);
        TextView name = (TextView)view.findViewById(R.id.custom_Name);
        TextView reps = (TextView)view.findViewById(R.id.custom_Reps);
        TextView sets = (TextView)view.findViewById(R.id.custom_Sets);
        //TextView weight = (TextView)view.findViewById(R.id.custom_Weight);
        TextView Notes = (TextView)view.findViewById(R.id.custom_Notes);

        StrengthExercise temp = objects.get(position);


        name.setText(temp.getName());
        reps.setText(String.valueOf(temp.getReps()));
        sets.setText(String.valueOf(temp.getSets()));
        //weight.setText(String.valueOf(temp.getWeight()));
        Notes.setText((temp.getNotes()));

        return view;
    }

}