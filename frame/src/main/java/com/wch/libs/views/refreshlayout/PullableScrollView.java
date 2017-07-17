package com.wch.libs.views.refreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class PullableScrollView extends ScrollView implements Pullable {

    private boolean Bottom = true;

    private boolean myRefresh = true;

    public PullableScrollView(Context context) {
        super(context);
    }

    public PullableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        if (!myRefresh) {
            return false;
        }
        if (getScrollY() == 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean canPullUp() {
        if (!Bottom) {
            return false;
        }
        if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
            return true;
        else
            return false;
    }

    public void setPullLoadEnable(boolean Bottom) {
        this.Bottom = Bottom;
    }

    public void setRefreshEnable(boolean myRefresh) {
        this.myRefresh = myRefresh;
    }

}
