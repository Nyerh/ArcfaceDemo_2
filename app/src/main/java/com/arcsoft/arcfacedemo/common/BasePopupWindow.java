package com.arcsoft.arcfacedemo.common;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.arcsoft.arcfacedemo.R;

/**
 * 基础视窗类
 */

public class BasePopupWindow extends PopupWindow implements View.OnClickListener, PopupWindow.OnDismissListener {
    protected View view;
    protected Context mContext;
    protected onPopResultOKListener onPopResultOKListener;

    public BasePopupWindow(Context context, int layoutId, int gravity) {
        mContext = context;
        if (Gravity.TOP == gravity) {
            view = LayoutInflater.from(mContext).inflate(R.layout.pop_base_top, null);
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        } else if (Gravity.CENTER == gravity) {
            view = LayoutInflater.from(mContext).inflate(R.layout.pop_base_center, null);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.pop_base_bottom, null);
        }
        View layout_outside = (View) view.findViewById(R.id.layout_outside);
        layout_outside.setOnClickListener(this);

        FrameLayout layout_content = view.findViewById(R.id.layout_content);
        layout_content.addView(LayoutInflater.from(mContext).inflate(layoutId, null));

        setContentView(view);

        int width = context.getResources().getDisplayMetrics().widthPixels;
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setWidth(width);
        setOutsideTouchable(true);
        setFocusable(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.layout_outside) {
            dismiss();
        }
    }

    @Override
    public void showAsDropDown(View anchor) {
//        if (Build.VERSION.SDK_INT >= 24) {
//            Rect rect = new Rect();
//            anchor.getGlobalVisibleRect(rect);
//            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
//            setHeight(h);
//        }
        if (Build.VERSION.SDK_INT >=24) {
            int[] a = new int[2];
            anchor.getLocationInWindow(a);
            showAtLocation(anchor, Gravity.NO_GRAVITY, 0, a[1] + anchor.getHeight() + 0);
        } else {
            super.showAsDropDown(anchor);
        }
    }

    @Override
    public void onDismiss() {
        if(mContext instanceof OnDismissListener){
            ((OnDismissListener)mContext).onDismiss();
        }
    }

    public void setOnPopResultOKListener(BasePopupWindow.onPopResultOKListener onPopResultOKListener) {
        this.onPopResultOKListener = onPopResultOKListener;
    }

    public interface onPopResultOKListener{
          void onResultOK(Object cls, Object result);
    }
}
