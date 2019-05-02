package com.se_lab.se_proj.model;

public class Profile {


    private String firstName;
    private String lastName;
    private String year;
    private String gender;
    private String weight;
    private String height;
    private String targetWeight;
    private String goal;

    public Profile(){}

    public Profile(String firstName, String lastName, String year, String gender, String weight, String height, String targetWeight, String goal){
        this.firstName = firstName;
        this.lastName = lastName;
        this.year = year;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.targetWeight = targetWeight;
        this.goal = goal;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(String targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String program) {
        this.goal = goal;
    }




}
