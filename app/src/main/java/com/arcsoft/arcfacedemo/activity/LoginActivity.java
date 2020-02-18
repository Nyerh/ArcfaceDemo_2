package com.arcsoft.arcfacedemo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.common.App;
import com.arcsoft.arcfacedemo.common.BaseActivity;
import com.arcsoft.arcfacedemo.common.Constants;
import com.arcsoft.arcfacedemo.greendao.DaoManager;
import com.arcsoft.arcfacedemo.greendao.UserDao;
import com.arcsoft.arcfacedemo.model.User;
import com.arcsoft.arcfacedemo.util.PreferencesUtils;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;

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

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private EditText text_name;
    private EditText text_pwd;
    private LinearLayout content;
    private TextView text_register;
    private TextView text_submit;
    private TextView text_find_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        text_name = (EditText) findViewById(R.id.text_name);
        text_pwd = (EditText) findViewById(R.id.text_pwd);
        content = (LinearLayout) findViewById(R.id.content);
        text_register = (TextView) findViewById(R.id.text_register);
        text_submit = (TextView) findViewById(R.id.text_submit);
        text_find_pwd = (TextView) findViewById(R.id.text_find_pwd);

        text_register.setOnClickListener(this);
        text_submit.setOnClickListener(this);
        text_find_pwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.text_submit:
                submit();
                break;
            case R.id.text_find_pwd:
                startActivity(ModifyPwdActivity.class);

                break;
        }
    }

    private void submit() {
        // validate
        String name = text_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }

        String pwd = text_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = DaoManager.getInstance().getDaoSession().getUserDao().queryBuilder().where(UserDao.Properties.Name.eq(name),
                UserDao.Properties.Pwd.eq(pwd)).unique();
        if (user != null) {
            App.getInstance().setUser(user);
            startActivity(MainActivity.class);
            finish();
        } else {
            showToast("用户名或密码不正确");
        }
    }

    public void activeEngine() {
        boolean active = PreferencesUtils.getBoolean(this, Constants.ACTIVE, false);
        if (!active) {
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                    FaceEngine faceEngine = new FaceEngine();
                    int activeCode = faceEngine.active(LoginActivity.this, Constants.APP_ID, Constants.SDK_KEY);
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
                            if (activeCode == ErrorInfo.MOK) {
                                PreferencesUtils.putBoolean(LoginActivity.this, Constants.ACTIVE, true);
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

}
