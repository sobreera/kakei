package com.example.era.kakei;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.renderscript.Sampler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


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

    }

    @Override
    public void onResume(){
        super.onResume();
        Intent i = getIntent();
        date.setText(i.getStringExtra("date"));
        category.setText(i.getStringExtra("category"));
        price.setText(i.getStringExtra("price"));
        memo.setText(i.getStringExtra("memo"));
        lastDate.setText(i.getStringExtra("last"));
        dataId=i.getStringExtra("id");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.save){
            final Calendar calendar = Calendar.getInstance();
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int hour = calendar.get(Calendar.HOUR);
            final int min = calendar.get(Calendar.MINUTE);
            final int sec = calendar.get(Calendar.SECOND);
            String date = year + "年" + (month+1) + "月" + day + "日 "+(hour+1)+"時"+min+"分"+sec+"秒";
            values.put("lastDate","最終更新日:"+date);
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
