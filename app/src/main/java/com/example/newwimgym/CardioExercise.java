package com.example.newwimgym;

public class CardioExercise extends Exercise{
    private int duration;
    private int intensity;

    public CardioExercise(String name, int reps, int sets, int weight, int waiting_Time_Sec, String notes,int duration,int intensity) {
        super(name, reps, sets, weight, waiting_Time_Sec, notes);
        this.duration=duration;
    }
}
