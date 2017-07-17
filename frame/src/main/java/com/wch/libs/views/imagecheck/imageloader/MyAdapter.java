package com.wch.libs.views.imagecheck.imageloader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.wch.libs.R;
import com.wch.libs.views.FileExplorer.FileUtils;
import com.wch.libs.views.FileExplorer.Global;
import com.wch.libs.views.imagecheck.utils.CommonAdapter;
import com.wch.libs.views.imagecheck.utils.ViewHolder;


public class MyAdapter extends CommonAdapter<String> {

	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public List<String> mSelectedImage = new LinkedList<String>();

	public ArrayList<String> name = new ArrayList<String>();

	/**
	 * 文件夹路径
	 */
	private String mDirPath;

	private mSelectedImageListener myListener;

	private int count;

	private Context context;

	public MyAdapter(Context context, List<String> mDatas, int itemLayoutId,
			String dirPath, int count) {
		super(context, mDatas, itemLayoutId);
		this.context = context;
		this.mDirPath = dirPath;
		this.count = count;
	}

	@Override
	public void convert(
			final ViewHolder helper,
			final String item) {
		// 设置no_pic
		helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
		// 设置no_selected
		helper.setImageResource(R.id.id_item_select,
				R.drawable.picture_unselected);
		// 设置图片
		helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);

		final ImageView mImageView = helper.getView(R.id.id_item_image);
		final ImageView mSelect = helper.getView(R.id.id_item_select);

		mImageView.setColorFilter(null);
		// 设置ImageView的点击事件
		mImageView.setOnClickListener(new OnClickListener() {
			// 选择，则将图片变暗，反之则反之
			@Override
			public void onClick(View v) {

				if (count != 0) {
					if (mSelectedImage.size() >= count) {
						if (mSelectedImage.contains(mDirPath + "/" + item)) {
							mSelectedImage.remove(mDirPath + "/" + item);
							name.remove(item);
							mSelect.setImageResource(R.drawable.picture_unselected);
							mImageView.setColorFilter(null);
							if (myListener != null) {
								myListener.getlist(mSelectedImage.size());
							}
							return;

						} else {
							Toast.makeText(
									context,
									context.getResources().getString(
											R.string.image_choose_count_top)
											+ ""
											+ count
											+ context
													.getResources()
													.getString(
															R.string.image_choose_count_end),
									Toast.LENGTH_SHORT).show();
							return;
						}
					}
				}
				// 不知道为啥会有不是图片的
				if (!FileUtils.checkEndsWithInStringArray(item,
						Global.FileTypes[Global.IndexImage])) {
					Toast.makeText(context, "该文件不是图片", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				// 已经选择过该图片
				if (mSelectedImage.contains(mDirPath + "/" + item)) {
					mSelectedImage.remove(mDirPath + "/" + item);
					name.remove(item);
					mSelect.setImageResource(R.drawable.picture_unselected);
					mImageView.setColorFilter(null);
				} else
				// 未选择该图片
				{
					mSelectedImage.add(mDirPath + "/" + item);
					name.add(item);
					mSelect.setImageResource(R.drawable.pictures_selected);
					mImageView.setColorFilter(Color.parseColor("#77000000"));
				}
				if (myListener != null) {
					myListener.getlist(mSelectedImage.size());
				}
			}
		});

		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (mSelectedImage.contains(mDirPath + "/" + item)) {
			mSelect.setImageResource(R.drawable.pictures_selected);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		}

	}

	public ArrayList<String> getmSelectedImage() {
		return name;
	}

	public String getmDirPath() {
		return mDirPath;
	}

	public void setmSelectedImageListener(mSelectedImageListener myListener) {
		this.myListener = myListener;
	}

	public interface mSelectedImageListener {
		public void getlist(int num);
	}
}
