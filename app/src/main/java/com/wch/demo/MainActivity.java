package com.wch.demo;

import android.os.Bundle;
import android.view.View;
import com.wch.libs.activity.BaseActivity;


public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onMyClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                startActivity(com.wch.libs.demo.MainDemo.class);
                break;
        }
    }

}
