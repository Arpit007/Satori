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

public class FragmentMustacheFilter extends Fragment {
    private ArrayList<String> mFilters  = new ArrayList<>();
    private Integer RESPONSE_CODE_MUSTACHE_FILETR = 12;
    private GridView gridView;

    public static FragmentMustacheFilter newInstance() {
        FragmentMustacheFilter fragment = new FragmentMustacheFilter();
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

        mFilters.add("https://d30y9cdsu7xlg0.cloudfront.net/png/19577-200.png");
        mFilters.add("http://pngimg.com/uploads/beard/beard_PNG6264.png");
        mFilters.add("http://www.freepngimg.com/thumb/mustache/5-2-no-shave-movember-day-mustache-png-image-thumb.png");
        mFilters.add("http://www.freepngimg.com/thumb/mustache/3-2-no-shave-movember-day-mustache-png-hd-thumb.png");
        mFilters.add("https://d30y9cdsu7xlg0.cloudfront.net/png/19577-200.png");
        mFilters.add("http://pngimg.com/uploads/beard/beard_PNG6264.png");
        mFilters.add("http://www.freepngimg.com/thumb/mustache/5-2-no-shave-movember-day-mustache-png-image-thumb.png");
        mFilters.add("http://www.freepngimg.com/thumb/mustache/3-2-no-shave-movember-day-mustache-png-hd-thumb.png");
        mFilters.add("https://d30y9cdsu7xlg0.cloudfront.net/png/19577-200.png");
        mFilters.add("http://pngimg.com/uploads/beard/beard_PNG6264.png");
        mFilters.add("http://www.freepngimg.com/thumb/mustache/5-2-no-shave-movember-day-mustache-png-image-thumb.png");
        mFilters.add("http://www.freepngimg.com/thumb/mustache/3-2-no-shave-movember-day-mustache-png-hd-thumb.png");
        mFilters.add("https://d30y9cdsu7xlg0.cloudfront.net/png/19577-200.png");
        mFilters.add("http://pngimg.com/uploads/beard/beard_PNG6264.png");
        mFilters.add("http://www.freepngimg.com/thumb/mustache/5-2-no-shave-movember-day-mustache-png-image-thumb.png");
        mFilters.add("http://www.freepngimg.com/thumb/mustache/3-2-no-shave-movember-day-mustache-png-hd-thumb.png");
        mFilters.add("https://d30y9cdsu7xlg0.cloudfront.net/png/19577-200.png");
        mFilters.add("http://pngimg.com/uploads/beard/beard_PNG6264.png");
        mFilters.add("http://www.freepngimg.com/thumb/mustache/5-2-no-shave-movember-day-mustache-png-image-thumb.png");
        mFilters.add("http://www.freepngimg.com/thumb/mustache/3-2-no-shave-movember-day-mustache-png-hd-thumb.png");
        mFilters.add("https://d30y9cdsu7xlg0.cloudfront.net/png/19577-200.png");
        mFilters.add("http://pngimg.com/uploads/beard/beard_PNG6264.png");
        mFilters.add("http://www.freepngimg.com/thumb/mustache/5-2-no-shave-movember-day-mustache-png-image-thumb.png");
        mFilters.add("http://www.freepngimg.com/thumb/mustache/3-2-no-shave-movember-day-mustache-png-hd-thumb.png");


        gridView.setAdapter(new ImagePickerAdapter(getActivity() , mFilters));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currenFilter = mFilters.get(position);
                Intent intent = new Intent(getContext() , Home.class);
                intent.putExtra("mFilter", currenFilter);
                getActivity().setResult(RESPONSE_CODE_MUSTACHE_FILETR, intent);
                getActivity().finish();
            }
        });
    }
}
