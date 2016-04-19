package com.stumbleapp.stumble;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stumbleapp.me.stumble.R;

/**
 * Created by Me on 15/04/2016.
 */
class CustomAdapter extends ArrayAdapter{

    String[] locations;
    public CustomAdapter(Context context, String[] streams) {
        super(context, R.layout.list_item_streams ,streams);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater listInflater = LayoutInflater.from(getContext());
        View customView = listInflater.inflate(R.layout.list_item_streams, parent, false);

        String name = getItem(position).toString();
        TextView nameText = (TextView) customView.findViewById(R.id.list_item_name_textview);
        nameText.setText(name);
;
        return customView;
    }
}


