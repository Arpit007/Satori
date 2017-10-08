package com.dauntless.starkx.satori.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.dauntless.starkx.satori.R;
import com.dauntless.starkx.satori.etc.Connection;
import com.dauntless.starkx.satori.lib.MessagePasser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import static com.dauntless.starkx.satori.ui.FragmentCamera.RESULT_CODE_EYE;
import static com.dauntless.starkx.satori.ui.FragmentCamera.RESULT_CODE_HEAD;
import static com.dauntless.starkx.satori.ui.FragmentCamera.RESULT_CODE_MUSTACHE;
import static com.dauntless.starkx.satori.ui.FragmentCamera.RESULT_CODE_NOSE;
import static com.dauntless.starkx.satori.ui.FragmentCamera.RESULT_CODE_NOSE_FACE;

public class FilterPicker extends AppCompatActivity implements MessagePasser {
	private SectionsPagerAdapter mSectionsPagerAdapter;

	private ViewPager mViewPager;
	private TabLayout tabLayout;
	private ArrayList<String> noseUrls;
	private ArrayList<String> eyeUrls;
	private ArrayList<String> mustacheUrls;
	private ArrayList<String> headUrls;
	private ArrayList<String> faceUrls;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_picker);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		noseUrls = new ArrayList<>();
		eyeUrls = new ArrayList<>();
		mustacheUrls = new ArrayList<>();
        faceUrls = new ArrayList<>();
		headUrls = new ArrayList<>();

		getAllImages();

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.container);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		tabLayout = (TabLayout) findViewById(R.id.tabs_filter);
		tabLayout.setupWithViewPager(mViewPager);
	}

	public void getAllImages() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					SharedPreferences preferences = getSharedPreferences("Resources", MODE_PRIVATE);
					JSONObject object = new JSONObject(preferences.getString("Data", "{head:{code:400, message:\"Failed\"}}"));
					ParseMessage(object);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void PassMessage(Parcelable Message, int Code) {
		Bundle bundle = new Bundle();
		bundle.putParcelable("bmp_img", Message);
		bundle.putInt("code", Code);
		Intent result = new Intent();
		result.putExtras(bundle);
		setResult(RESULT_OK, result);
		finish();
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

	private void ParseMessage(JSONObject object) throws Exception {
		if (object.getJSONObject("head").getInt("code") != 200) {
			hideDialog("Restart App with Active Network Connection");
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
			mustacheUrls.add(Connection.getUrl() + mustaches.getString(x));
		}
        JSONArray faces = object.getJSONObject("body").getJSONObject("res").getJSONArray("face");
        for (int x = 0; x < faces.length(); x++) {
            faceUrls.add(Connection.getUrl() + faces.getString(x));
        }
		FilterPicker.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mSectionsPagerAdapter.notifyDataSetChanged();
			}
		});
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0:
					return FragmentFilterPicker.newInstance(eyeUrls, RESULT_CODE_EYE, FilterPicker.this);
				case 1:
					return FragmentFilterPicker.newInstance(noseUrls, RESULT_CODE_NOSE, FilterPicker.this);
				case 2:
					return FragmentFilterPicker.newInstance(headUrls, RESULT_CODE_HEAD, FilterPicker.this);
				case 3:
					return FragmentFilterPicker.newInstance(faceUrls, RESULT_CODE_NOSE_FACE, FilterPicker.this);
				case 4:
					return FragmentFilterPicker.newInstance(mustacheUrls, RESULT_CODE_MUSTACHE, FilterPicker.this);
			}
			return FragmentFilterPicker.newInstance(eyeUrls, RESULT_CODE_EYE, FilterPicker.this);
		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {

			switch (position) {
				case 0:
					return "EYE";
				case 1:
					return "NOSE";
				case 2:
					return "HEAD";
				case 3:
					return "FACE";
				case 4:
					return "MUSTACHE";
			}
			return null;
		}
	}
}
