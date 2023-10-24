package com.example.mobiledevlab;

public class Note {
    private long id;
    private String title;
    private String description;
    private String colour;

    Note(long id, String title, String description, String colour){
        this.id = id;
        this.title = title;
        this.description = description;
        this.colour = colour;
    }
    Note(){

    }

    public void setId(long id){
        this.id = id;
    }

    public long getId(){
        return id;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }
}
