package com.arcsoft.arcfacedemo.common;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import java.util.Calendar;


public class BaseActivity extends AppCompatActivity {
    private ProcessDialog mWaitDlg;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public void startWaitingDialog(String content) {
        if (mWaitDlg == null) {
            mWaitDlg = new ProcessDialog(this, content, true);
        } else {
            mWaitDlg.setContent(content);
        }
        if (mWaitDlg != null && mWaitDlg.isShowing() == false) {
            mWaitDlg.show();
        }
    }

    public void startWaitingDialog() {
        startWaitingDialog("");
    }

    public void dismissWaitingDialog() {
        if (mWaitDlg != null) {
            mWaitDlg.dismiss();
            mWaitDlg = null;
        }
    }

    public void showConfirmDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setMessage(message).setCancelable(true).show();
    }

    /**
     * 隐藏软键盘 false:隐藏,true:显示
     */
    public void showSoftInput(boolean show, View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (show) {
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        } else if (imm.isActive() == true) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showToast(String message) {
        if (TextUtils.isEmpty(message) == false) {
            if (toast == null) {
                toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            } else {
                toast.setText(message);
            }
            toast.show();
        }
    }

    public void showDateDialog(DatePickerDialog.OnDateSetListener listener, String date) {
        int year = 0;
        int month = 0;
        int day = 0;
        if (TextUtils.isEmpty(date) == false && date.length() > 7) {
            String[] time = date.split("-");
            year = Integer.parseInt(time[0]);
            month = Integer.parseInt(time[1]) - 1;
            day = Integer.parseInt(time[2]);
        } else {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener, year, month, day);
        datePickerDialog.setCanceledOnTouchOutside(true);
        datePickerDialog.setCancelable(true);
        datePickerDialog.show();
    }

    protected void showTimePickerDialog(TimePickerDialog.OnTimeSetListener onTimeSetListener, String time) {
        int hour = 0;
        int minute = 0;
        if (TextUtils.isEmpty(time) == false) {
            String[] times = time.split(":");
            hour = Integer.parseInt(times[0]);
            minute = Integer.parseInt(times[1]);
        } else {
            Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setCanceledOnTouchOutside(true);
        timePickerDialog.setCancelable(true);
        timePickerDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissWaitingDialog();
    }

    public void startActivity( Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void startActivity( Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
