package com.example.era.kakei;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity
        implements View.OnDragListener {

    private SQLiteDatabase db;
    private Database helper;
    private ContentValues values;
    static int newYosan;
    static int oldYosan;
    static String oldDate;
    static String getMonth;
    TextView price;
    Button up,right,left,bottom;
    SharedPreferences data;
    SharedPreferences.Editor editor;
    NavigationView mNavigationView;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        data = getSharedPreferences("settings", MODE_PRIVATE);
        editor = data.edit();

        if (!data.getBoolean("Tutorial",false)) {
            editor.putBoolean("Tutorial",true);
            editor.apply();
            Intent i = new Intent(this,TutorialActivity.class);
            startActivity(i);
        }

        price=(TextView)findViewById(R.id.editText);
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("使用額");
                builder.setMessage("金額を入力してください");
                builder.setCancelable(false);
                builder.setView(editText);
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
                // フィルターを作成
                InputFilter inputFilter = new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        if (source.toString().matches("^[0-9]+$")) {
                            return source;
                        } else {
                            return "";
                        }
                    }
                };
                // フィルターの配列を作成
                InputFilter[] filters = new InputFilter[]{inputFilter};
                // フィルターの配列をセット
                editText.setFilters(filters);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(editText.getText().toString().equals("")){
                            price.setText("");
                        }else {
                            if(Integer.parseInt(editText.getText().toString())!=0) {
                                price.setText(editText.getText().toString());
                            }else {
                                price.setText("");
                            }
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }

        });


        helper=new Database(getApplicationContext());
        db=helper.getWritableDatabase();
        up=(Button)findViewById(R.id.up);
        right=(Button)findViewById(R.id.right);
        left=(Button)findViewById(R.id.left);
        bottom=(Button)findViewById(R.id.bottom);
        values = new ContentValues();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        // You were missing this setHomeAsUpIndicator
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView)findViewById(R.id.navigation);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent i;
                switch (item.getItemId()){
                    case R.id.home:
                        break;
                    case R.id.list:
                        i = new Intent(getApplicationContext(),AccountList.class);
                        startActivity(i);
                        break;
                    case R.id.setting:
                        i = new Intent(getApplicationContext(),SettingActivity.class);
                        startActivity(i);
                        break;
                }
                mDrawerLayout.closeDrawers();
                return false;
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);

        settings();
    }

    public void onResume(){
        super.onResume();
        settings();

        price.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //ClipData clipData = ClipData.newPlainText("String",MainActivity.price.getText().toString());
                //ドラッグ中に表示するイメージのビルダー
                View.DragShadowBuilder shadow = new MyDragShadowBuilder(v);
                //ドラッグを開始
                if (price.getText().toString().equals("") ) return false;
                else v.startDrag(null, shadow, v, 0);
                return true;
            }
        });

        up.setOnDragListener(this);
        right.setOnDragListener(this);
        left.setOnDragListener(this);
        bottom.setOnDragListener(this);

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
        data = getSharedPreferences("settings", MODE_PRIVATE);
        editor =data.edit();
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
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
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



    @Override
    public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DROP:
                    switch (v.getId()){
                        case R.id.up:
                            dragger(up.getText().toString());
                            break;
                        case R.id.bottom:
                            dragger(bottom.getText().toString());
                            break;
                        case R.id.right:
                            dragger(right.getText().toString());
                            break;
                        case R.id.left:
                            dragger(left.getText().toString());
                            break;
                    }
                default:
                    break;
            }

        return true;
    }

    public boolean dragger(String category){
            String date = getNowDate();
            values.put(Database.DATE, date);
            values.put(Database.LASTDATE, date);
            values.put(Database.CATEGORY, category);
            values.put(Database.PRICE, price.getText().toString());
            values.put(Database.YOSAN, oldYosan);
            long id = db.insert(
                    Database.TABLE,
                    null,
                    values
            );

            Log.d(null, "insert:" + id);
            Toast.makeText(this, "保存完了:" + date, Toast.LENGTH_SHORT).show();
            price.setText("");
            return true;

//        Log.d("DragEvent:::::",""+event.getAction()+"::::ACTIONDROP:::"+DragEvent.ACTION_DRAG_ENDED);

    }
}
