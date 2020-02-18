package com.arcsoft.arcfacedemo.common;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcsoft.arcfacedemo.R;


/**
 * 基础视窗类
 */

public class BaseBarActivity extends BaseActivity implements View.OnClickListener {


    protected ImageView img_back;
    private TextView text_bar_title;
    protected TextView bar_right;
    private FrameLayout layout_content;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base_bar);
        initBaseView();

        View contentView = LayoutInflater.from(this).inflate(layoutResID, null);
        layout_content.addView(contentView);
    }


    private void initBaseView() {
        img_back = findViewById(R.id.img_back);
        text_bar_title = findViewById(R.id.text_bar_title);
        bar_right = findViewById(R.id.bar_right);
        layout_content = findViewById(R.id.layout_content);

        img_back.setOnClickListener(this);
        text_bar_title.setOnClickListener(this);
        bar_right.setOnClickListener(this);
    }

    public void setTitleText(String title) {
        text_bar_title.setText(title);
    }

    public void setBarRightText(String right) {
        bar_right.setText(right);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_back) {
            finish();

        }
    }

}
