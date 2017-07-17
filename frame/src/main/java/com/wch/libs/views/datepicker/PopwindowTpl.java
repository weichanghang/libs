package com.wch.libs.views.datepicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

/**
 * popwindow
 * 
 * @author suzhenpeng
 * @version 1.0.1
 * @since JDK 1.6 SDK 2.1
 */
public abstract class PopwindowTpl {
	public PopwindowTpl(Context context) {
		super();
		this.context = context;
		beforeInitView();
		initPop();
	}

	/**
	 * 设置布局ID
	 */
	protected abstract void beforeInitView();

	/**
	 * 初始�?
	 */
	protected void initPop() {
		init();
	}

	/**
	 * 初始�?
	 */
	protected void init() {
		initPopWindow();
		initView();
	}

	/**
	 * 初始化popwindow
	 */
	protected void initPopWindow() {
		LayoutInflater inflater = LayoutInflater.from(context);
		popView = inflater.inflate(viewID, null);
		popWindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		popWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	/**
	 * 初始化界�?
	 */
	protected abstract void initView();

	/**
	 * 界面刷新
	 */
	public abstract void update();

	/**
	 * 弹出pop
	 * 
	 * @param view
	 */
	public void showAsDropDown(View view) {
		if (popWindow != null) {
			popWindow.setFocusable(true);
			popWindow.showAsDropDown(view);
		}
	}

	/**
	 * 默认全屏中央弹出
	 */
	public void showAsDropDown(int viewID, int layoutID) {
		// 获取整体布局，定位popwindow
		LayoutInflater inflater = LayoutInflater.from(context);
		View popView = inflater.inflate(viewID, null);
		View view = popView.findViewById(layoutID);

		showAsDropDown(view);
	}

	/**
	 * @param viewID
	 *            the viewID to set
	 */
	public void setViewID(int viewID) {
		this.viewID = viewID;
	}

	public void dismiss() {
		if (popWindow == null)
			return;
		popWindow.dismiss();
	}

	/*-------------------------------------------------属�?-----------------------------------------------*/
	/** 上下文对�? */
	protected Context context;

	/** popWindow */
	protected PopupWindow popWindow;

	/** 弹出框布�? */
	protected View popView;

	/** 布局ID */
	private int viewID;

}