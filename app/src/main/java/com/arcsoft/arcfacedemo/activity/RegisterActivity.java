package com.arcsoft.arcfacedemo.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.common.BaseActivity;
import com.arcsoft.arcfacedemo.common.Constants;
import com.arcsoft.arcfacedemo.greendao.DaoManager;
import com.arcsoft.arcfacedemo.greendao.UserDao;
import com.arcsoft.arcfacedemo.model.User;
import com.arcsoft.arcfacedemo.util.PreferencesUtils;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
/**
 * Created by NYERH on 2019/4/9.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    private EditText text_name;
    private EditText text_pwd1;
    private EditText text_pwd2;
    private LinearLayout content;
    private TextView text_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        }
        initView();
    }

    private void initView() {
        text_name = (EditText) findViewById(R.id.text_name);
        text_pwd1 = (EditText) findViewById(R.id.text_pwd1);
        text_pwd2 = (EditText) findViewById(R.id.text_pwd2);
        content = (LinearLayout) findViewById(R.id.content);
        text_submit = (TextView) findViewById(R.id.text_submit);

        text_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_submit:
                submit();
                break;
        }
    }

    private void submit() {
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
            return;
        }

        // validate
        String name = text_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }

        String pwd1 = text_pwd1.getText().toString().trim();
        if (TextUtils.isEmpty(pwd1)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String pwd2 = text_pwd2.getText().toString().trim();
        if (TextUtils.isEmpty(pwd2)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.equals(pwd1, pwd2)) {
            Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.setName(name);
        user.setPwd(pwd1);

        List<User> users = DaoManager.getInstance().getDaoSession().getUserDao().queryBuilder().where(UserDao.Properties.Name.eq(name)).list();
        if (users.size() > 0) {
            showToast("该用户已注册");
            return;
        }

        long result = DaoManager.getInstance().getDaoSession().getUserDao().insert(user);
        if (result > 0) {
            showToast("注册成功");
            Bundle bundle = new Bundle();
            bundle.putCharSequence(Constants.INTENT_KEY_NAME, name);
            startActivity(RegisterAndRecognizeActivity.class, bundle);
            finish();
        }
    }

    public void activeEngine() {
        boolean active = PreferencesUtils.getBoolean(this, Constants.ACTIVE, false);
        if (!active) {
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                    FaceEngine faceEngine = new FaceEngine();
                    int activeCode = faceEngine.active(RegisterActivity.this, Constants.APP_ID, Constants.SDK_KEY);
                    emitter.onNext(activeCode);
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Integer activeCode) {
                            dismissWaitingDialog();
                            if (activeCode == ErrorInfo.MOK) {
                                PreferencesUtils.putBoolean(RegisterActivity.this, Constants.ACTIVE, true);
                            } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
//                            showToast(getString(R.string.already_activated));
                            } else {
//                            showToast(getString(R.string.active_failed, activeCode));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        }
    }

    private boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            boolean isAllGranted = true;
            for (int grantResult : grantResults) {
                isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
            }
            if (isAllGranted) {
                activeEngine();
            } else {
                showToast(getString(R.string.permission_denied));
            }
        }
    }
}
