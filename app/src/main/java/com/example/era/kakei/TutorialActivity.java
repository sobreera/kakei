package com.example.era.kakei;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class TutorialActivity extends ActionBarActivity {

    private FragmentPagerAdapter mPagerAdapter = null;
    private ViewPager mViewPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        //FragmentPagerAdapter の設定
        mPagerAdapter = new TutorialPagerAdapter(getSupportFragmentManager());

        //ViewPager の設定
        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPager.setAdapter(mPagerAdapter);
    }

    public  class TutorialPagerAdapter extends FragmentPagerAdapter {

        //最大ページ数を1000に
        public static final int MaxPage = 6;

        public  TutorialPagerAdapter(android.support.v4.app.FragmentManager fm){
            super(fm);
        }

        //画面表示するFragmentを返す
        @Override
        public Fragment getItem(int position){

            Bundle bundle = new Bundle();
            bundle.putInt("position",position);
            Log.d(null,"ActivityPosition:"+position);

            TutorialFragment fragment = new TutorialFragment();
            //Bundle情報をセット
            fragment.setArguments(bundle);
            return fragment;

        }

        //表示するFragment 数を返す
        @Override
        public int getCount(){
            //最大ページ数返しとけばいい
            return MaxPage;
        }

        //ページタイトルを返す
        @Override
        public CharSequence getPageTitle(int position){
            String Title ="";
            if(position==5){
                Title="";
            }else {
                Title = (position + 1) + "ページ目";
            }
            return Title;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tutorial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
