package com.example.newwimgym;


public class UserData {
    private String Mail; // user's mail
    private int age; //user's age
    private float height;// user's height
    private float weight;// user's weight
    private Target userTarget;// user's target
    private String name;// user's name


    public UserData(String username, Integer newAge, Float newWeight, Float newHeight) {
        this.name=username;
        this.age=newAge;
        this.weight=newWeight;
        this.height=newHeight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Target getUserTarget() {
        return userTarget;
    }

    public void setUserTarget(Target userTarget) {
        this.userTarget = userTarget;
    }



    public enum Target
    {
        Mass,
        tonning
    }
}
