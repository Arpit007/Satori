package com.dauntless.starkx.satori.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dauntless.starkx.satori.Model.ChatList;
import com.dauntless.starkx.satori.Model.Contacts;
import com.dauntless.starkx.satori.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sonu on 7/10/17.
 */

public class ChatListAdapter  extends ArrayAdapter<ChatList> {

    private Activity mActivity;
    private ArrayList<ChatList> chatList;
    private Context mContext;

    public ChatListAdapter(Activity activity, ArrayList<ChatList> chatList, Context mContext) {
        super(mContext, R.layout.chat_list, chatList);
        this.mActivity = activity;
        this.chatList = chatList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ChatList C = getItem(position);
        ViewHolder viewHolder;
        View vi;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.chat_list, parent, false);
            viewHolder.receiverName = (TextView) convertView.findViewById(R.id.cotactName);
            viewHolder.receiverNumber = (TextView) convertView.findViewById(R.id.cotactNumber);
            viewHolder.receiverImage = (ImageView) convertView.findViewById(R.id.userImage);
            vi = convertView;
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            vi = convertView;
        }
        viewHolder.receiverName.setText(C.receiverName);
        viewHolder.receiverNumber.setText(C.receiverNumber);
        Picasso.with(mContext)
                .load(C.chatUrl)
                .fit().centerInside()
                .placeholder(R.drawable.loading_fail)
                .error(R.drawable.loading_fail)
                .into(viewHolder.receiverImage);
        return vi;
    }
    public class ViewHolder {
        ImageView receiverImage;
        TextView receiverName;
        TextView receiverNumber;
    }
}
