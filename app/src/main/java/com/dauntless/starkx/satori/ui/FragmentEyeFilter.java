package com.dauntless.starkx.satori.ui;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.dauntless.starkx.satori.Adapter.ContactsAdapter;
import com.dauntless.starkx.satori.Adapter.ImagePickerAdapter;
import com.dauntless.starkx.satori.Model.Contacts;
import com.dauntless.starkx.satori.R;

import java.util.ArrayList;

public class FragmentEyeFilter extends Fragment {

    private ArrayList<String> mFilters  = new ArrayList<>();
    private Integer RESPONSE_CODE_FILETR = 10;
    private GridView gridView;

    public static FragmentEyeFilter newInstance() {
        FragmentEyeFilter fragment = new FragmentEyeFilter();
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

        mFilters.add("http://smalldata.io/img/sdl_logo.png");
        mFilters.add("http://woollypedlar.co.uk/wp-content/uploads/2016/02/twitter-logo.jpg");
        mFilters.add("https://img3.stockfresh.com/files/r/rastudio/m/46/6638876_stock-vector-bunch-of-grapes-line-icon.jpg");
        mFilters.add("https://cdn1.iconfinder.com/data/icons/metro-ui-dock-icon-set--icons-by-dakirby/512/CD.png");
        mFilters.add("http://smalldata.io/img/sdl_logo.png");
        mFilters.add("http://woollypedlar.co.uk/wp-content/uploads/2016/02/twitter-logo.jpg");
        mFilters.add("http://woollypedlar.co.uk/wp-content/uploads/2016/02/twitter-logo.jpg");
        mFilters.add("https://img3.stockfresh.com/files/r/rastudio/m/46/6638876_stock-vector-bunch-of-grapes-line-icon.jpg");
        mFilters.add("https://cdn1.iconfinder.com/data/icons/metro-ui-dock-icon-set--icons-by-dakirby/512/CD.png");
        mFilters.add("http://smalldata.io/img/sdl_logo.png");
        mFilters.add("http://woollypedlar.co.uk/wp-content/uploads/2016/02/twitter-logo.jpg");
        mFilters.add("https://img3.stockfresh.com/files/r/rastudio/m/46/6638876_stock-vector-bunch-of-grapes-line-icon.jpg");
        mFilters.add("https://cdn1.iconfinder.com/data/icons/metro-ui-dock-icon-set--icons-by-dakirby/512/CD.png");
        mFilters.add("http://smalldata.io/img/sdl_logo.png");
        mFilters.add("http://woollypedlar.co.uk/wp-content/uploads/2016/02/twitter-logo.jpg");
        mFilters.add("https://img3.stockfresh.com/files/r/rastudio/m/46/6638876_stock-vector-bunch-of-grapes-line-icon.jpg");
        mFilters.add("https://cdn1.iconfinder.com/data/icons/metro-ui-dock-icon-set--icons-by-dakirby/512/CD.png");
        mFilters.add("http://smalldata.io/img/sdl_logo.png");
        mFilters.add("http://woollypedlar.co.uk/wp-content/uploads/2016/02/twitter-logo.jpg");
        mFilters.add("https://img3.stockfresh.com/files/r/rastudio/m/46/6638876_stock-vector-bunch-of-grapes-line-icon.jpg");
        mFilters.add("https://cdn1.iconfinder.com/data/icons/metro-ui-dock-icon-set--icons-by-dakirby/512/CD.png");


        gridView.setAdapter(new ImagePickerAdapter(getActivity() , mFilters));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currenFilter = mFilters.get(position);
                Intent intent = new Intent(getContext() , Home.class);
                intent.putExtra("mFilter", currenFilter);
                getActivity().setResult(RESPONSE_CODE_FILETR, intent);
                getActivity().finish();
            }
        });
    }

}
