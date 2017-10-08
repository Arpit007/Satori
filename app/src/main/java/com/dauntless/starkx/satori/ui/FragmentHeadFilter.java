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

public class FragmentHeadFilter extends Fragment {

    private ArrayList<String> mFilters  = new ArrayList<>();
    private Integer RESPONSE_CODE_HEAD_FILETR = 13;
    private GridView gridView;

    public static FragmentHeadFilter newInstance() {
        FragmentHeadFilter fragment = new FragmentHeadFilter();
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

        mFilters.add("http://www.clker.com/cliparts/7/2/9/3/1394792024464130176Top_Hat_1.svg.hi.png");
        mFilters.add("http://www.clker.com/cliparts/G/3/n/z/w/D/santa-hat-small-hi.png");
        mFilters.add("http://www.clker.com/cliparts/a/D/I/J/x/1/green-hat-hi.png");
        mFilters.add("http://www.pngall.com/wp-content/uploads/2016/07/Birthday-Hat-PNG-HD.png");
        mFilters.add("http://www.clker.com/cliparts/p/s/f/8/4/c/uncle-sam-american-hat-hi.png");
        mFilters.add("http://www.clker.com/cliparts/7/2/9/3/1394792024464130176Top_Hat_1.svg.hi.png");
        mFilters.add("http://www.clker.com/cliparts/G/3/n/z/w/D/santa-hat-small-hi.png");
        mFilters.add("http://www.clker.com/cliparts/a/D/I/J/x/1/green-hat-hi.png");
        mFilters.add("http://www.pngall.com/wp-content/uploads/2016/07/Birthday-Hat-PNG-HD.png");
        mFilters.add("http://www.clker.com/cliparts/p/s/f/8/4/c/uncle-sam-american-hat-hi.png");
        mFilters.add("http://www.clker.com/cliparts/7/2/9/3/1394792024464130176Top_Hat_1.svg.hi.png");
        mFilters.add("http://www.clker.com/cliparts/G/3/n/z/w/D/santa-hat-small-hi.png");
        mFilters.add("http://www.clker.com/cliparts/a/D/I/J/x/1/green-hat-hi.png");
        mFilters.add("http://www.pngall.com/wp-content/uploads/2016/07/Birthday-Hat-PNG-HD.png");
        mFilters.add("http://www.clker.com/cliparts/p/s/f/8/4/c/uncle-sam-american-hat-hi.png");


        gridView.setAdapter(new ImagePickerAdapter(getActivity() , mFilters));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currenFilter = mFilters.get(position);
                Intent intent = new Intent(getContext() , Home.class);
                intent.putExtra("mFilter", currenFilter);
                getActivity().setResult(RESPONSE_CODE_HEAD_FILETR, intent);
                getActivity().finish();
            }
        });
    }
}
