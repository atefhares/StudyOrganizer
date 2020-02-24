package com.example.atefhares.study_organizer.DataClasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gehad Rezk on 4/13/2016.
 */
public class termData implements Parcelable {
    private String ID;
    private String nameTerm;
    private String startDate;
    private String endDate;
    private String yearID;

    public termData(){

    }

    protected termData(Parcel in) {
        ID = in.readString();
        nameTerm = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        yearID = in.readString();
    }

    public static final Creator<termData> CREATOR = new Creator<termData>() {
        @Override
        public termData createFromParcel(Parcel in) {
            return new termData(in);
        }

        @Override
        public termData[] newArray(int size) {
            return new termData[size];
        }
    };

    public void setName(String nameTerm) {
        this.nameTerm = nameTerm;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getID() {
        return ID;
    }
    public String getTermName() {
        return nameTerm;
    }
    public String getStartDate() {
        return startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public String getYearID() {
        return yearID;
    }
    public void setYearID(String yearID) {
        this.yearID = yearID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(nameTerm);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(yearID);
    }
}
