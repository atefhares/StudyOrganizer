package com.example.atefhares.study_organizer.DataClasses;

/**
 * Created by Gehad Rezk on 4/13/2016.
 */
public class yearData {
    private String nameYear;
    private String startDate;
    private String endDate;
    private String ID;

    public void setNameYear(String nameYear) {
        this.nameYear = nameYear;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getYearName() {
        return nameYear;
    }
    public String getStartDate() {
        return startDate;
    }
    public String getEndDate() {
        return endDate;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
