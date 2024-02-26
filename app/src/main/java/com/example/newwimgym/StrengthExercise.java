package com.example.newwimgym;

public class StrengthExercise extends Exercise{
    private int weight; // user's weight
    private int Waiting_Time_Sec; // waiting time between sets in seconds
        public StrengthExercise(String name, int reps, int sets, int weight, int waiting_Time_Sec, String notes) {
        super(name, reps, sets, weight, waiting_Time_Sec, notes);
        this.Waiting_Time_Sec= waiting_Time_Sec;
        this.weight=weight;
    }

    public int getWeight() {
        return weight;
    }

    public int getWaiting_Time_Sec() {
        return Waiting_Time_Sec;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setWaiting_Time_Sec(int waiting_Time_Sec) {
        Waiting_Time_Sec = waiting_Time_Sec;
    }
}
