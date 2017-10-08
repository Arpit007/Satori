package com.dauntless.starkx.satori.Adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.dauntless.starkx.satori.R;
import com.dauntless.starkx.satori.lib.MessagePasser;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by sonu on 7/10/17.
 */

public class ImagePickerAdapter extends ArrayAdapter<String> {
	private WeakReference<MessagePasser> messagePasserReference;
	private ArrayList<String> mFilters = new ArrayList<>();
	private final Integer RESPONSE_CODE;

	public ImagePickerAdapter(Context mContext, ArrayList<String> mFilters, Integer responseCode, MessagePasser messagePasser) {
		super(mContext, R.layout.image_picker_grid, mFilters);
		messagePasserReference = new WeakReference<>(messagePasser);
		this.mFilters = mFilters;
		this.RESPONSE_CODE = responseCode;
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.image_picker_grid, parent, false);
		}

		final ImageView imageView = (ImageView) convertView.findViewById(R.id.filter_image);

		Picasso.with(getContext())
				.load(mFilters.get(position))
				.fit().centerInside()
				.placeholder(R.drawable.loading_fail)
				.error(R.drawable.loading_fail)
				.into(imageView);

		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				messagePasserReference.get()
						.PassMessage(( (BitmapDrawable) imageView.getDrawable() ).getBitmap(), RESPONSE_CODE);
			}
		});

		return convertView;
	}
}
