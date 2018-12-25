package com.foxconn.bottomnavigationviewdemo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Demo2Activity extends AppCompatActivity {

    private List<Fragment> fragments;
    BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo2);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setItemIconTintList(null);
//        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        fragments = new ArrayList<>();
        List<String> titles=new ArrayList<>();
        titles.add(getResources().getString(R.string.title_home));
        titles.add(getResources().getString(R.string.title_catrgray));
        titles.add(getResources().getString(R.string.title_plus));
        titles.add(getResources().getString(R.string.title_find));
        titles.add(getResources().getString(R.string.title_me));
        for (int i = 0; i <titles.size() ; i++) {
            fragments.add(DemoFragment.newInstance(titles.get(i)));
        }
        VpAdapter adapter = new VpAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        floatingActionButton= (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Drawable drawable=getResources().getDrawable(R.drawable.plus_selected);
                floatingActionButton.setImageResource(R.drawable.plus_selected);
                viewPager.setCurrentItem(2);
                bottomNavigationView.setSelectedItemId(R.id.menu_empty);
            }
        });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int position=0;
            switch (item.getItemId()) {
                case R.id.navigation_index:
                    position=0;
                    break;
                case R.id.navigation_catrgray:
                    position=1;
                    break;
                case R.id.menu_empty:
                    position=2;
                    break;
                case R.id.navigation_find:
                    position=3;
                    break;
                case R.id.navigation_me:
                    position=4;
                    break;
            }
            Log.d("MainActivity","当前选中："+position);
            viewPager.setCurrentItem(position);
            if (position==2){
                floatingActionButton.setImageResource(R.drawable.plus_selected);
            }else {
                floatingActionButton.setImageResource(R.drawable.plus_normal);
            }
            return true;
        }
    };
    ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            int id=bottomNavigationView.getMenu().getItem(i).getItemId();
            bottomNavigationView.setSelectedItemId(id);
            if (i==2){
                floatingActionButton.setImageResource(R.drawable.plus_selected);
            }else {
                floatingActionButton.setImageResource(R.drawable.plus_normal);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private static class VpAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> data;

        public VpAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }
    }
}
