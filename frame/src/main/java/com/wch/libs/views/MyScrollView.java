package com.wch.libs.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 自定义ScrollView 解决ScrollView中ViewPager无法正常滑动问题 解决方法只需要在接近水平滚动时ScrollView不处理事件而交由其子View(即这里的ViewPager)处理即可，
 * 重写ScrollView的onInterceptTouchEvent函数
 * 
 * @author zhangbingkang
 * @version V1.0 创建时间：2014-2-18 下午4:01:10
 */
public class MyScrollView extends ScrollView
{
    private GestureDetector mGestureDetector;
    
    public MyScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }
    
    class YScrollDetector extends SimpleOnGestureListener
    {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
        {
            /**
             * 如果我们滚动更接近水平方向,返回false,让子视图来处理它
             */
            return (Math.abs(distanceY) > Math.abs(distanceX));
        }
    }
}
