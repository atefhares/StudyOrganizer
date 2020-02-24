package com.example.atefhares.study_organizer.DataClasses;

/**
 * Created by Gehad Rezk on 4/13/2016.
 */
public class classData {
    private String ID;
    private String place;
    private String Name;
    private String InstructorName;
    private String module;
    private String color;
    private String termID;
    public void setClassName(String class_name) {
        Name = class_name;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public void setInstructorName(String instructorName) {
        this.InstructorName = instructorName;
    }
    public void setModule(String module) {
        this.module = module;
    }
    public void setColor(String color){this.color=color;}
    public String getName() {
        return Name;
    }
    public String getInstructorName() {
        return InstructorName;
    }
    public String getPlace() {
        return place;
    }
    public String getModule() {
        return module;
    }
    public String getColor(){return color;}
    public String getID() {
        return ID;
    }
    public void setID(String id) {
        ID = id;
    }
    public String getTermID() {
        return termID;
    }
    public void setTermID(String term_ID) {
        termID = term_ID;
    }
}

