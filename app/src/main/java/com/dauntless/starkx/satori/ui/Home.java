package com.dauntless.starkx.satori.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dauntless.starkx.satori.R;
import com.dauntless.starkx.satori.etc.Connection;
import com.dauntless.starkx.satori.etc.SocketConnection;

import org.json.JSONObject;

public class Home extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        SocketConnection.getInstance().initialize(getSharedPreferences("Contacts", MODE_PRIVATE).getString("Number", ""));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.Logout){
            SharedPreferences preferences=getApplicationContext().getSharedPreferences("Contacts", MODE_PRIVATE);
            try
            {
                JSONObject object = new JSONObject();
                object.put("Number", preferences.getString("Number", ""));
                Connection.Post(Connection.getUrl() + "/user/logout", object, new Connection.ConnectionResponse(){
                    @Override
                    public void JsonResponse(JSONObject object, boolean Success)
                    {
                        super.JsonResponse(object, Success);
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            preferences.edit().clear().apply();
            startActivity(new Intent(this, Splash.class));

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:return FragmentCamera.newInstance();
                case 1:return FragmentChat.newInstance();
                case 2:return FragmentContacts.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 1) {
                return "Chat";
            }else if(position == 2) {
                return "Contacts";
            }else {
                return "Camera";
            }
        }
    }
}
