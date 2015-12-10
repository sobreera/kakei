package com.example.era.kakei;

import android.app.AlertDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.nend.android.NendAdView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class AccountList extends FragmentActivity {

    private FragmentPagerAdapter mPagerAdapter = null;
    private ViewPager mViewPager = null;
    Calendar currentDate;
    int defaultPosition;
    String newYosanSt;
    int newYosan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);

        //現在の日時
        currentDate = Calendar.getInstance();

        //FragmentPagerAdapter の設定
        mPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());

        //ViewPager の設定
        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPager.setAdapter(mPagerAdapter);

        SharedPreferences data = getSharedPreferences("settings", MODE_PRIVATE);
        newYosanSt = data.getString("new_yosan","10000");
        newYosan = Integer.parseInt(newYosanSt);


//        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.root);
//        // 1 NendAdView をインスタンス化
//        NendAdView nendAdView = new NendAdView(getApplicationContext(),407896,"1c9d695be3d17a55622722e08e7e6160a8bc028e");
//
//        // 中央下部表示の場合
//        rootLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
//        // 2 NendAdView をレイアウトに追加
//        rootLayout.addView(nendAdView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        // 3 広告の取得を開始
//        nendAdView.loadAd();
//        Log.d("nendAdView:::::::::","load!!!!!!");
    }


    @Override
    public void onResume() {
        super.onResume();


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        defaultPosition = MyFragmentPagerAdapter.MaxPage/2;
        mViewPager.setCurrentItem(defaultPosition, false);

        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.root);
        // 1 NendAdView をインスタンス化
        NendAdView nendAdView = new NendAdView(getApplicationContext(),407896,"1c9d695be3d17a55622722e08e7e6160a8bc028e");

        // 中央下部表示の場合
        rootLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        // 2 NendAdView をレイアウトに追加
        rootLayout.addView(nendAdView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        // 3 広告の取得を開始
    }

    //ViewPager 用のAdapter の設定
    //メモリ使用量が多い、Fragmentを大量に生成するような場合は
    //FragmentPagerAdapter を検討
    public  class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        //最大ページ数を1000に
        public static final int MaxPage = 1000;

        public  MyFragmentPagerAdapter(android.support.v4.app.FragmentManager fm){
            super(fm);
        }

        //画面表示するFragmentを返す
        @Override
        public Fragment getItem(int position){

            //初期位置からの位置
            int targetPosition = position-defaultPosition;

            String Title = getNowDate(targetPosition);

            //取得した日時をbundleでFragment側に渡す
            Bundle bundle = new Bundle();
            bundle.putString("date",Title);
            bundle.putInt("newYosan", newYosan);

            //Fragmentを作成
            ListFragment fragment = new ListFragment();
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

            int targetPosition = position-defaultPosition;

            String Title = getNowDate(targetPosition);

            String[] split = Title.split("-", 0);
            int split1 = Integer.parseInt(split[1]);
            split[1] = String.valueOf(split1);
            Title = split[0]+"年"+split[1]+"月";
            return Title;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }


/*
    public void setAdapter() {
        arr = new ArrayList<>();
        String now = getNowDate2();
        now = now.replace("0", "");
        String[] nowSplit = now.split("-", 0);
        if (sort) {
            c = db.rawQuery("select * from myData where date like '%' || ? || '%' escape '$' order by date asc", new String[]{searchWord});
        } else {
            c = db.rawQuery("select * from myData where date like '%' || ? || '%' escape '$' order by date desc", new String[]{searchWord});
        }
        while (c.moveToNext()) {
            String dbDate = nowSplit[1] + "/" + nowSplit[2];
            String dbCategory = c.getString(c.getColumnIndex(Database.CATEGORY));
            int dbPrice = c.getInt(c.getColumnIndex(Database.PRICE));
            sum = sum + dbPrice;
            Log.d(null, "sum:" + sum);
            arr.add(dbDate + "　" + dbCategory + ":" + dbPrice + "円");
        }
        adapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.custom_text_list_item,
                arr
        );

        listView.setAdapter(adapter);
    }
*/
    public String getNowDate(int targetPosition){
        //日時取得
        Calendar targetDate = (Calendar) currentDate.clone();
        targetDate.add(Calendar.MONTH, targetPosition);
        String Title = convertDefault("yyyy-MM-dd", targetDate.getTime());
        return Title;
    }

    public String convertDefault(String format,Date date){
        Locale loc = Locale.getDefault();
        DateFormat df = new SimpleDateFormat(format, loc);
        TimeZone zone = TimeZone.getDefault();
        df.setTimeZone(zone);
        return df.format(date);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
/*
        if (id == R.id.sort) {
            //降順昇順切り替え用
            if (sort) {
                sort = false;
            } else {
                sort = true;
            }
            setAdapter();
        }
*/
        if (id == R.id.action_settings) {
            Intent i = new android.content.Intent(this, SettingActivity.class);
            startActivity(i);
            return true;
        }
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}