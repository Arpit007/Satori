package com.dauntless.starkx.satori.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dauntless.starkx.satori.Model.ChatList;
import com.dauntless.starkx.satori.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sonu on 7/10/17.
 */

public class ChatListAdapter extends ArrayAdapter<ChatList> {
	ArrayList<ChatList> chatList;

	public ChatListAdapter(ArrayList<ChatList> chatList, Context mContext) {
		super(mContext, R.layout.chat_list, chatList);
		this.chatList = chatList;
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.chat_list, parent, false);
		}

		ChatList chatItem = getItem(position);
		ViewHolder viewHolder = new ViewHolder(convertView);

		viewHolder.receiverName.setText(chatItem.receiverName);
		viewHolder.receiverNumber.setText(chatItem.receiverNumber);

		Picasso.with(getContext())
				.load(chatItem.chatUrl)
				.fit().centerInside()
				.placeholder(R.drawable.loading_fail)
				.error(R.drawable.loading_fail)
				.into(viewHolder.receiverImage);

		return convertView;
	}

	public class ViewHolder {
		ImageView receiverImage;
		TextView receiverName;
		TextView receiverNumber;

		ViewHolder(View view) {
			receiverName = (TextView) view.findViewById(R.id.name);
			receiverNumber = (TextView) view.findViewById(R.id.number);
			receiverImage = (ImageView) view.findViewById(R.id.userImage);
		}
	}
}
