package com.wch.libs.views.datepicker;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wch.libs.R;

import java.util.Date;


public class DatePickerDialog extends Dialog {
	private Context mContext;

	private Dialog mDialog;

	/** 确认事件 */
	private setOkListener okListener;

	/** 取消事件 */
	private OnClickListener cancelListener;

	/** 确认按钮 */
	private Button ok;

	/** 取消按钮 */
	private Button cancel;

	/** 日历控件 */
	private DatePicker datePicker;

	/** 时间选择 */
	private TextView time;

	public DatePickerDialog(Context context) {
		super(context, R.style.myDialog);
		this.mContext = context;
		mDialog = new Dialog(mContext, R.style.myDialog);// 自定义Dialog的样式
		initView();
	}

	private void initView() {
		mDialog.setContentView(R.layout.date_picker_dialog);
		ok = (Button) mDialog.findViewById(R.id.ok);
		cancel = (Button) mDialog.findViewById(R.id.cancel);
		datePicker = (DatePicker) mDialog.findViewById(R.id.datePicker);
		time = (TextView) mDialog.findViewById(R.id.time);
		time.setText(datePicker.getFormaTime());
		datePicker.setOnSelectingListener(new DatePicker.OnSelectingListener() {

			@Override
			public void selected(boolean selected) {
				if (!selected)
					return;
				time.setText(datePicker.getFormaTime());

			}
		});
		// 确认事件
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (okListener != null) {
					okListener.isOk(true);
				}
				mDialog.dismiss();
			}
		});
		// 取消事件
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (okListener != null) {
					okListener.isOk(false);
				}
				mDialog.dismiss();
			}
		});
	}

	public void show() {
		if (mDialog != null && !mDialog.isShowing()) {
			mDialog.show();
		}
	}

	public void dismiss(int position) {
		if (mDialog.isShowing()) {
			mDialog.cancel();
		}
	}

	public void setTime(long time) {
		if (datePicker == null)
			return;
		datePicker.setTime(time);
	}

	public void setDate(Date date) {
		if (datePicker == null)
			return;
		datePicker.setDate(date);
		time.setText(datePicker.getFormaTime());
	}

	public void setTime(int year, int month, int day) {
		if (datePicker == null)
			return;
		datePicker.setTime(year, month, day);
	}

	public Date getDate() {
		if (datePicker == null) {
			return new Date(System.currentTimeMillis());
		}
		return datePicker.getDate();
	}

	public long getTime() {
		if (datePicker == null)
			return -1L;
		return datePicker.getTime();
	}

	public int getYear() {
		if (datePicker == null)
			return -1;
		return datePicker.getYear();
	}

	public int getMonth() {
		if (datePicker == null)
			return -1;
		return datePicker.getMonth();
	}

	public int getDay() {
		if (datePicker == null)
			return -1;
		return datePicker.getDay();
	}

	public String getFormaTime() {
		if (datePicker == null)
			return "";
		return datePicker.getFormaTime();
	}

	public String getFormatTime(String format) {
		if (datePicker == null)
			return "";
		return datePicker.getFormatTime(format);
	}

	public DatePicker getDatePicker() {
		return datePicker;
	}

	public void setOkListener(setOkListener onClickListener) {
		this.okListener = onClickListener;
	}

	public interface setOkListener {
		public void isOk(boolean isOk);
	}
}
