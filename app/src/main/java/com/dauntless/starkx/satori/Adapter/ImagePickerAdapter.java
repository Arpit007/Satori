package com.dauntless.starkx.satori.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import com.dauntless.starkx.satori.ui.Home;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sonu on 7/10/17.
 */

public class ImagePickerAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private Activity mActivity;
    private ArrayList<String> mFilters  = new ArrayList<>();
    private final Integer RESPONSE_CODE_HEAD_FILETR  ;

    public ImagePickerAdapter(Context mContext,Activity mActivity , ArrayList<String>  mFilters , Integer responseCode){
        super(mContext, R.layout.image_picker_grid, mFilters);
        this.mContext = mContext;
        this.mFilters = mFilters;
        this.mActivity = mActivity;
        this.RESPONSE_CODE_HEAD_FILETR = responseCode;
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

        final ViewHolder finalHolder = holder;
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, Home.class);
                BitmapDrawable drawable = (BitmapDrawable) finalHolder.image.getDrawable();
                Bitmap bmp = drawable.getBitmap();
                mIntent.putExtra("bmp_img", bmp);
                mActivity.setResult(RESPONSE_CODE_HEAD_FILETR, mIntent);
                mActivity.finish();
            }
        });
        return row;
    }
}
