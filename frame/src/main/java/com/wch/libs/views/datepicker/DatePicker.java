package com.wch.libs.views.datepicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ParseException;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;


import com.wch.libs.R;
import com.wch.libs.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * 日期选择控件
 * 
 * @author suzhenpeng
 * 
 */
public class DatePicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker yearPicker;

	private ScrollerNumberPicker monthPicker;

	private ScrollerNumberPicker dayPicker;

	/** 日历 */
	private DateUtil calendarUtil = DateUtil.getSingleton();

	/** 日历 */
	public Calendar calendar;

	/** 临时日期 */
	private int tempYearIndex = -1;

	private int tempMonthIndex = -1;

	/** 选择监听 */
	private OnSelectingListener onSelectingListener;

	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;

	public DatePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public DatePicker(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		LayoutInflater.from(getContext())
				.inflate(R.layout.su_date_picker, this);
		// 初始化日历
		calendar = Calendar.getInstance();
		calendarUtil = DateUtil.getSingleton();
		// 获取控件引用
		yearPicker = (ScrollerNumberPicker) findViewById(R.id.year);
		monthPicker = (ScrollerNumberPicker) findViewById(R.id.month);
		dayPicker = (ScrollerNumberPicker) findViewById(R.id.day);
		// 获取当前年月日
		int y = calendar.get(Calendar.YEAR);
		int m = calendar.get(Calendar.MONTH);
		int d = calendar.get(Calendar.DAY_OF_MONTH);
		// 设置数据
		yearPicker.setData(calendarUtil.getYears());
		monthPicker.setData(calendarUtil.getMonths());
		dayPicker.setData(calendarUtil.getDays(y, m));
		// 设置默认值为当前
		yearPicker.setDefault(y - 1970);
		monthPicker.setDefault(m);
		dayPicker.setDefault(d - 1);

		yearPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub
				if (text == null || text.equals(""))
					return;
				if (tempYearIndex != id) {
					String selectDay = dayPicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					String selectMonth = monthPicker.getSelectedText();
					if (selectMonth == null || selectMonth.equals(""))
						return;
					dayPicker.setData(calendarUtil.getDays(
							Integer.valueOf(text),
							Integer.valueOf(selectMonth) - 1));
					int lastDay = Integer.valueOf(dayPicker
							.getItemText(dayPicker.getListSize() - 1));
					if (Integer.valueOf(selectDay) > lastDay) {
						dayPicker.setDefault(lastDay - 1);
					} else {
						dayPicker.setDefault(Integer.valueOf(selectDay) - 1);
					}
				}
				tempYearIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);

			}

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub

			}
		});

		monthPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub
				if (text.equals("") || text == null)
					return;
				if (tempMonthIndex != id) {
					String selectDay = dayPicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					String selectYear = yearPicker.getSelectedText();
					if (selectYear == null || selectYear.equals(""))
						return;
					dayPicker.setData(calendarUtil.getDays(
							Integer.valueOf(selectYear),
							Integer.valueOf(text) - 1));
					int lastDay = Integer.valueOf(dayPicker
							.getItemText(dayPicker.getListSize() - 1));
					if (Integer.valueOf(selectDay) > lastDay) {
						dayPicker.setDefault(lastDay - 1);
					} else {
						dayPicker.setDefault(Integer.valueOf(selectDay) - 1);
					}
				}
				tempMonthIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub

			}
		});

		dayPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub

			}

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}
		});
	}

	public void setTime(long time) {
		setDate(new Date(time));
	}

	public void setDate(Date date) {
		int year;
		if (date.getYear() < 70) {
			year = (70 + 1900);
		} else {
			year = (date.getYear() + 1900);
		}
		String a = DateUtil.ConverToString(date, "yyyyMMdd");
		setTime(Integer.valueOf(a.substring(0, 4)),
				Integer.valueOf(a.substring(4, 6)),
				Integer.valueOf(a.substring(6, 8)));
	}

	public void setTime(int year, int month, int day) {
		if (month > 12)
			return;
		if (day > 31)
			return;
		if (month <= 0 || year <= 0 || day <= 0)
			return;

		// 设置数据
		yearPicker.setData(calendarUtil.getYears());
		monthPicker.setData(calendarUtil.getMonths());
		dayPicker.setData(calendarUtil.getDays(year, month - 1));
		// 设置默认值为当前
		yearPicker.setDefault(year - 1970);
		monthPicker.setDefault(month - 1);
		if ((day - 1) >= calendarUtil.getDays(year, month - 1).size()) {
			dayPicker
					.setDefault(calendarUtil.getDays(year, month - 1).size() - 1);
		} else {
			dayPicker.setDefault(day - 1);
		}
	}

	/**
	 * 获取时间
	 * 
	 * @return
	 */
	public Date getDate() {
		String timeString = yearPicker.getSelectedText() + "-"
				+ monthPicker.getSelectedText() + "-"
				+ dayPicker.getSelectedText();
		Date date = new Date(
				Long.valueOf(formatToTime(timeString, "yyyy-MM-dd")));
		return date;
	}

	/**
	 * 获取时间戳
	 * 
	 * @return
	 */
	public long getTime() {
		return getDate().getTime();
	}

	public int getYear() {
		return Integer.valueOf(yearPicker.getSelectedText());
	}

	public int getMonth() {
		return Integer.valueOf(monthPicker.getSelectedText());
	}

	public int getDay() {
		return Integer.valueOf(dayPicker.getSelectedText());
	}

	public String getFormaTime() {
		return (yearPicker.getSelectedText() + "-"
				+ monthPicker.getSelectedText() + "-" + dayPicker
					.getSelectedText());
	}

	public String getFormatTime(String format) {
		return getFormatTime(getTime(), format);
	}

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	/**
	 * 从格式化时间获取time
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static String formatToTime(String time, String format) {
		Date date = null;
		SimpleDateFormat formatTime = new SimpleDateFormat(format, Locale.CHINA);
		try {
			try {
				date = formatTime.parse(time);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return String.valueOf(date.getTime());
	}

	/**
	 * 返回指定格式时间
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static String getFormatTime(long time, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
		return dateFormat.format(time);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null)
					onSelectingListener.selected(true);
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 
	 * 日期监听
	 * 
	 * @author suzhenpeng
	 * 
	 */
	public interface OnSelectingListener {

		public void selected(boolean selected);
	}

}
