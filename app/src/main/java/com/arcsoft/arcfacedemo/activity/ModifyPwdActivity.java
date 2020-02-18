package com.arcsoft.arcfacedemo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.common.BaseActivity;
import com.arcsoft.arcfacedemo.greendao.DaoManager;
import com.arcsoft.arcfacedemo.greendao.UserDao;
import com.arcsoft.arcfacedemo.model.User;
/**
 * Created by NYERH on 2019/4/9.
 */

public class ModifyPwdActivity extends BaseActivity implements View.OnClickListener{

    private EditText text_name;
    private EditText text_pwd_old;
    private EditText text_pwd1;
    private EditText text_pwd2;
    private TextView text_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        initView();
    }

    private void initView() {
        text_name = (EditText) findViewById(R.id.text_name);
        text_pwd_old = (EditText) findViewById(R.id.text_pwd_old);
        text_pwd1 = (EditText) findViewById(R.id.text_pwd1);
        text_pwd2 = (EditText) findViewById(R.id.text_pwd2);
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
        // validate
        String name = text_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }

        String old = text_pwd_old.getText().toString().trim();
        if (TextUtils.isEmpty(old)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
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

        User user = DaoManager.getInstance().getDaoSession().getUserDao().queryBuilder().where(UserDao.Properties.Name.eq(name)).unique();
        if (user != null) {
            if (TextUtils.equals(user.getPwd(), old)) {
                user.setPwd(pwd1);
                DaoManager.getInstance().getDaoSession().getUserDao().update(user);
                showToast("密码修改成功");
                finish();
            } else {
                showToast("密码修改失败");
                return;
            }
        } else {
            showToast("用户名不存在");
            return;
        }

    }
}
