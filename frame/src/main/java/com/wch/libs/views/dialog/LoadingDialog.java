package com.wch.libs.views.dialog;

import android.content.Context;
import android.widget.TextView;

import com.wch.libs.R;
import com.wch.libs.views.dialog.xalertdialog.ProgressHelper;
import com.wch.libs.views.dialog.xalertdialog.ProgressWheel;


/**
 * 等待加载框
 */
public class LoadingDialog extends BaseDialog {
    private final TextView loadingTv;

    public LoadingDialog(Context context) {
        super(context, R.layout.dialog_loading, R.style.myDialog2, false);
        setLocation(2);
        ProgressHelper mProgressHelper = new ProgressHelper(context);
        mProgressHelper.setProgressWheel((ProgressWheel) findViewById(R.id.progressWheel));
        mProgressHelper.setBarColor(context.getResources().getColor(R.color.reddish_orange_four));
        setCanceledOnTouchOutside(false);

        loadingTv=(TextView)findViewById(R.id.loading_tv);
    }
    private void setText(CharSequence text){
        loadingTv.setText(text);
    }
}
