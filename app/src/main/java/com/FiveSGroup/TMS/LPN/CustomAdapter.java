package com.FiveSGroup.TMS.LPN;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.FiveSGroup.TMS.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;


    ArrayList<String> arrDate;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext,  ArrayList<String> arrDate) {
        this.context = applicationContext;
        this.arrDate = arrDate;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return arrDate.size();
    }

    @Override
    public Object getItem(int i) {
        return arrDate.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.layout_custom_item_spinner, null);
        TextView names = (TextView) convertView.findViewById(R.id.textView);
        names.setText(arrDate.get(position));
        return convertView;
    }


}
