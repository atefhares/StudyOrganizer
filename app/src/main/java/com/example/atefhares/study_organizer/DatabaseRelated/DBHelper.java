package com.example.atefhares.study_organizer.DatabaseRelated;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.atefhares.study_organizer.DataClasses.classData;
import com.example.atefhares.study_organizer.DataClasses.examData;
import com.example.atefhares.study_organizer.DataClasses.termData;
import com.example.atefhares.study_organizer.DataClasses.timeData;
import com.example.atefhares.study_organizer.DataClasses.yearData;
import com.example.atefhares.study_organizer.UtilitiesClasses.ComparatorByDate;
import com.example.atefhares.study_organizer.UtilitiesClasses.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Gehad Rizk on 27-Apr-16.
 * Modified by Atef Hares
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;

    private static final String DATABASE_NAME = "StudentData";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_YEAR_NAME = "years";
    private static final String TABLE_TERM_NAME = "terms";
    private static final String TABLE_CLASS_NAME = "classes";
    private static final String TABLE_TIMES_NAME = "times";
    private static final String TABLE_EXAMS_NAME = "exams";

    //years table - columns names
    private static final String KEY_YEAR_TABLE_ID = "id";
    private static final String KEY_YEAR_TABLE_NAME = "name";
    private static final String KEY_YEAR_TABLE_START_DATE = "start_date";
    private static final String KEY_YEAR_TABLE_END_DATE = "end_date";

    //terms table -columns names
    private static final String KEY_TERM_TABLE_ID = "id";
    private static final String KEY_TERM_TABLE_NAME = "name";
        private static final String KEY_TERM_TABLE_START_DATE = "start_date";
    private static final String KEY_TERM_TABLE_END_DATE = "end_date";
    private static final String KEY_TERM_TABLE_YEAR_ID = "year_id";

    //classes table - columns name
    private static final String KEY_CLASS_TABLE_ID = "id";
    private static final String KEY_CLASS_TABLE_NAME = "name";
    private static final String KEY_CLASS_TABLE_PLACE = "place";
    private static final String KEY_CLASS_TABLE_INSTRUCTOR_NAME = "instructor_name";
    private static final String KEY_CLASS_TABLE_MODULE = "module";
    private static final String KEY_CLASS_TABLE_COLOR = "color";
    private static final String KEY_CLASS_TABLE_TERM_ID = "term_id";

    //times table - columns name
    private static final String KEY_TIMES_TABLE_ID = "id";
    private static final String KEY_TIMES_TABLE_DAY = "day";
    private static final String KEY_TIMES_TABLE_START_TIME = "start_time";
    private static final String KEY_TIMES_TABLE_DURATION = "duration";
    private static final String KEY_TIMES_TABLE_CLASS_ID = "class_id";
    private static final String KEY_TIMES_TABLE_TERM_ID = "term_id";

    //exams table - columns name
    private static final String KEY_EXAMS_TABLE_ID = "id";
    private static final String KEY_EXAMS_TABLE_PLACE = "place";
    private static final String KEY_EXAMS_TABLE_START_TIME = "start_time";
    private static final String KEY_EXAMS_TABLE_DURATION = "duration";
    private static final String KEY_EXAMS_TABLE_CLASS_ID = "class_id";
    private static final String KEY_EXAMS_TABLE_DATE = "exam_date";
    private static final String KEY_EXAMS_TABLE_MODULE = "module";


    //tables creation statements
    private static final String CREATE_TABLE_YEAR_SQL_STATEMENT = "CREATE TABLE " + TABLE_YEAR_NAME + "(" + KEY_YEAR_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_YEAR_TABLE_NAME + " TEXT, " + KEY_YEAR_TABLE_START_DATE + " TEXT, " + KEY_YEAR_TABLE_END_DATE + " TEXT " + ")";
    private static final String CREATE_TABLE_TERM_SQL_STATEMENT = "CREATE TABLE " + TABLE_TERM_NAME + "(" + KEY_TERM_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TERM_TABLE_NAME + " TEXT, " + KEY_TERM_TABLE_START_DATE + " TEXT, " + KEY_TERM_TABLE_END_DATE + " TEXT, " + KEY_TERM_TABLE_YEAR_ID + " INTEGER " + ")";
    private static final String CREATE_TABLE_CLASS_SQL_STATEMENT = "CREATE TABLE " + TABLE_CLASS_NAME + "(" + KEY_CLASS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CLASS_TABLE_NAME + " TEXT, " + KEY_CLASS_TABLE_MODULE + " TEXT, " + KEY_CLASS_TABLE_PLACE + " TEXT, " + KEY_CLASS_TABLE_INSTRUCTOR_NAME + " TEXT, " + KEY_CLASS_TABLE_COLOR + " INTEGER, " + KEY_CLASS_TABLE_TERM_ID + " INTEGER " + ")";
    private static final String CREATE_TABLE_TIMES_SQL_STATEMENT = "CREATE TABLE " + TABLE_TIMES_NAME + "(" + KEY_TIMES_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TIMES_TABLE_DAY + " TEXT, " + KEY_TIMES_TABLE_START_TIME + " TEXT, " + KEY_TIMES_TABLE_DURATION + " TEXT, " + KEY_TIMES_TABLE_CLASS_ID + " INTEGER, " + KEY_TIMES_TABLE_TERM_ID + " INTEGER " + ")";
    private static final String CREATE_TABLE_EXAMS_SQL_STATEMENT = "CREATE TABLE " + TABLE_EXAMS_NAME + "(" + KEY_EXAMS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_EXAMS_TABLE_PLACE + " TEXT, " + KEY_EXAMS_TABLE_MODULE + " TEXT, " + KEY_EXAMS_TABLE_START_TIME + " TEXT, " + KEY_EXAMS_TABLE_DURATION + " TEXT, " + KEY_EXAMS_TABLE_DATE + " TEXT, " + KEY_EXAMS_TABLE_CLASS_ID + " INTEGER " + ")";

    public static synchronized DBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_YEAR_SQL_STATEMENT);
        db.execSQL(CREATE_TABLE_TERM_SQL_STATEMENT);
        db.execSQL(CREATE_TABLE_CLASS_SQL_STATEMENT);
        db.execSQL(CREATE_TABLE_TIMES_SQL_STATEMENT);
        db.execSQL(CREATE_TABLE_EXAMS_SQL_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_YEAR_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_TERM_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_CLASS_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_TIMES_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_EXAMS_NAME);
        onCreate(db);
    }

    //Returns last inserted row id of classes table, THIS METHOD IS NEEDED WHILE INSERTING NEW TIME
    public int getLastInsertedRowID(String tabelname) {
        String KEY_TABEL_NAME = "";
        if (tabelname.equals("classes"))
            KEY_TABEL_NAME = TABLE_CLASS_NAME;
        else if (tabelname.equals("times"))
            KEY_TABEL_NAME = TABLE_TIMES_NAME;
        else if (tabelname.equals("terms"))
            KEY_TABEL_NAME = TABLE_TERM_NAME;
        else if (tabelname.equals("years"))
            KEY_TABEL_NAME = TABLE_YEAR_NAME;


        SQLiteDatabase db = this.getReadableDatabase();
        final String selectQuery = "SELECT MAX(id) FROM " + KEY_TABEL_NAME;
        Cursor cur = db.rawQuery(selectQuery, null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        return ID;
    }

    //Adding new year
    public void addYear(yearData year) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_YEAR_TABLE_NAME, year.getYearName());
        values.put(KEY_YEAR_TABLE_START_DATE, year.getStartDate());
        values.put(KEY_YEAR_TABLE_END_DATE, year.getEndDate());

        db.insert(TABLE_YEAR_NAME, null, values);
    }

    //adding new term
    public void addTerm(termData term) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TERM_TABLE_YEAR_ID, term.getYearID());
        values.put(KEY_TERM_TABLE_NAME, term.getTermName());
        values.put(KEY_TERM_TABLE_START_DATE, term.getStartDate());
        values.put(KEY_TERM_TABLE_END_DATE, term.getEndDate());

        db.insert(TABLE_TERM_NAME, null, values);
    }

    //adding new class_logo
    public void addClass(classData GivinClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CLASS_TABLE_TERM_ID, GivinClass.getTermID());
        values.put(KEY_CLASS_TABLE_NAME, GivinClass.getName());
        values.put(KEY_CLASS_TABLE_INSTRUCTOR_NAME, GivinClass.getInstructorName());
        values.put(KEY_CLASS_TABLE_MODULE, GivinClass.getModule());
        values.put(KEY_CLASS_TABLE_PLACE, GivinClass.getPlace());
        values.put(KEY_CLASS_TABLE_COLOR, GivinClass.getColor());

        db.insert(TABLE_CLASS_NAME, null, values);
    }

    //add new time
    public void addTime(timeData time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TIMES_TABLE_CLASS_ID, time.getClassID());
        values.put(KEY_TIMES_TABLE_DAY, time.getDay());
        values.put(KEY_TIMES_TABLE_DURATION, time.getDuration());
        values.put(KEY_TIMES_TABLE_START_TIME, time.getStartTime());
        values.put(KEY_TIMES_TABLE_TERM_ID, time.getTermID());
        db.insert(TABLE_TIMES_NAME, null, values);
    }

    //adding new Exam
    public void addExam(examData GivinExam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EXAMS_TABLE_PLACE, GivinExam.getPlace());
        values.put(KEY_EXAMS_TABLE_CLASS_ID, GivinExam.getClassID());
        values.put(KEY_EXAMS_TABLE_DURATION, GivinExam.getDuration());
        values.put(KEY_EXAMS_TABLE_MODULE, GivinExam.getModule());
        values.put(KEY_EXAMS_TABLE_START_TIME, GivinExam.getStartTime());
        values.put(KEY_EXAMS_TABLE_DATE, GivinExam.getDate());
        db.insert(TABLE_EXAMS_NAME, null, values);
    }

    //return All times for specific term from times table as an array list
    public ArrayList getAllTimes_ForSomeTerm(String TermID) {
        ArrayList<timeData> ctds = new ArrayList<timeData>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TIMES_NAME + " WHERE " + KEY_TIMES_TABLE_TERM_ID + " = " + TermID;
        Cursor c = db.rawQuery(selectQuery, null);
        c.getCount();
        if (c.moveToFirst()) {
            do {
                timeData ctd = new timeData();
                ctd.setDay(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_DAY)));
                ctd.setStartTime(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_START_TIME)));
                ctd.setDuration(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_DURATION)));
                ctd.setClassID(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_CLASS_ID)));
                ctd.setTermID(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_TERM_ID)));

                ctd.setID(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_ID)));

                String[] mStartTime_parts = ctd.getStartTime().split(":");
                int[] mEndTime_details = Utilities.get_EndTime_Using_StartTime(Integer.parseInt(mStartTime_parts[0]), Integer.parseInt(mStartTime_parts[1]), Integer.parseInt(ctd.getDuration()));
                String EndTime = mEndTime_details[0] + ":" + mEndTime_details[1];
                ctd.setEndTime(EndTime);

                ctds.add(ctd);
            } while (c.moveToNext());
        }
        c.close();
        return ctds;
    }

    //return All times  wih specific term and day name from times table as an array list
    public ArrayList getAllTimes_ForTheCalendar(String TermID, String DayName) {
        ArrayList<timeData> ctds = new ArrayList<timeData>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TIMES_NAME + " WHERE " + KEY_TIMES_TABLE_TERM_ID + " = " + TermID + " AND " + KEY_TIMES_TABLE_DAY + " = \"" + DayName + "\"";
        Cursor c = db.rawQuery(selectQuery, null);
        c.getCount();
        if (c.moveToFirst()) {
            do {
                timeData ctd = new timeData();
                ctd.setDay(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_DAY)));
                ctd.setStartTime(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_START_TIME)));
                ctd.setDuration(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_DURATION)));
                ctd.setClassID(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_CLASS_ID)));
                ctd.setTermID(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_TERM_ID)));
                ctd.setID(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_ID)));

                String[] mStartTime_parts = ctd.getStartTime().split(":");
                int[] mEndTime_details = Utilities.get_EndTime_Using_StartTime(Integer.parseInt(mStartTime_parts[0]), Integer.parseInt(mStartTime_parts[1]), Integer.parseInt(ctd.getDuration()));
                String EndTime = mEndTime_details[0] + ":" + mEndTime_details[1];
                ctd.setEndTime(EndTime);

                ctds.add(ctd);
            } while (c.moveToNext());
        }
        c.close();
        return ctds;
    }

    //return All Terms from terms table as an array list
    public ArrayList getAllTerms() {
        ArrayList<termData> termDataArrayList = new ArrayList<termData>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TERM_NAME;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                termData td = new termData();
                td.setName(c.getString(c.getColumnIndex(KEY_TERM_TABLE_NAME)));
                td.setStartDate(c.getString(c.getColumnIndex(KEY_TERM_TABLE_START_DATE)));
                td.setEndDate(c.getString(c.getColumnIndex(KEY_TERM_TABLE_END_DATE)));
                td.setYearID(c.getString(c.getColumnIndex(KEY_TERM_TABLE_YEAR_ID)));
                td.setID(c.getString(c.getColumnIndex(KEY_TERM_TABLE_ID)));
                termDataArrayList.add(td);
            } while (c.moveToNext());
        }
        c.close();

        Collections.sort(termDataArrayList, new ComparatorByDate());
        return termDataArrayList;
    }

    //return All Terms foe some year
    public ArrayList getAllTerms(yearData year) {
        ArrayList<termData> termDataArrayList = new ArrayList<termData>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TERM_NAME + " WHERE " + KEY_TERM_TABLE_YEAR_ID + " = " + year.getID();
        Cursor c = db.rawQuery(selectQuery, null);
//        Log.w("QQQQQQQQQQQQQQQQQQQQ", c.getCount() + "");
        if (c.moveToFirst()) {
            do {
                termData td = new termData();
                td.setName(c.getString(c.getColumnIndex(KEY_TERM_TABLE_NAME)));
                td.setStartDate(c.getString(c.getColumnIndex(KEY_TERM_TABLE_START_DATE)));
                td.setEndDate(c.getString(c.getColumnIndex(KEY_TERM_TABLE_END_DATE)));
                td.setYearID(c.getString(c.getColumnIndex(KEY_TERM_TABLE_YEAR_ID)));
                td.setID(c.getString(c.getColumnIndex(KEY_TERM_TABLE_ID)));
                termDataArrayList.add(td);
            } while (c.moveToNext());
        }
        c.close();

        Collections.sort(termDataArrayList, new ComparatorByDate());

        return termDataArrayList;
    }

    public ArrayList getAllYears() {
        ArrayList<yearData> yearsDataArrayList = new ArrayList<yearData>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_YEAR_NAME + " ORDER BY " + KEY_YEAR_TABLE_NAME;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                yearData td = new yearData();
                td.setNameYear(c.getString(c.getColumnIndex(KEY_YEAR_TABLE_NAME)));
                td.setStartDate(c.getString(c.getColumnIndex(KEY_YEAR_TABLE_START_DATE)));
                td.setEndDate(c.getString(c.getColumnIndex(KEY_YEAR_TABLE_END_DATE)));
                td.setID(c.getString(c.getColumnIndex(KEY_YEAR_TABLE_ID)));
                yearsDataArrayList.add(td);
            } while (c.moveToNext());
        }
        c.close();
        return yearsDataArrayList;
    }

    //get single year by ID
    public yearData getYear(String yId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_YEAR_NAME + " WHERE " + KEY_YEAR_TABLE_ID + " = " + yId;
        Cursor c = db.rawQuery(selectQuery, null);
        yearData yYear = new yearData();
        if (c.moveToFirst()) {
            yYear.setID(c.getString(c.getColumnIndex(KEY_YEAR_TABLE_ID)));
            yYear.setNameYear(c.getString(c.getColumnIndex(KEY_YEAR_TABLE_NAME)));
            yYear.setStartDate(c.getString(c.getColumnIndex(KEY_YEAR_TABLE_START_DATE)));
            yYear.setEndDate(c.getString(c.getColumnIndex(KEY_YEAR_TABLE_END_DATE)));
        }
        c.close();
        return yYear;
    }

    //get single term by ID
    public termData getTerm(String tId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TERM_NAME + " WHERE " + KEY_YEAR_TABLE_ID + " = " + tId;
        Cursor c = db.rawQuery(selectQuery, null);
        termData tTerm = new termData();
        if (c.moveToFirst()) {
            tTerm.setID(c.getString(c.getColumnIndex(KEY_TERM_TABLE_ID)));
            tTerm.setName(c.getString(c.getColumnIndex(KEY_TERM_TABLE_NAME)));
            tTerm.setStartDate(c.getString(c.getColumnIndex(KEY_TERM_TABLE_START_DATE)));
            tTerm.setEndDate(c.getString(c.getColumnIndex(KEY_TERM_TABLE_END_DATE)));
            tTerm.setYearID(c.getString(c.getColumnIndex(KEY_TERM_TABLE_YEAR_ID)));
        }
        c.close();
        return tTerm;
    }

    public classData getClass(String ClassID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CLASS_NAME + " WHERE " + KEY_CLASS_TABLE_ID + " = " + ClassID;
        Cursor c = db.rawQuery(selectQuery, null);
        classData Class = new classData();
        if (c != null) {
            c.moveToFirst();
            Class.setID(c.getInt(c.getColumnIndex(KEY_CLASS_TABLE_ID)) + "");
            Class.setClassName(c.getString(c.getColumnIndex(KEY_CLASS_TABLE_NAME)));
            Class.setInstructorName(c.getString(c.getColumnIndex(KEY_CLASS_TABLE_INSTRUCTOR_NAME)));
            Class.setModule(c.getString(c.getColumnIndex(KEY_CLASS_TABLE_MODULE)));
            Class.setPlace(c.getString(c.getColumnIndex(KEY_CLASS_TABLE_PLACE)));
            Class.setColor(c.getString(c.getColumnIndex(KEY_CLASS_TABLE_COLOR)));
            Class.setTermID(c.getString(c.getColumnIndex(KEY_CLASS_TABLE_TERM_ID)));
            c.close();
        }
        return Class;
    }

    public ArrayList<timeData> getAllTimes_ForSomeClass(String ClassId) {
        ArrayList<timeData> ctds = new ArrayList<timeData>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TIMES_NAME + " WHERE " + KEY_TIMES_TABLE_CLASS_ID + " = " + ClassId;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                timeData ctd = new timeData();
                ctd.setDay(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_DAY)));
                ctd.setStartTime(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_START_TIME)));
                ctd.setDuration(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_DURATION)));
                ctd.setClassID(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_CLASS_ID)));
                ctd.setTermID(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_TERM_ID)));
                ctd.setID(c.getString(c.getColumnIndex(KEY_TIMES_TABLE_ID)));

                String[] mStartTime_parts = ctd.getStartTime().split(":");
                int[] mEndTime_details = Utilities.get_EndTime_Using_StartTime(Integer.parseInt(mStartTime_parts[0]), Integer.parseInt(mStartTime_parts[1]), Integer.parseInt(ctd.getDuration()));
                String EndTime = mEndTime_details[0] + ":" + mEndTime_details[1];
                ctd.setEndTime(EndTime);

                ctds.add(ctd);
            } while (c.moveToNext());
        }
        c.close();
        return ctds;
    }

    public ArrayList<classData> getAllClasses(String TermID) {
        ArrayList<classData> Classes = new ArrayList<classData>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CLASS_NAME + " WHERE " + KEY_CLASS_TABLE_TERM_ID + " = " + TermID;
        Cursor c = db.rawQuery(selectQuery, null);
        int x = c.getCount();
        if (c.moveToFirst()) {
            do {
                classData td = new classData();
                td.setID(c.getString(c.getColumnIndex(KEY_CLASS_TABLE_ID)));
                td.setClassName(c.getString(c.getColumnIndex(KEY_CLASS_TABLE_NAME)));
                td.setModule(c.getString(c.getColumnIndex(KEY_CLASS_TABLE_MODULE)));
                td.setTermID(c.getString(c.getColumnIndex(KEY_CLASS_TABLE_TERM_ID)));
                td.setInstructorName(c.getString(c.getColumnIndex(KEY_CLASS_TABLE_INSTRUCTOR_NAME)));
                td.setPlace(c.getString(c.getColumnIndex(KEY_CLASS_TABLE_PLACE)));
                td.setColor(c.getString(c.getColumnIndex(KEY_CLASS_TABLE_COLOR)));
                Classes.add(td);
            } while (c.moveToNext());
        }
        c.close();
        return Classes;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //DELETE
    public void deleteExam(String ExamID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXAMS_NAME, KEY_EXAMS_TABLE_ID + " = ? ", new String[]{String.valueOf(ExamID)});
    }

    public void deleteTime(String TimeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TIMES_NAME, KEY_TIMES_TABLE_ID + " = ? ", new String[]{String.valueOf(TimeID)});
    }


    public void deleteALLClassExams(String classId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXAMS_NAME, KEY_EXAMS_TABLE_CLASS_ID + " = ? ", new String[]{String.valueOf(classId)});
    }

    public void deleteALLClassTimes(String classid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TIMES_NAME, KEY_TIMES_TABLE_CLASS_ID + " = ? ", new String[]{String.valueOf(classid)});
    }

    public void deleteClass(String classID) {
        SQLiteDatabase db = this.getWritableDatabase();

        deleteALLClassTimes(classID);

        deleteALLClassExams(classID);

        db.delete(TABLE_CLASS_NAME, KEY_CLASS_TABLE_ID + "= ?", new String[]{String.valueOf(classID)});
    }

    public void deleteTerm(String termID) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<classData> allClasses = getAllClasses(termID);
        for (classData classes : allClasses) {
            deleteClass(classes.getID());
        }
        db.delete(TABLE_TERM_NAME, KEY_TERM_TABLE_ID + "= ?", new String[]{String.valueOf(termID)});
    }

    public void deleteYear(String yearID) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<termData> YearTerms = getAllTerms(getYear(yearID));
        for (termData term : YearTerms) {
            deleteTerm(term.getID());
        }
        db.delete(TABLE_YEAR_NAME, KEY_YEAR_TABLE_ID + "= ?", new String[]{String.valueOf(yearID)});
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Update
    public void UpdateYear(String yearID, yearData New_Year) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_YEAR_TABLE_NAME, New_Year.getYearName());
        values.put(KEY_YEAR_TABLE_START_DATE, New_Year.getStartDate());
        values.put(KEY_YEAR_TABLE_END_DATE, New_Year.getEndDate());

        db.update(TABLE_YEAR_NAME, values, KEY_YEAR_TABLE_ID + "= ?", new String[]{yearID});
    }

    public void UpdateTerm(termData New_Term) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TERM_TABLE_YEAR_ID, New_Term.getYearID());
        values.put(KEY_TERM_TABLE_NAME, New_Term.getTermName());
        values.put(KEY_TERM_TABLE_START_DATE, New_Term.getStartDate());
        values.put(KEY_TERM_TABLE_END_DATE, New_Term.getEndDate());

        db.update(TABLE_TERM_NAME, values, KEY_TERM_TABLE_ID + "= ?", new String[]{String.valueOf(New_Term.getID())});
    }

    //adding new class_logo
    public void UpdateClass(classData NewClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CLASS_TABLE_TERM_ID, NewClass.getTermID());
        values.put(KEY_CLASS_TABLE_NAME, NewClass.getName());
        values.put(KEY_CLASS_TABLE_INSTRUCTOR_NAME, NewClass.getInstructorName());
        values.put(KEY_CLASS_TABLE_MODULE, NewClass.getModule());
        values.put(KEY_CLASS_TABLE_PLACE, NewClass.getPlace());
        values.put(KEY_CLASS_TABLE_COLOR, NewClass.getColor());

        db.update(TABLE_CLASS_NAME, values, KEY_CLASS_TABLE_ID + "= ?", new String[]{String.valueOf(NewClass.getID())});
    }

    //add new time
    public void UpdateTime(timeData New_Time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TIMES_TABLE_CLASS_ID, New_Time.getClassID());
        values.put(KEY_TIMES_TABLE_DAY, New_Time.getDay());
        values.put(KEY_TIMES_TABLE_DURATION, New_Time.getDuration());
        values.put(KEY_TIMES_TABLE_START_TIME, New_Time.getStartTime());
        values.put(KEY_TIMES_TABLE_TERM_ID, New_Time.getTermID());

        db.update(TABLE_TIMES_NAME, values, KEY_TIMES_TABLE_ID + "= ?", new String[]{String.valueOf(New_Time.getID())});

    }

    public ArrayList<examData> getAllExams_forAllClasses_InSomeTerm(String currentSelectedTermID) {

        ArrayList<classData> Classes = getAllClasses(currentSelectedTermID);
        ArrayList<examData> Exams = new ArrayList<examData>();
        for (classData temp_class : Classes) {
            Exams.addAll(getExams_for_SomeClass(temp_class.getID()));
        }
        Collections.sort(Exams, new ComparatorByDate());
        return Exams;
    }

    public ArrayList<examData> getExams_for_SomeClass(String mClassID) {
        ArrayList<examData> Exams = new ArrayList<examData>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_EXAMS_NAME + " WHERE " + KEY_EXAMS_TABLE_CLASS_ID + " = " + mClassID;
        Cursor c = db.rawQuery(selectQuery, null);
        int x = c.getCount();
        if (c.moveToFirst()) {
            do {
                examData td = new examData();
                td.setID(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_ID)));
                td.setPlace(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_PLACE)));
                td.setDuration(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_DURATION)));
                td.setStartTime(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_START_TIME)));
                td.setClassID(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_CLASS_ID)));
                td.setDate(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_DATE)));
                td.setModule(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_MODULE)));
                Exams.add(td);
            } while (c.moveToNext());
        }
        c.close();
        Collections.sort(Exams, new ComparatorByDate());
        return Exams;
    }

    public examData getExam(String mExamID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_EXAMS_NAME + " WHERE " + KEY_EXAMS_TABLE_ID + " = " + mExamID;
        Cursor c = db.rawQuery(selectQuery, null);
        examData exam = new examData();
        if (c.moveToFirst()) {
            exam.setID(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_ID)));
            exam.setClassID(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_CLASS_ID)));
            exam.setDate(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_DATE)));
            exam.setModule(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_MODULE)));
            exam.setPlace(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_PLACE)));
            exam.setStartTime(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_START_TIME)));
            exam.setDuration(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_DURATION)));
        }
        c.close();
        return exam;
    }

    public void UpdateExam(examData mExam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EXAMS_TABLE_DATE, mExam.getDate());
        values.put(KEY_EXAMS_TABLE_PLACE, mExam.getPlace());
        values.put(KEY_EXAMS_TABLE_DURATION, mExam.getDuration());
        values.put(KEY_EXAMS_TABLE_START_TIME, mExam.getStartTime());
        values.put(KEY_EXAMS_TABLE_CLASS_ID, mExam.getClassID());
        values.put(KEY_EXAMS_TABLE_MODULE, mExam.getModule());

        db.update(TABLE_EXAMS_NAME, values, KEY_EXAMS_TABLE_ID + "= ?", new String[]{String.valueOf(mExam.getID())});

    }

    public ArrayList<examData> getExams_byDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_EXAMS_NAME + " WHERE " + KEY_EXAMS_TABLE_DATE + " = " + "\""+ date + "\"";
        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<examData> exams = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                examData exam = new examData();
                exam.setID(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_ID)));
                exam.setClassID(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_CLASS_ID)));
                exam.setDate(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_DATE)));
                exam.setModule(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_MODULE)));
                exam.setPlace(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_PLACE)));
                exam.setStartTime(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_START_TIME)));
                exam.setDuration(c.getString(c.getColumnIndex(KEY_EXAMS_TABLE_DURATION)));
                exams.add(exam);
            } while (c.moveToNext());
        }
        c.close();
        return exams;
    }
}
