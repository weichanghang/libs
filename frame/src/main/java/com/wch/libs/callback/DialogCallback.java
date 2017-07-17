package com.wch.libs.callback;

import android.app.Activity;

import com.lzy.okgo.request.base.Request;
import com.wch.libs.activity.BaseActivity;

public abstract class DialogCallback<T> extends JsonCallback<T> {

    private Activity activity;

    public DialogCallback(Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        ((BaseActivity) activity).ShowLoading();
    }

    @Override
    public void onFinish() {
        ((BaseActivity) activity).CancelLoading();
    }
}
