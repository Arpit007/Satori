package com.dauntless.starkx.satori.ui;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dauntless.starkx.satori.R;
import com.dauntless.starkx.satori.etc.Connection;

import org.json.JSONException;
import org.json.JSONObject;

public class SingleChatWindow extends AppCompatActivity {

    String name = "null";
    String mobile = "null";
    Toolbar myToolbar;
    String status = "Offline";
    ImageView audiocall , videocall;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat_window);
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        mobile = bundle.getString("mobile");
        audiocall = (ImageView) findViewById(R.id.audiocall);
        videocall = (ImageView) findViewById(R.id.videocall);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(name + " ( Checking... )");
        myToolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(myToolbar);
        checkStatus();

        audiocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Audio Call ...", Snackbar.LENGTH_LONG)
						.setAction("No action", null).show();
            }
        });
        videocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Video Call ...", Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });

    }
    public void checkStatus() {
        try {
            JSONObject object = new JSONObject();
            object.put("Number", mobile);
            Connection.Post(Connection.getUrl() + "/user/isOnline", object, new Connection.ConnectionResponse() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void JsonResponse(final JSONObject object, boolean Success) {
                try {
                    if (!Success) {
                        Log.d("SingleChatWindow" , "No Success");
                        return;
                    }
                    if(object.getJSONObject("head").getInt("code") != 400) {
                        Log.d("SingleChatWindow", object.getJSONObject("body").getString("status"));
                        status = " Online ";
                    }else {
                        Log.d("no" , "user");
                        status = " Offline ";
                    }
                    final String finalStatus = status;
                    SingleChatWindow.this.runOnUiThread(new Runnable() {
                        public void run() {
                            myToolbar.setTitle(name + " (" + finalStatus + ") ");
                        }
                    });
                } catch (Exception e) {
                    Log.d("SingleChatWindow" , e + "nkkjkfj");
                    e.printStackTrace();
                }
                }
            });
        } catch (Exception e) {
            Log.d("SingleChatWindow" , "No Success" + e);
        }
    }


}
