package com.example.atefhares.study_organizer.DataClasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Atef Hares on 23-Apr-16.
 */
public class timeData implements Parcelable {
    private String ID;
    private String day;
    private String startTime;
    private String EndTime;
    private String duration;
    private String ClassID;
    private String TermID;

    public timeData(){

    }

    protected timeData(Parcel in) {
        ID = in.readString();
        day = in.readString();
        startTime = in.readString();
        EndTime = in.readString();
        duration = in.readString();
        ClassID = in.readString();
        TermID = in.readString();
    }

    public static final Creator<timeData> CREATOR = new Creator<timeData>() {
        @Override
        public timeData createFromParcel(Parcel in) {
            return new timeData(in);
        }

        @Override
        public timeData[] newArray(int size) {
            return new timeData[size];
        }
    };

    public void setID(String ID) {
        this.ID = ID;
    }
    public String getID() {
        return ID;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDay() {
        return day;
    }
    public String getStartTime() {
        return startTime;
    }
    public String getDuration() {
        return duration;
    }

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String ID) {
        ClassID = ID;
    }
    public String getEndTime() {
        return EndTime;
    }
    public void setEndTime(String endTime) {
        EndTime = endTime;
    }


    public String getTermID() {
        return TermID;
    }

    public void setTermID(String termID) {
        TermID = termID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(day);
        dest.writeString(startTime);
        dest.writeString(EndTime);
        dest.writeString(duration);
        dest.writeString(ClassID);
        dest.writeString(TermID);
    }
}