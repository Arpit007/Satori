package com.dauntless.starkx.satori.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dauntless.starkx.satori.Adapter.ImagePickerAdapter;
import com.dauntless.starkx.satori.R;

import java.util.ArrayList;

/**
 * Created by sonu on 8/10/17.
 */

public class FragmentNoseFilter extends Fragment {

    private ArrayList<String> mFilters  = new ArrayList<>();
    private Integer RESPONSE_CODE_NOSE_FILETR = 11;
    private GridView gridView;

    public static FragmentNoseFilter newInstance() {
        FragmentNoseFilter fragment = new FragmentNoseFilter();
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

        mFilters.add("http://clipartfans.com/resource/nose-clip-art/nose-clip-art-12.png");
        mFilters.add("http://www.okclipart.com/img103/hubqxrsfoietltefugmu.png");
        mFilters.add("http://goinkscape.com/wp-content/uploads/2014/07/nemo-29.png");
        mFilters.add("http://clipartfans.com/resource/nose-clip-art/nose-clip-art-12.png");
        mFilters.add("http://www.okclipart.com/img103/hubqxrsfoietltefugmu.png");
        mFilters.add("http://goinkscape.com/wp-content/uploads/2014/07/nemo-29.png");
        mFilters.add("http://clipartfans.com/resource/nose-clip-art/nose-clip-art-12.png");
        mFilters.add("http://www.okclipart.com/img103/hubqxrsfoietltefugmu.png");
        mFilters.add("http://goinkscape.com/wp-content/uploads/2014/07/nemo-29.png");
        mFilters.add("http://clipartfans.com/resource/nose-clip-art/nose-clip-art-12.png");
        mFilters.add("http://www.okclipart.com/img103/hubqxrsfoietltefugmu.png");
        mFilters.add("http://goinkscape.com/wp-content/uploads/2014/07/nemo-29.png");
        mFilters.add("http://clipartfans.com/resource/nose-clip-art/nose-clip-art-12.png");
        mFilters.add("http://www.okclipart.com/img103/hubqxrsfoietltefugmu.png");
        mFilters.add("http://goinkscape.com/wp-content/uploads/2014/07/nemo-29.png");
        mFilters.add("http://clipartfans.com/resource/nose-clip-art/nose-clip-art-12.png");
        mFilters.add("http://www.okclipart.com/img103/hubqxrsfoietltefugmu.png");
        mFilters.add("http://goinkscape.com/wp-content/uploads/2014/07/nemo-29.png");


        gridView.setAdapter(new ImagePickerAdapter(getActivity() , mFilters));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currenFilter = mFilters.get(position);
                Intent intent = new Intent(getContext() , Home.class);
                intent.putExtra("mFilter", currenFilter);
                getActivity().setResult(RESPONSE_CODE_NOSE_FILETR, intent);
                getActivity().finish();
            }
        });
    }
}
