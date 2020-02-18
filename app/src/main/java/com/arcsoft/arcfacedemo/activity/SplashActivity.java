package com.arcsoft.arcfacedemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.common.BaseActivity;
import com.arcsoft.arcfacedemo.common.Constants;
import com.arcsoft.arcfacedemo.util.PreferencesUtils;
/**
 * Created by NYERH on 2019/4/9.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        Boolean result = PreferencesUtils.getBoolean(this, Constants.REGISTER_TYPE_FACE, false);

        if (result) {
            startActivity(LoginByFaceActivity.class);
        } else {
            startActivity(LoginActivity.class);
        }

        finish();
    }
}
