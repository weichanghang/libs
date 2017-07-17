package com.wch.libs.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author hanweipeng
 * @date 2014年10月18日 下午2:34:38
 */
public class MyListview extends ListView
{
    
    public MyListview(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    public MyListview(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    
    public MyListview(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
