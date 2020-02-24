package com.example.atefhares.study_organizer.UtilitiesClasses;

import com.example.atefhares.study_organizer.DataClasses.examData;
import com.example.atefhares.study_organizer.DataClasses.termData;

import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by Atef Hares on 27-May-16.
 */
public class ComparatorByDate implements Comparator {

    @Override
    public int compare(Object lhs, Object rhs) {
        termData rhs_term;
        termData lhs_term;
        String lhs_startDate_details[],rhs_startDate_details[];

        examData rhs_exam;
        examData lhs_exam;
        if (lhs instanceof termData && rhs instanceof termData) {
            lhs_term = (termData) lhs;
            rhs_term = (termData) rhs;
            lhs_startDate_details = lhs_term.getStartDate().split("/");
            rhs_startDate_details = rhs_term.getStartDate().split("/");

        }else if(lhs instanceof examData && rhs instanceof examData){
            lhs_exam = (examData) lhs;
            rhs_exam = (examData) rhs;
            lhs_startDate_details = lhs_exam.getDate().split("/");
            rhs_startDate_details = rhs_exam.getDate().split("/");

        } else {
            // if you want to add another types to be compared
            return 0;
        }

        //General piece of code
        Calendar lhs_startDate = Calendar.getInstance();
        lhs_startDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(lhs_startDate_details[0]));
        lhs_startDate.set(Calendar.MONTH, Integer.parseInt(lhs_startDate_details[1]));
        lhs_startDate.set(Calendar.YEAR, Integer.parseInt(lhs_startDate_details[2]));

        Calendar rhs_startDate = Calendar.getInstance();
        rhs_startDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(rhs_startDate_details[0]));
        rhs_startDate.set(Calendar.MONTH, Integer.parseInt(rhs_startDate_details[1]));
        rhs_startDate.set(Calendar.YEAR, Integer.parseInt(rhs_startDate_details[2]));

        if (lhs_startDate.getTimeInMillis() > rhs_startDate.getTimeInMillis()) {
            return +1;

        } else if (lhs_startDate.getTimeInMillis() == rhs_startDate.getTimeInMillis()) {
            return 0;

        } else {
            return -1;
        }

    }
}
