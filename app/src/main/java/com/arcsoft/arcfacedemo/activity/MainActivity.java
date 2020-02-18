package com.arcsoft.arcfacedemo.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.common.App;
import com.arcsoft.arcfacedemo.common.BaseBarActivity;
import com.arcsoft.arcfacedemo.common.Constants;
import com.arcsoft.arcfacedemo.faceserver.FaceServer;
import com.arcsoft.arcfacedemo.model.User;
import com.arcsoft.arcfacedemo.util.PreferencesUtils;
/**
 * Created by NYERH on 2019/4/9.
 */
public class MainActivity extends BaseBarActivity implements View.OnClickListener {

    private TextView tv_name;
    private Button tv_clear;
    private Button tv_register_face;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        setTitleText("主页");
        tv_name = (TextView) findViewById(R.id.tv_name);

        user = App.getInstance().getUser();
        if (user != null) {
            tv_name.setText("登录成功：" + user.getName());
        }
        tv_clear = (Button) findViewById(R.id.tv_clear);
        tv_clear.setOnClickListener(this);
        tv_register_face = (Button) findViewById(R.id.tv_register_face);
        tv_register_face.setOnClickListener(this);
        Boolean result = PreferencesUtils.getBoolean(this, Constants.REGISTER_TYPE_FACE, false);
        if (result) {
            tv_register_face.setVisibility(View.GONE);
        }else{
            tv_register_face.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_clear:
                clearFaces();
                break;
            case R.id.tv_register_face:
                Bundle bundle = new Bundle();
                bundle.putCharSequence(Constants.INTENT_KEY_NAME, user.getName());
                startActivity(RegisterAndRecognizeActivity.class, bundle);
                break;
        }
    }

    public void clearFaces() {
        int faceNum = FaceServer.getInstance().getFaceNumber(this);
        if (faceNum == 0) {
            Toast.makeText(this, R.string.no_face_need_to_delete, Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.notification)
                    .setMessage(getString(R.string.confirm_delete, faceNum))
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int deleteCount = FaceServer.getInstance().clearAllFaces(MainActivity.this);
                            PreferencesUtils.putBoolean(MainActivity.this, Constants.REGISTER_TYPE_FACE, false);
                            Toast.makeText(MainActivity.this, deleteCount + " faces cleared!", Toast.LENGTH_SHORT).show();
                            initView();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .create();
            dialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }
}
