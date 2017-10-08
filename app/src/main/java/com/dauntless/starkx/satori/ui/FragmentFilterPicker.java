package com.dauntless.starkx.satori.ui;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dauntless.starkx.satori.Adapter.ImagePickerAdapter;
import com.dauntless.starkx.satori.R;

import java.util.ArrayList;

public class FragmentFilterPicker extends Fragment {

    private ArrayList<String> mFilters  = new ArrayList<>();
    private Integer RESPONSE_CODE_FILETR ;
    private GridView gridView;

    public static FragmentFilterPicker newInstance(ArrayList<String> mFilters , Integer responseCode) {
        FragmentFilterPicker fragment = new FragmentFilterPicker();
        fragment.mFilters = mFilters;
        fragment.RESPONSE_CODE_FILETR = responseCode;
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter_picker, container, false);
        gridView = (GridView) rootView.findViewById(R.id.grid_view);
        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView.setAdapter(new ImagePickerAdapter(getActivity(), getActivity() , mFilters , RESPONSE_CODE_FILETR));
    }

}
