package com.dauntless.starkx.satori.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dauntless.starkx.satori.Adapter.ChatListAdapter;
import com.dauntless.starkx.satori.Model.ChatList;
import com.dauntless.starkx.satori.R;

import java.util.ArrayList;

/**
 * Created by sonu on 6/10/17.
 */

public class FragmentChat extends Fragment {

	private ListView chatList;
	private ChatListAdapter chatListAdapter;
	private ArrayList<ChatList> chatListsDetails;

	public FragmentChat() {
	}

	public static FragmentChat newInstance() {
		FragmentChat fragment = new FragmentChat();
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
		chatList = (ListView) rootView.findViewById(R.id.chatList);

		return rootView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		chatListsDetails = new ArrayList<>();

		chatListsDetails.add(new ChatList("http://smalldata.io/img/sdl_logo.png", "Narendra", "9660729583"));
		chatListsDetails.add(new ChatList("https://cdn1.iconfinder.com/data/icons/metro-ui-dock-icon-set--icons-by-dakirby/512/CD.png", "Arpit", "+919711431657"));
		chatListsDetails.add(new ChatList("https://img3.stockfresh.com/files/r/rastudio/m/46/6638876_stock-vector-bunch-of-grapes-line-icon.jpg", "Rajesh", "9660729583"));
		chatListsDetails.add(new ChatList("http://smalldata.io/img/sdl_logo.png", "Narendra", "9660729583"));
		chatListsDetails.add(new ChatList("https://cdn1.iconfinder.com/data/icons/metro-ui-dock-icon-set--icons-by-dakirby/512/CD.png", "Arpit", "+919711431657"));
		chatListsDetails.add(new ChatList("https://img3.stockfresh.com/files/r/rastudio/m/46/6638876_stock-vector-bunch-of-grapes-line-icon.jpg", "Rajesh", "9660729583"));
		chatListsDetails.add(new ChatList("http://smalldata.io/img/sdl_logo.png", "Narendra", "9660729583"));
		chatListsDetails.add(new ChatList("https://cdn1.iconfinder.com/data/icons/metro-ui-dock-icon-set--icons-by-dakirby/512/CD.png", "Arpit", "+919711431657"));
		chatListsDetails.add(new ChatList("https://img3.stockfresh.com/files/r/rastudio/m/46/6638876_stock-vector-bunch-of-grapes-line-icon.jpg", "Rajesh", "9660729583"));
		chatListsDetails.add(new ChatList("http://smalldata.io/img/sdl_logo.png", "Narendra", "9660729583"));
		chatListsDetails.add(new ChatList("https://cdn1.iconfinder.com/data/icons/metro-ui-dock-icon-set--icons-by-dakirby/512/CD.png", "Arpit", "+919711431657"));
		chatListsDetails.add(new ChatList("https://img3.stockfresh.com/files/r/rastudio/m/46/6638876_stock-vector-bunch-of-grapes-line-icon.jpg", "Rajesh", "9660729583"));
		chatListsDetails.add(new ChatList("http://smalldata.io/img/sdl_logo.png", "Narendra", "9660729583"));
		chatListsDetails.add(new ChatList("https://cdn1.iconfinder.com/data/icons/metro-ui-dock-icon-set--icons-by-dakirby/512/CD.png", "Arpit", "+919711431657"));
		chatListsDetails.add(new ChatList("https://img3.stockfresh.com/files/r/rastudio/m/46/6638876_stock-vector-bunch-of-grapes-line-icon.jpg", "Rajesh", "9660729583"));
		chatListsDetails.add(new ChatList("http://smalldata.io/img/sdl_logo.png", "Narendra", "9660729583"));
		chatListsDetails.add(new ChatList("https://cdn1.iconfinder.com/data/icons/metro-ui-dock-icon-set--icons-by-dakirby/512/CD.png", "Arpit", "+919711431657"));
		chatListsDetails.add(new ChatList("https://img3.stockfresh.com/files/r/rastudio/m/46/6638876_stock-vector-bunch-of-grapes-line-icon.jpg", "Rajesh", "9660729583"));
		chatListsDetails.add(new ChatList("http://smalldata.io/img/sdl_logo.png", "Narendra", "9660729583"));
		chatListsDetails.add(new ChatList("https://cdn1.iconfinder.com/data/icons/metro-ui-dock-icon-set--icons-by-dakirby/512/CD.png", "Arpit", "+919711431657"));
		chatListsDetails.add(new ChatList("https://img3.stockfresh.com/files/r/rastudio/m/46/6638876_stock-vector-bunch-of-grapes-line-icon.jpg", "Rajesh", "9660729583"));
		chatListsDetails.add(new ChatList("http://smalldata.io/img/sdl_logo.png", "Narendra", "9660729583"));
		chatListsDetails.add(new ChatList("https://cdn1.iconfinder.com/data/icons/metro-ui-dock-icon-set--icons-by-dakirby/512/CD.png", "Arpit", "+919711431657"));
		chatListsDetails.add(new ChatList("https://img3.stockfresh.com/files/r/rastudio/m/46/6638876_stock-vector-bunch-of-grapes-line-icon.jpg", "Rajesh", "9660729583"));
		chatListsDetails.add(new ChatList("http://smalldata.io/img/sdl_logo.png", "Narendra", "9660729583"));
		chatListsDetails.add(new ChatList("https://cdn1.iconfinder.com/data/icons/metro-ui-dock-icon-set--icons-by-dakirby/512/CD.png", "Arpit", "+919711431657"));
		chatListsDetails.add(new ChatList("https://img3.stockfresh.com/files/r/rastudio/m/46/6638876_stock-vector-bunch-of-grapes-line-icon.jpg", "Rajesh", "9660729583"));
		chatListsDetails.add(new ChatList("http://smalldata.io/img/sdl_logo.png", "Narendra", "9660729583"));
		chatListsDetails.add(new ChatList("https://cdn1.iconfinder.com/data/icons/metro-ui-dock-icon-set--icons-by-dakirby/512/CD.png", "Arpit", "+919711431657"));
		chatListsDetails.add(new ChatList("https://img3.stockfresh.com/files/r/rastudio/m/46/6638876_stock-vector-bunch-of-grapes-line-icon.jpg", "Rajesh", "9660729583"));
		chatListsDetails.add(new ChatList("http://smalldata.io/img/sdl_logo.png", "Narendra", "9660729583"));
		chatListsDetails.add(new ChatList("https://cdn1.iconfinder.com/data/icons/metro-ui-dock-icon-set--icons-by-dakirby/512/CD.png", "Arpit", "+919711431657"));
		chatListsDetails.add(new ChatList("https://img3.stockfresh.com/files/r/rastudio/m/46/6638876_stock-vector-bunch-of-grapes-line-icon.jpg", "Rajesh", "9660729583"));
		chatListsDetails.add(new ChatList("http://smalldata.io/img/sdl_logo.png", "Narendra", "9660729583"));
		chatListsDetails.add(new ChatList("https://cdn1.iconfinder.com/data/icons/metro-ui-dock-icon-set--icons-by-dakirby/512/CD.png", "Arpit", "+919711431657"));
		chatListsDetails.add(new ChatList("https://img3.stockfresh.com/files/r/rastudio/m/46/6638876_stock-vector-bunch-of-grapes-line-icon.jpg", "Rajesh", "9660729583"));

		chatListAdapter = new ChatListAdapter(chatListsDetails, getContext());

		chatList.setAdapter(chatListAdapter);

		chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ChatList chatList1 = chatListsDetails.get(position);
				Snackbar.make(view, chatList1.receiverName, Snackbar.LENGTH_LONG)
						.setAction("No action", null).show();
			}
		});
	}
}
