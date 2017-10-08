package com.dauntless.starkx.satori.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dauntless.starkx.satori.Adapter.ImagePickerAdapter;
import com.dauntless.starkx.satori.R;
import com.dauntless.starkx.satori.lib.MessagePasser;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FragmentFilterPicker extends Fragment {
	private WeakReference<MessagePasser> messagePasserReference;
	private ArrayList<String> mFilters = new ArrayList<>();
	private Integer RESPONSE_CODE;
	private GridView gridView;

	public static FragmentFilterPicker newInstance(ArrayList<String> mFilters, Integer responseCode, MessagePasser messagePasser) {
		FragmentFilterPicker fragment = new FragmentFilterPicker();
		fragment.mFilters = mFilters;
		fragment.messagePasserReference = new WeakReference<>(messagePasser);
		fragment.RESPONSE_CODE = responseCode;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_filter_picker, container, false);
		gridView = (GridView) rootView.findViewById(R.id.grid_view);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		gridView.setAdapter(new ImagePickerAdapter(getActivity(), mFilters, RESPONSE_CODE, messagePasserReference.get()));
	}
}
