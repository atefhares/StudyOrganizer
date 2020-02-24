package com.example.atefhares.study_organizer.NewOrEditActivities;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.atefhares.study_organizer.R;

/**
 * Created by Atef Hares on 22-Apr-16.
 */

class CustomSpinnerAdapter extends ArrayAdapter<Integer> {
    private Context mContext;
    private Integer  [] mColors;
    public CustomSpinnerAdapter(Context context, int resource, Integer [] Colors) {
        super(context, resource,Colors);
        mContext=context;
        mColors=Colors;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }




    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        TextView circle;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.color_spinner_row, parent, false);
            circle = (TextView) convertView.findViewById(R.id.circle);
            convertView.setTag(circle);
        } else
            circle = (TextView) convertView.getTag();

        // Do everything you want with  "circle"
        GradientDrawable bgShape = (GradientDrawable)circle.getBackground();
        bgShape.setColor(mContext.getResources().getColor(mColors[position]));
        return convertView;
    }
}
