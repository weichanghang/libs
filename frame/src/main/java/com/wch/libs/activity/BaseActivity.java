package com.wch.libs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.wch.libs.R;
import com.wch.libs.application.WchApp;
import com.wch.libs.views.dialog.LoadingDialog;


/**
 * @author weich
 *  @time 2017/6/27
 */
public class BaseActivity extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateActivity(savedInstanceState);
        initViews();
        setContentData();
    }

    /**
     * 创建Activity
     *
     * @param savedInstanceState
     */
    protected void onCreateActivity(@Nullable Bundle savedInstanceState) {

    }

    protected void initViews() {
    }

    protected void setContentData() {
    }

    /**
     * 展示提示文字
     *
     * @param msg 消息内容
     */
    public void Toast(CharSequence msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(WchApp.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 展示提示文字
     *
     * @param stringResId 消息Id
     */
    public void Toast(int stringResId) {
        Toast.makeText(WchApp.getInstance().getApplicationContext(), stringResId, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * Activity间跳转
     *
     * @param cls 目标Activity
     */
    protected void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    /**
     * Activity间跳转（传值）
     *
     * @param cls    目标Activity
     * @param bundle 传递的数据
     */
    protected void startActivity(Class<?> cls, @Nullable Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null)
            intent.putExtras(bundle);// 添加数据
        startActivity(intent);
    }

    /**
     * Activity间跳转（带返回值）
     *
     * @param cls         目标Activity
     * @param requestCode 请求码
     */
    protected void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(new Intent(this, cls), requestCode);
    }

    /**
     * Activity间跳转（带返回值且传值）
     *
     * @param cls         目标Activity
     * @param requestCode 请求码
     * @param bundle      传递的数据
     */
    protected void startActivityForResult(Class<?> cls, int requestCode, @Nullable Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null)
            intent.putExtras(bundle);// 添加数据
        startActivityForResult(intent, requestCode);
    }

    public void activityAnimationOpen() {
        overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
    }
    public void activityAnimationClose() {
        overridePendingTransition(R.anim.activity_back, R.anim.activity_finish);
    }
    public LoadingDialog ShowLoading() {
        if (loadingDialog == null)
            loadingDialog = new LoadingDialog(this);
        if (loadingDialog != null && !loadingDialog.isShowing())
            loadingDialog.show();
        return loadingDialog;
    }

    public void CancelLoading() {
        if (loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.cancel();
    }
}
