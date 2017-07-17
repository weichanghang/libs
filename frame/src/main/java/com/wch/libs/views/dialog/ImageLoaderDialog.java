package com.wch.libs.views.dialog;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wch.libs.R;
import com.wch.libs.activity.BaseActivity;
import com.wch.libs.net.URLs;
import com.wch.libs.util.FileUtil;
import com.wch.libs.views.imagecheck.imageloader.ImageLoaderMainActivity;


public class ImageLoaderDialog extends BaseDialog implements OnClickListener {

    public static final int PHOTOHRAPH = 1;// 拍照

    public static final int IMAGE = 2;// 相册

    public String IMAGE_NAME = "";

    private int count = 0;
    private TextView blog_taking_photos, blog_album_photos, setting_canel;

    public ImageLoaderDialog(Context context, int res) {
        super(context, res);
        initView();
    }

    public ImageLoaderDialog(Context context) {
        super(context, R.layout.dialog_imageloader);
        initView();
    }

    private void initView() {
        blog_taking_photos = (TextView) findViewById(R.id.blog_taking_photos);
        blog_album_photos = (TextView) findViewById(R.id.blog_album_photos);
        setting_canel = (TextView) findViewById(R.id.setting_canel);
        blog_taking_photos.setOnClickListener(this);
        blog_album_photos.setOnClickListener(this);
        setting_canel.setOnClickListener(this);
        blog_taking_photos.setTag(1);
        blog_album_photos.setTag(2);
        setting_canel.setTag(3);

    }

    public void setText(String text1, String text2, String text3) {
        blog_taking_photos.setText(text1);
        blog_album_photos.setText(text2);
        setting_canel.setText(text3);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.blog_taking_photos) {
            setImage(true);
        } else if (i == R.id.blog_album_photos) {
            setImage(false);
        } else if (i == R.id.setting_canel) {
        } else {
        }
    }

    private void setImage(boolean takingPhotos) {
        if (count == 0) {
            return;
        }
        if (takingPhotos) {
            IMAGE_NAME = System.currentTimeMillis() + ".jpg";
            FileUtil.createFolder(URLs.ADDRESSIMAGE);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(URLs.ADDRESSIMAGE, IMAGE_NAME)));
            ((BaseActivity) mContext)
                    .startActivityForResult(intent, PHOTOHRAPH);
        } else {
            Intent photoIntent = new Intent(mContext,
                    ImageLoaderMainActivity.class);
            photoIntent.putExtra("count", count);
            ((BaseActivity) mContext)
                    .startActivityForResult(photoIntent, IMAGE);
            ((BaseActivity) mContext).activityAnimationOpen();
        }
    }

    public String getIMAGE_NAME() {
        return IMAGE_NAME;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
