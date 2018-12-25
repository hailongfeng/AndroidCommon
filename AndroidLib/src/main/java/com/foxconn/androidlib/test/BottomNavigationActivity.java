package com.foxconn.androidlib.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.foxconn.androidlib.R;
import com.foxconn.androidlib.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigationActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private List<Fragment> fragments;
    BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_index:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_catrgray:
                    mTextMessage.setText(R.string.title_catrgray);
                    return true;
                case R.id.navigation_find:
                    mTextMessage.setText(R.string.title_find);
                    return true;
                case R.id.navigation_me:
                    mTextMessage.setText(R.string.title_me);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        mTextMessage = (TextView) findViewById(R.id.message);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setItemIconTintList(null);
//        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        fragments = new ArrayList<>();
        List<String> titles=new ArrayList<>();
        titles.add(getResources().getString(R.string.title_home));
        titles.add(getResources().getString(R.string.title_catrgray));
        titles.add(getResources().getString(R.string.title_find));
        titles.add(getResources().getString(R.string.title_me));
        for (int i = 0; i <titles.size() ; i++) {
            fragments.add(DemoFragment.newInstance(titles.get(i)));
        }
        VpAdapter adapter = new VpAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }


   BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
       @Override
       public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
           int order=menuItem.getOrder();
           int id=menuItem.getItemId();
           if (id==R.id.navigation_index){
               viewPager.setCurrentItem(0);
           }else if (id==R.id.navigation_catrgray){
               viewPager.setCurrentItem(1);
           }else if (id==R.id.navigation_find){
               viewPager.setCurrentItem(2);
           }else if (id==R.id.navigation_me){
               viewPager.setCurrentItem(3);
           }
           LogUtil.d("当前选中："+id);
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
