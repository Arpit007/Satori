package com.dauntless.starkx.satori.ui;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import android.widget.Toast;

import com.dauntless.starkx.satori.R;
import com.dauntless.starkx.satori.etc.Connection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FilterPicker extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private ArrayList<String> noseUrls;
    private ArrayList<String> eyeUrls;
    private ArrayList<String> mustacheUrls;
    private ArrayList<String> headUrls;
    private ArrayList<String> mouthUrls;
    private  ProgressDialog progressDialog;
    private Integer RESULT_CODE_EYE = 10;
    private Integer RESULT_CODE_NOSE = 11;
    private Integer RESULT_CODE_MUSTACHE = 12;
    private Integer RESULT_CODE_HEAD = 13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_picker);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");

        noseUrls = new ArrayList<>();
        eyeUrls = new ArrayList<>();
        mustacheUrls = new ArrayList<>();
        mouthUrls = new ArrayList<>();
        headUrls = new ArrayList<>();
        progressDialog.show();
        getAllImages();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs_filter);
        tabLayout.setupWithViewPager(mViewPager);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


        // Set up the ViewPager with the sections adapter.

    }
    public void getAllImages() {
        try {
            Connection.Get(Connection.getUrl() + "/res/all", new JSONObject(), true ,new Connection.ConnectionResponse() {
                @Override
                public void JsonResponse(JSONObject object, boolean Success) {
                    if (!Success) {
                        hideDialog("Connection Failed");
                        return;
                    }
                    try {
                        if (object.getJSONObject("head").getInt("code") != 200) {
                            hideDialog(object.getJSONObject("head").getString("message"));
                            return;
                        }
                        JSONArray eyes = object.getJSONObject("body").getJSONObject("res").getJSONArray("eyes");
                        for (int x = 0; x < eyes.length(); x++) {
                            eyeUrls.add(Connection.getUrl() + eyes.getString(x));
                        }
                        JSONArray hats = object.getJSONObject("body").getJSONObject("res").getJSONArray("head");
                        for (int x = 0; x < hats.length(); x++) {
                            headUrls.add(Connection.getUrl() + hats.getString(x));
                        }
                        JSONArray noses = object.getJSONObject("body").getJSONObject("res").getJSONArray("nose");
                        for (int x = 0; x < noses.length(); x++) {
                            noseUrls.add(Connection.getUrl() + noses.getString(x));
                        }
                        JSONArray mustaches = object.getJSONObject("body").getJSONObject("res").getJSONArray("moustache");
                        for (int x = 0; x < mustaches.length(); x++) {
                            mustacheUrls.add(Connection.getUrl() + hats.getString(x));
                        }
                        FilterPicker.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSectionsPagerAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                        });
                        Log.d("Filter Picker" , "done ========================================");

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        hideDialog("Json Failed");
                    }
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
            hideDialog("Connection Check");
        }
    }
    void hideDialog(final String message) {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (!message.isEmpty()) {
                    Toast.makeText(FilterPicker.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    public void setAll() {
//        Handler mainHandler = new Handler(getApplicationContext().getMainLooper());
//        Runnable myRunnable = new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        };
//        mainHandler.post(myRunnable);
//    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0 : return FragmentFilterPicker.newInstance(eyeUrls , 10);
                case 1 : return FragmentFilterPicker.newInstance(noseUrls , 11);
                case 2 : return FragmentFilterPicker.newInstance(headUrls , 13);
                case 4 : return FragmentFilterPicker.newInstance(mustacheUrls , 12);
            }
            return FragmentFilterPicker.newInstance(eyeUrls , 10);

        }
        @Override
        public int getCount() {
            return 5;
        }
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0 : return "EYE";
                case 1 : return "NOSE";
                case 2 : return "HEAD";
                case 3 : return "MOUTH";
                case 4 : return "MUSTACHE";
            }
            return null;
        }
    }
}
