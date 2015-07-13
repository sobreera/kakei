package com.example.era.kakei;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
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
    static int newYosan;
    static int oldYosan;
    static String oldDate;
    static String getMonth;
    EditText price;
    Button up,right,left,bottom;
    //当たり判定これらで取得しようとしたけどそれぞれにsetOnDragListenerセットした方が楽だったのでお蔵入り
    //Rect rect = new Rect();
    //int x,y;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        price=(EditText)findViewById(R.id.editText);
        //price.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        helper=new Database(getApplicationContext());
        db=helper.getWritableDatabase();
        up=(Button)findViewById(R.id.up);
        right=(Button)findViewById(R.id.right);
        left=(Button)findViewById(R.id.left);
        bottom=(Button)findViewById(R.id.bottom);
        values = new ContentValues();

        settings();
    }

    public void onResume(){
        super.onResume();
        price.getEditableText().clear();
        settings();

        price.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //ClipData clipData = ClipData.newPlainText("String",MainActivity.price.getText().toString());
                //ドラッグ中に表示するイメージのビルダー
                View.DragShadowBuilder shadow = new MyDragShadowBuilder(v);
                //ドラッグを開始
                v.startDrag(null, shadow, v, 0);
                return true;
            }
        });

        up.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                //final int action = event.getAction();     intでもできることの確認
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        //ドラッグ開始時に呼び出し
                    case DragEvent.ACTION_DRAG_ENDED:
                        //ドラッグ終了時に呼び出し
                    case DragEvent.ACTION_DRAG_ENTERED:
                        //ドラッグ開始直後に呼び出し
                    case DragEvent.ACTION_DRAG_EXITED:
                        //ドラッグ終了直前に呼び出し
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        //ドラッグ中に呼び出し
                        return true;

                    case DragEvent.ACTION_DROP:
                        /*
                        ClipData使い所よくわかんなかったです・・・
                        ClipData data = event.getClipData();
                        String texData = String.valueOf(data);
                        */


                        String date = getNowDate();
                        values.put(Database.DATE, date);
                        values.put(Database.LASTDATE, date);
                        values.put(Database.CATEGORY, up.getText().toString());
                        values.put(Database.PRICE, price.getText().toString());
                        values.put(Database.YOSAN, oldYosan);
                        long id = db.insert(
                                Database.TABLE,
                                null,
                                values
                        );

                        Log.d(null, "insert:" + id);
                        Toast.makeText(MainActivity.this, "保存完了:" + date, Toast.LENGTH_SHORT).show();
                        price.getEditableText().clear();


                        return true;

                    default:
                        break;
                }

                return false;
            }
        });

        right.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                    case DragEvent.ACTION_DRAG_ENTERED:
                    case DragEvent.ACTION_DRAG_EXITED:
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        return true;

                    case DragEvent.ACTION_DROP:


                        String date = getNowDate();
                        values.put(Database.DATE, date);
                        values.put(Database.LASTDATE, date);
                        values.put(Database.CATEGORY, right.getText().toString());
                        values.put(Database.PRICE, price.getText().toString());
                        values.put(Database.YOSAN, oldYosan);
                        long id = db.insert(
                                Database.TABLE,
                                null,
                                values
                        );
                        Toast.makeText(MainActivity.this, "保存完了:" + date, Toast.LENGTH_SHORT).show();
                        Log.d(null, "insert:" + id);
                        price.getEditableText().clear();


                        return true;

                    default:
                        break;
                }

                return false;
            }
        });

        left.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                    case DragEvent.ACTION_DRAG_ENTERED:
                    case DragEvent.ACTION_DRAG_EXITED:
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        return true;

                    case DragEvent.ACTION_DROP:


                        String date = getNowDate();
                        values.put(Database.DATE, date);
                        values.put(Database.LASTDATE, date);
                        values.put(Database.CATEGORY, left.getText().toString());
                        values.put(Database.PRICE, price.getText().toString());
                        values.put(Database.YOSAN, oldYosan);
                        long id = db.insert(
                                Database.TABLE,
                                null,
                                values
                        );
                        Toast.makeText(MainActivity.this, "保存完了:" + date, Toast.LENGTH_SHORT).show();
                        Log.d(null, "insert:" + id);
                        price.getEditableText().clear();

                        return true;

                    default:
                        break;
                }

                return false;
            }
        });

        bottom.setOnDragListener(new View.OnDragListener() {
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
                        values.put(Database.DATE, date);
                        values.put(Database.LASTDATE, date);
                        values.put(Database.CATEGORY, bottom.getText().toString());
                        values.put(Database.PRICE, price.getText().toString());
                        values.put(Database.YOSAN, oldYosan);
                        long id = db.insert(
                                Database.TABLE,
                                null,
                                values
                        );
                        Toast.makeText(MainActivity.this, "保存完了:" + date, Toast.LENGTH_SHORT).show();
                        Log.d(null, "insert:" + id);
                        price.getEditableText().clear();


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


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    /*
    現在の端末日時をyyyy-MM-dd HH:mm:ss　形式で取得
     */
    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    public static String getNowMonth(){
        final DateFormat df = new SimpleDateFormat("yyyy-MM");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    public void settings(){
        SharedPreferences data = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor =data.edit();
        //editor.clear();
        //editor.commit();
        String newYosanSt = data.getString("new_yosan", "10000");
        String oldYosanSt = data.getString("old_yosan", "10000");
        newYosan = Integer.parseInt(newYosanSt);
        oldYosan = Integer.parseInt(oldYosanSt);
        Log.d(null,"oldYosan:"+oldYosan);
        Log.d(null,"newYosan:"+newYosan);
        oldDate = data.getString("old_date", null);
        getMonth = getNowMonth();
        Log.d(null, "oldDate:" + oldDate + " getMonth:" + getMonth);
        editor.putString("new_date", getMonth);
        if(!getMonth.equals(oldDate)){
            editor.putString("old_yosan",newYosanSt);
            Log.d(null,"true");
        }else{
            Log.d(null,"false");
        }
        editor.putString("old_date", getMonth);
        editor.apply();

        Log.d(null, "oldYosan:" + oldYosan);
        Log.d(null, "newYosan:" + newYosan);

        String upName = data.getString("up_edit", "食費");
        String rightName = data.getString("right_edit", "交際費");
        String leftName = data.getString("left_edit", "娯楽費");
        String bottomName = data.getString("bottom_edit","買い物費");
        up.setText(upName);
        right.setText(rightName);
        left.setText(leftName);
        bottom.setText(bottomName);
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
                break;
            case 2:
                Intent i = new Intent(MainActivity.this,AccountList.class);
                startActivity(i);
                break;
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

    private static class MyDragShadowBuilder extends View.DragShadowBuilder {
        /**
         * コンストラクタ
         */
        public MyDragShadowBuilder(View v) {
            super(v);
        }

        /**
         * ドラッグ中のイメージを描画する際にシステムが呼び出すメソッド
         */
        @Override
        public void onDrawShadow(Canvas canvas)
        {
            // ドラッグ対象View
            View view = getView();

            // Viewのキャプチャを取得する準備
            view.setDrawingCacheEnabled(true);
            view.destroyDrawingCache();

            // キャプチャを取得し、キャンバスへ描画する
            Bitmap bitmap = view.getDrawingCache();
            canvas.drawBitmap(bitmap, 0f, 0f, null);
        }
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
