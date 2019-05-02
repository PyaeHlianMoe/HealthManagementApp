package com.se_lab.se_proj.model;

public class Suggestion {

    private String type;
    private String suggestion;


    public Suggestion(){

    }

    public Suggestion(String type, String suggestion){
        this.type = type;
        this.suggestion = suggestion;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }




}
