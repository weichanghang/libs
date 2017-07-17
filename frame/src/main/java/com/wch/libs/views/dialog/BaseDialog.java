package com.wch.libs.views.dialog;

import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.wch.libs.R;
import com.wch.libs.callback.Listener.ClickDialog;


public class BaseDialog extends Dialog implements View.OnClickListener {
    protected Context mContext;
    protected ClickDialog click;
    protected int tag = 0;

    public BaseDialog(Context context, int res, int style) {
        super(context, style);
        init(context, res,true);
    }
    public BaseDialog(Context context, int res, int style, boolean noAnimations) {
        super(context, style);
        init(context, res,noAnimations);
    }

    public BaseDialog(Context context, int res) {
        super(context, R.style.myDialog);
        init(context, res,true);
    }

    private void init(Context context, int res, boolean noAnimations) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(res, null);
        setContentView(layout);
        // 设置window属性
        Window win = getWindow();
        if (noAnimations)
            win.setWindowAnimations(R.style.mystyle);
        win.setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
        win.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public void setLocation(int lo) {
        if (lo == 1) {
            getWindow().setWindowAnimations(R.style.mystyle_top);
            getWindow().setGravity(Gravity.TOP);
        } else if (lo == 2) {
            getWindow().setGravity(Gravity.CENTER);
        }
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setOnClick(ClickDialog click) {
        this.click = click;
    }

    @Override
    public void onClick(View v) {
        if (click != null) {
            int position = 0;
            if (v.getTag() != null) {
                position = (int) v.getTag();
            }
            click.choose(tag, position, v);
        }
        if (isShowing()) {
            cancel();
        }
    }
}
