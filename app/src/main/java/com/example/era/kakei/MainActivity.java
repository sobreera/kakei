package com.example.era.kakei;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    private SQLiteDatabase db;
    private Database helper;
    private ContentValues values;
    EditText price;
    TextView food;
    TextView social;
    TextView recreation;
    TextView shopping;
    //当たり判定これらで取得しようとしたけどそれぞれにsetOnDragListenerセットした方が楽だったのでお蔵入り
    //Rect rect = new Rect();
    //int x,y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        price=(EditText)findViewById(R.id.editText);
        helper=new Database(getApplicationContext());
        db=helper.getWritableDatabase();
        food=(TextView)findViewById(R.id.food);
        social=(TextView)findViewById(R.id.social);
        recreation=(TextView)findViewById(R.id.recreation);
        shopping=(TextView)findViewById(R.id.shopping);
        values = new ContentValues();


        price.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                //ClipData clipData = ClipData.newPlainText("String",MainActivity.price.getText().toString());
                //ドラッグ中に表示するイメージのビルダー
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
                //ドラッグを開始
                v.startDrag(null,shadow,v,0);
                return true;
            }
        });



        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        food.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                //final int action = event.getAction();     intでもできることの確認
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                    case DragEvent.ACTION_DRAG_ENTERED:
                    case DragEvent.ACTION_DRAG_LOCATION:
                    case DragEvent.ACTION_DRAG_EXITED:
                        return true;

                    case DragEvent.ACTION_DROP:
                        /*んにゃぴ・・・ClipData使い所よくわかんなかったです・・・
                        ClipData data = event.getClipData();
                        String texData = String.valueOf(data);
                        */

                        String date = getNowDate();
                        values.put(Database.DATE,date);
                        values.put(Database.LASTDATE,date);
                        values.put(Database.CATEGORY, food.getText().toString());
                        values.put(Database.PRICE, price.getText().toString());
                        long id=db.insert(
                                Database.TABLE,
                                null,
                                values
                        );
                        Log.d(null,"insert:"+id);
                        Toast.makeText(MainActivity.this,"保存完了:"+date,Toast.LENGTH_SHORT).show();
                        return true;

                    default:
                        break;
                }

                return false;
            }
        });

        social.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                    case DragEvent.ACTION_DRAG_ENTERED:
                    case DragEvent.ACTION_DRAG_LOCATION:
                    case DragEvent.ACTION_DRAG_EXITED:
                        return true;

                    case DragEvent.ACTION_DROP:

                        String date = getNowDate();
                        values.put(Database.DATE,date);
                        values.put(Database.LASTDATE,date);
                        values.put(Database.CATEGORY,social.getText().toString());
                        values.put(Database.PRICE, price.getText().toString());
                        long id=db.insert(
                                Database.TABLE,
                                null,
                                values
                        );
                        Toast.makeText(MainActivity.this,"保存完了:"+date,Toast.LENGTH_SHORT).show();
                        Log.d(null,"insert:"+id);
                        return true;

                    default:
                        break;
                }

                return false;
            }
        });

        recreation.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                    case DragEvent.ACTION_DRAG_ENTERED:
                    case DragEvent.ACTION_DRAG_LOCATION:
                    case DragEvent.ACTION_DRAG_EXITED:
                        return true;

                    case DragEvent.ACTION_DROP:

                        String date = getNowDate();
                        values.put(Database.DATE,date);
                        values.put(Database.LASTDATE,date);
                        values.put(Database.CATEGORY,recreation.getText().toString());
                        values.put(Database.PRICE, price.getText().toString());
                        long id=db.insert(
                                Database.TABLE,
                                null,
                                values
                        );
                        Toast.makeText(MainActivity.this,"保存完了:"+date,Toast.LENGTH_SHORT).show();
                        Log.d(null,"insert:"+id);

                        return true;

                    default:
                        break;
                }

                return false;
            }
        });

        shopping.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                    case DragEvent.ACTION_DRAG_ENTERED:
                    case DragEvent.ACTION_DRAG_LOCATION:
                    case DragEvent.ACTION_DRAG_EXITED:
                        return true;

                    case DragEvent.ACTION_DROP:

                        String date = getNowDate();
                        values.put(Database.DATE,date);
                        values.put(Database.LASTDATE,date);
                        values.put(Database.CATEGORY, shopping.getText().toString());
                        values.put(Database.PRICE, price.getText().toString());
                        long id=db.insert(
                                Database.TABLE,
                                null,
                                values
                        );
                        Toast.makeText(MainActivity.this,"保存完了:"+date,Toast.LENGTH_SHORT).show();
                        Log.d(null,"insert:"+id);
                        return true;

                    default:
                        break;
                }

                return false;
            }
        });

        price.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                    case DragEvent.ACTION_DRAG_ENTERED:
                    case DragEvent.ACTION_DRAG_LOCATION:
                    case DragEvent.ACTION_DRAG_EXITED:
                    case DragEvent.ACTION_DROP:
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /*
    現在の端末日時をyyyy-MM-dd HH:mm:ss　形式で取得
     */
    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle=getString(R.string.app_name);
                break;
            case 2:
                Intent i = new Intent(MainActivity.this,AccountList.class);
                startActivity(i);
                break;
            case 3:
                Intent i2 = new Intent(MainActivity.this,SubActivity.class);
                startActivity(i2);
                //mTitle = getString(R.string.title_section3);
                //break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent i = new android.content.Intent(this,SettingActivity.class);
                startActivity(i);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
