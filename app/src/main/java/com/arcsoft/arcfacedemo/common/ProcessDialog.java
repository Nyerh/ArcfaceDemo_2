package com.arcsoft.arcfacedemo.common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.arcsoft.arcfacedemo.R;
/**
 * 基础视窗绘制.
 */
public class ProcessDialog extends Dialog implements DialogInterface.OnKeyListener {

    private TextView text_content;

    public ProcessDialog(Context context, String content) {
        this(context, content, false);
    }

    public ProcessDialog(Context context, boolean cancelable) {
        this(context, "", cancelable);
    }

    public ProcessDialog(Context context) {
        this(context, "", false);
    }

    public ProcessDialog(Context context, String content, boolean cancelable) {
        super(context);
        this.setContentView(R.layout.dialog_processdialog);
        initDialog(content, cancelable);
    }

    private void initDialog(String content, boolean cancelable) {
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        text_content = (TextView) this.findViewById(R.id.text_content);
        if (TextUtils.isEmpty(content) == false) {
            text_content.setVisibility(View.VISIBLE);
            text_content.setText(content);
        }
        setOnKeyListener(this);
        setCancelable(cancelable);
    }

    public void setContent(String content){
        if (TextUtils.isEmpty(content) == false) {
            text_content.setVisibility(View.VISIBLE);
            text_content.setText(content);
        }
    }

    @Override
    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (KeyEvent.KEYCODE_BACK == i) {
            dismiss();
        }
        return false;
    }

}