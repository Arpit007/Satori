package com.dauntless.starkx.satori.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dauntless.starkx.satori.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sonu on 7/10/17.
 */

public class ImagePickerAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private ArrayList<String> mFilters  = new ArrayList<>();


    public ImagePickerAdapter(Context mContext , ArrayList<String>  mFilters){
        super(mContext, R.layout.image_picker_grid, mFilters);
        this.mContext = mContext;
        this.mFilters = mFilters;
    }

    static class ViewHolder {
        ImageView image;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.image_picker_grid, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.filter_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Picasso.with(mContext)
                .load(mFilters.get(position))
                .fit().centerInside()
                .placeholder(R.drawable.loading_fail)
                .error(R.drawable.loading_fail)
                .into(holder.image);

        return row;
    }
}
