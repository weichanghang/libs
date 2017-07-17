package com.wch.libs.demo;

import android.os.Bundle;
import android.view.View;

import com.wch.libs.R;
import com.wch.libs.activity.BaseActivity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class BmobDemo extends BaseActivity {

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        super.onCreateActivity(savedInstanceState);
        setContentView(R.layout.demo_bmob);
    }

    @Override
    protected void initViews() {
        super.initViews();
        findViewById(R.id.click1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }

    private void add() {
        Person p2 = new Person();
        p2.setName("lucky");
        p2.setAddress("北京海淀");
        p2.save(listener);
    }

    SaveListener<String> listener = new SaveListener<String>() {
        @Override
        public void done(String objectId, BmobException e) {
            if (e == null) {
                Toast("添加数据成功，返回objectId为：" + objectId);
            } else {
                Toast("创建数据失败：" + e.getMessage());
            }
        }
    };


    public class Person extends BmobObject {
        private String name;
        private String address;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getAddress() {
            return address;
        }
        public void setAddress(String address) {
            this.address = address;
        }
    }
}
