package com.yaogd.myapptest;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.yaogd.myapptest.activity.ActivityA;
import com.yaogd.myapptest.activity.ActivityB;
import com.yaogd.myapptest.activity.ActivityC;
import com.yaogd.myapptest.activity.ActivityD;

public class MainActivity extends TabActivity {

    private TabHost tabhost;
    private RadioGroup mainRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_host_view_test);

        //获取按钮
        mainRadioGroup = (RadioGroup) findViewById(R.id.main_radio_group);

        //往TabWidget添加Tab
        tabhost = getTabHost();
        tabhost.addTab(tabhost.newTabSpec("tag1").setIndicator("0").setContent(new Intent(this, ActivityA.class)));
        tabhost.addTab(tabhost.newTabSpec("tag2").setIndicator("1").setContent(new Intent(this, ActivityB.class)));
        tabhost.addTab(tabhost.newTabSpec("tag3").setIndicator("2").setContent(new Intent(this, ActivityC.class)));
        tabhost.addTab(tabhost.newTabSpec("tag4").setIndicator("3").setContent(new Intent(this, ActivityD.class)));

        //设置监听事件
        RadioCheckedListener radioListener = new RadioCheckedListener();
        mainRadioGroup.setOnCheckedChangeListener(radioListener);

    }

    //监听类
    public class RadioCheckedListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            //setCurrentTab 通过标签索引设置当前显示的内容
            //setCurrentTabByTag 通过标签名设置当前显示的内容
            switch (checkedId) {
                case R.id.tab_icon_01:
                    tabhost.setCurrentTab(0);
                    //或
                    //tabhost.setCurrentTabByTag("tag1");
                    break;
                case R.id.tab_icon_02:
                    tabhost.setCurrentTab(1);
                    break;
                case R.id.tab_icon_03:
                    tabhost.setCurrentTab(2);
                    break;
                case R.id.tab_icon_04:
                    tabhost.setCurrentTab(3);
                    break;
            }
        }
    }

}
