package com.example.diemdanhdihoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class home extends AppCompatActivity {

    private TabLayout tabLayout;
    private TabItem tabGV, tabHV;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(LoginAct.checkUser=="" || LoginAct.checkUser==null)
        {
            Intent intent = new Intent(home.this,LoginAct.class );
            startActivity(intent);
        }

        tabLayout = (TabLayout)findViewById(R.id.tabBar);
        tabGV = (TabItem)findViewById(R.id.tabLopQuanLy);
        tabHV = (TabItem)findViewById(R.id.tabLopHocVien);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),2);

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition(),true);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });


    }
}