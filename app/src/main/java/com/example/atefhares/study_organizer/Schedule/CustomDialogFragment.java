package com.example.atefhares.study_organizer.Schedule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atefhares.study_organizer.DataClasses.termData;
import com.example.atefhares.study_organizer.DataClasses.yearData;
import com.example.atefhares.study_organizer.DatabaseRelated.DBHelper;
import com.example.atefhares.study_organizer.R;
import com.example.atefhares.study_organizer.UtilitiesClasses.Utilities;

import java.util.ArrayList;

/**
 * Created by Atef Hares on 30-Apr-16.
 */
public class CustomDialogFragment extends android.app.DialogFragment {
    private DBHelper mdbHelper;
    LinearLayout ViewsLL;
    String CurrentSelectedTermID = new String();
    String CurrentSelectedYearID = new String();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog,null);
        getDialog().setTitle("Filter by Year/Term");


        mdbHelper = new DBHelper(getActivity());

        TextView Show_Current_Term_TV = (TextView) rootView.findViewById(R.id.Current_Term);
        Show_Current_Term_TV.setBackgroundResource(R.drawable.ripple_effect_square);
        ViewsLL = (LinearLayout) rootView.findViewById(R.id.VL);

        Show_Current_Term_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean there_exists_a_current_year_and_term = false;
                ArrayList<yearData> yearData_ArrayList = mdbHelper.getAllYears();
                if(yearData_ArrayList != null && !yearData_ArrayList.isEmpty()) {
                    for (yearData year : yearData_ArrayList) {
                        ArrayList<termData> Year_terms_ArrayList = mdbHelper.getAllTerms(year);
                        if (Utilities.year_is_Current_year(year)) {
                            CurrentSelectedYearID = year.getID();
                            for (termData term : Year_terms_ArrayList) {
                                if (Utilities.this_term_is_the_current_term(term)) {
                                    CurrentSelectedTermID = term.getID();
                                    sendResult(CurrentSelectedYearID,CurrentSelectedTermID);
                                    dismiss();
                                    there_exists_a_current_year_and_term = true;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    if(!there_exists_a_current_year_and_term){
                        Toast.makeText(getActivity(),"There are no Current Year/Term",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(!there_exists_a_current_year_and_term){
                        Toast.makeText(getActivity(),"You haven't added any Years/Terms",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        Bundle bundle = getArguments();
        if(bundle!=null){
            CurrentSelectedTermID = bundle.getString("mSelectedTermID");
            CurrentSelectedYearID = bundle.getString("mSelectedYearID");
        }

        Show_Dialog_Data();

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    private void Show_Dialog_Data() {
        ArrayList<yearData> yearData_ArrayList = mdbHelper.getAllYears();
        if(yearData_ArrayList != null && !yearData_ArrayList.isEmpty()) {
            for (yearData year : yearData_ArrayList) {
                ArrayList<termData> Year_terms_ArrayList = mdbHelper.getAllTerms(year);
                CreateViews(Year_terms_ArrayList, year);
            }
        }
    }

    private void CreateViews(final ArrayList<termData> Terms, final yearData year) {

        LinearLayout.LayoutParams Space_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 5 );
        LinearLayout.LayoutParams line_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 1);


        //step 0
        final LinearLayout Main_linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams Main_linearLayout_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        Main_linearLayout.setLayoutParams(Main_linearLayout_params);
        Main_linearLayout.setOrientation(LinearLayout.VERTICAL);
        Main_linearLayout.setPadding(15,15,15,15);
        ViewsLL.addView(Main_linearLayout);

        final Space space_above = new Space(getActivity());
        space_above.setLayoutParams(Space_params);
        ViewsLL.addView(space_above);

        View line1 = new View(getActivity());
        line1.setLayoutParams(line_params);
        line1.setBackgroundColor(getResources().getColor(R.color.LayoutDivider));
        ViewsLL.addView(line1);

//        final Space Space_blew = new Space(getActivity());
//        Space_blew.setLayoutParams(Space_params);
//        ViewsLL.addView(Space_blew);

        //step 1
        LinearLayout H_linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams H_linearLayout_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        H_linearLayout.setLayoutParams(H_linearLayout_params);
        H_linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        Main_linearLayout.addView(H_linearLayout);

        //step 2
        LinearLayout V_linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams V_linearLayout_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        V_linearLayout.setLayoutParams(V_linearLayout_params);
        V_linearLayout.setOrientation(LinearLayout.VERTICAL);
        V_linearLayout.setPadding(10,0,0,0);
        H_linearLayout.addView(V_linearLayout);
                               /*+=+=+=+=+=+=+==+=+=+=+=+==+=+=+=+=+=+=+=+=+=+=+=+=+=+==+=+=+=+=+=+=+=+=+==+=+=+=+=*/
        TextView YearNameTV = new TextView(getActivity());
        LinearLayout.LayoutParams YearDateTV_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        YearNameTV.setLayoutParams(YearDateTV_params);
        YearNameTV.setText(year.getYearName());
        YearNameTV.setTextSize(20);
        V_linearLayout.addView(YearNameTV);
                               /*+=+=+=+=+=+=+==+=+=+=+=+==+=+=+=+=+=+=+=+=+=+=+=+=+=+==+=+=+=+=+=+=+=+=+==+=+=+=+=*/
        final Space space_between = new Space(getActivity());
        space_between.setLayoutParams(Space_params);
        V_linearLayout.addView(space_between);

        TextView YearDateTV = new TextView(getActivity());
        YearDateTV.setLayoutParams(YearDateTV_params);
        String yearStartDate_details[] = Utilities.get_Date_Correct_Style(year.getStartDate());
        String yearEndDate_details[] = Utilities.get_Date_Correct_Style(year.getEndDate());
        YearDateTV.setText(yearStartDate_details[0] + " " + yearStartDate_details[1] + ", " +yearStartDate_details[2]
                + "  -  " +yearEndDate_details[0] + " " + yearEndDate_details[1] + ", " +yearEndDate_details[2]);
        V_linearLayout.addView(YearDateTV);

        //step 3
        TextView Terms_NumberTV = new TextView(getActivity());
        LinearLayout.LayoutParams Terms_NumberTV_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Terms_NumberTV_params.gravity = Gravity.CENTER;
        Terms_NumberTV.setLayoutParams(Terms_NumberTV_params);
        Terms_NumberTV.setText(Terms.size()+ " Terms");
        Terms_NumberTV.setGravity(Gravity.CENTER );
        Terms_NumberTV.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        H_linearLayout.addView(Terms_NumberTV);

        //step4
        final ImageView dropdown = new ImageView(getActivity());
        dropdown.setImageResource(R.drawable.dropdownleft);
        LinearLayout.LayoutParams dropdown_params = new LinearLayout.LayoutParams(20, 20);
        dropdown_params.gravity = Gravity.CENTER;
        dropdown.setLayoutParams(dropdown_params);
        dropdown.setPadding(5,0,0,0);
        H_linearLayout.addView(dropdown);




        final Space space_Above = new Space(getActivity());
        space_Above.setLayoutParams(Space_params);
        Main_linearLayout.addView(space_Above);

        final LinearLayout TermsLL = new LinearLayout(getActivity());
        TermsLL.setOrientation(LinearLayout.VERTICAL);
        TermsLL.setPadding(30,10,10,10);

        Terms_NumberTV.setBackgroundResource(R.drawable.ripple_effect_square);
        final boolean[] Show = {true};
        Terms_NumberTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Show[0]){
                    ShowTerms(year,Terms,TermsLL);
                    Main_linearLayout.addView(TermsLL);
                    Show[0] =false;
                    dropdown.setImageResource(R.drawable.dropdowndown);
                }else{
                    TermsLL.removeAllViews();
                    Main_linearLayout.removeView(TermsLL);
                    Show[0] =true;
                    dropdown.setImageResource(R.drawable.dropdownleft);
                }
            }
        });

        final Space space_blew = new Space(getActivity());
        space_blew.setLayoutParams(Space_params);
        Main_linearLayout.addView(space_blew);



        if(!CurrentSelectedYearID.equals("")) {
            if (CurrentSelectedYearID.equals(year.getID())) {
                ShowTerms(year, Terms, TermsLL);
                Main_linearLayout.addView(TermsLL);
                Show[0] = false;
                dropdown.setImageResource(R.drawable.dropdowndown);
            }
        }
    }

    private void ShowTerms(final yearData year, ArrayList<termData> terms, LinearLayout termsLL) {

        LinearLayout.LayoutParams space_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 5);
        LinearLayout.LayoutParams line_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);

        for (final termData data : terms){


            final Space space3 = new Space(getActivity());
            space3.setLayoutParams(space_params);
            termsLL.addView(space3);


            View line2 = new View(getActivity());
            line2.setLayoutParams(line_params);
            line2.setBackgroundColor(getResources().getColor(R.color.LayoutDivider));
            termsLL.addView(line2);


            final Space space4 = new Space(getActivity());
            space4.setLayoutParams(space_params);
            termsLL.addView(space4);

            LinearLayout linearLayout = new LinearLayout(getActivity());
            LinearLayout.LayoutParams params1 = new  LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(params1);
            linearLayout.setPadding(30,5,5,5);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setBackgroundResource(R.drawable.ripple_effect_square);

            final TextView termNameTV = new TextView(getActivity());
            LinearLayout.LayoutParams params2 = new  LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            termNameTV.setLayoutParams(params2);
            termNameTV.setText(data.getTermName());
            termNameTV.setTextSize(20);
            linearLayout.addView(termNameTV);

            final Space space1 = new Space(getActivity());
            space1.setLayoutParams(space_params);
            linearLayout.addView(space1);

            final TextView TermDateTV = new TextView(getActivity());
            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TermDateTV.setLayoutParams(params3);
            TermDateTV.setTextSize(15);
            String[] termStartDate_details= Utilities.get_Date_Correct_Style(data.getStartDate());
            String[] termEndDate_details= Utilities.get_Date_Correct_Style(data.getEndDate());
            TermDateTV.setText(termStartDate_details[0] + " " +termStartDate_details[1] +", " +termStartDate_details[2]
            + "   -   " + termEndDate_details[0] + " " +termEndDate_details[1] +", " +termEndDate_details[2]);
            linearLayout.addView(TermDateTV);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    termNameTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    TermDateTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    sendResult(year.getID(),data.getID());
                    dismiss();
                }
            });

            final Space space2 = new Space(getActivity());
            space2.setLayoutParams(space_params);
            linearLayout.addView(space2);

            termsLL.addView(linearLayout);

            if(!CurrentSelectedTermID.equals("AA")) {
                if (CurrentSelectedTermID.equals(data.getID())) {
                    termNameTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    TermDateTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        }
    }

    private void sendResult(String YearID, String termID) {
        Intent intent = new Intent();
        intent.putExtra("mSelectedYearID", YearID);
        intent.putExtra("mSelectedTermID", termID);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }
}
