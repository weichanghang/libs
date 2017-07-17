package com.wch.libs.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wch.libs.R;
import com.wch.libs.activity.BaseActivity;
import com.wch.libs.views.datepicker.DatePickerDialog;
import com.wch.libs.views.dialog.ImageLoaderDialog;

import java.util.ArrayList;
import java.util.Date;


public class MainDemo extends BaseActivity {

    private ImageLoaderDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_main);

    }

    public void onMyClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_0) {
            startActivity(XDialogDemo.class);
        } else if (i == R.id.bt_1) {
            startActivity(BmobDemo.class);
        } else if (i == R.id.bt_2) {
            startActivity(GetuiDemo.class);
        } else if (i == R.id.bt_3) {
            ShowLoading();
        } else if (i == R.id.bt_4) {
            dialog = new ImageLoaderDialog(this);
            dialog.setCount(1);
            dialog.show();
        } else if (i == R.id.bt_5) {
            startActivity(RefreshDemo.class);
        } else if (i == R.id.bt_6) {
            DatePickerDialog datepickerdialog = new DatePickerDialog(MainDemo.this);
            datepickerdialog.setDate(new Date());
            datepickerdialog.show();

            datepickerdialog.setOkListener(new DatePickerDialog.setOkListener() {
                @Override
                public void isOk(boolean isOk) {
                }
            });
        }else if (i == R.id.bt_7) {
            startActivity(ScanActivity.class);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageLoaderDialog.PHOTOHRAPH) {
            if (resultCode != RESULT_OK) {
                return;
            }
            //imageUrl = URLs.ADDRESSIMAGE + dialog.getIMAGE_NAME();
            // imageName = dialog.getIMAGE_NAME();
        } else if (requestCode == ImageLoaderDialog.IMAGE) {
            if (data == null) {
                return;
            }
            ArrayList<String> imagelist = data
                    .getStringArrayListExtra("selectedImage");
            String mDirPath = data.getStringExtra("mDirPath");
            if (imagelist == null || imagelist.size() < 1) {
                return;
            } else {
                // imageUrl = mDirPath + "/" + imagelist.get(0);
                // imageName = imagelist.get(0);
            }
        }
        // imageLoader.displayImage("file://" + imageUrl,userImage);
    }
}
