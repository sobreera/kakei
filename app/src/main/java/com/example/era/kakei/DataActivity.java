package com.example.era.kakei;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.renderscript.Sampler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DataActivity extends ActionBarActivity {
    private TextView date;
    private EditText category;
    private EditText price;
    private EditText memo;
    private TextView lastDate;
    private Database helper;
    private SQLiteDatabase db;
    private ContentValues values;
    private String dataId;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        setTitle("うまいラーメン屋の屋台");

        date=(TextView)findViewById(R.id.date);
        category=(EditText)findViewById(R.id.category);
        price=(EditText)findViewById(R.id.price);
        memo=(EditText)findViewById(R.id.memo);
        lastDate=(TextView)findViewById(R.id.lastDate);
        helper=new Database(getApplicationContext());
        db=helper.getWritableDatabase();
        values=new ContentValues();

        button=(Button)findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DataActivity.this,SubActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        Intent i = getIntent();
        date.setText(i.getStringExtra("date"));
        category.setText(i.getStringExtra("category"));
        price.setText(i.getStringExtra("price"));
        memo.setText(i.getStringExtra("memo"));
        lastDate.setText("最終更新日:"+i.getStringExtra("last"));
        dataId=i.getStringExtra("id");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data, menu);
        return true;
    }

    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.save){
            String date = getNowDate();
            values.put("lastDate",date);
            values.put("category",category.getText().toString());
            values.put("price",price.getText().toString());
            values.put("memo",memo.getText().toString());
            db.update(
                    Database.TABLE,
                    values,
                    Database.ID + "=" + dataId,
                    null
            );
            Toast.makeText(DataActivity.this,"保存完了:"+date,Toast.LENGTH_SHORT).show();
            finish();
            return true;
        } else if(id==android.R.id.home){
            finish();
        }

        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }
}
