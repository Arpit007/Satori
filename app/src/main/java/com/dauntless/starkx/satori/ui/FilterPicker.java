package com.dauntless.starkx.satori.ui;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.dauntless.starkx.satori.Adapter.ImagePickerAdapter;
import com.dauntless.starkx.satori.Model.Contacts;
import com.dauntless.starkx.satori.R;

import java.util.ArrayList;

public class FilterPicker extends AppCompatActivity {

    private ArrayList<String> mFilters  = new ArrayList<>();
    private Integer RESPONSE_CODE_FILETR = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_picker2);

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


        GridView gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(new ImagePickerAdapter(this , mFilters));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currenFilter = mFilters.get(position);
                Intent intent = new Intent(FilterPicker.this , Home.class);
                intent.putExtra("mFilter", currenFilter);
                setResult(RESPONSE_CODE_FILETR, intent);
                finish();
            }
        });
    }
}
