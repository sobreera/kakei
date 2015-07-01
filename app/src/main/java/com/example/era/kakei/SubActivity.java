package com.example.era.kakei;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class SubActivity extends ActionBarActivity {
    TextView moku;
    EditText year;
    EditText month;
    Button button;
    TextView sum;
    private ContentValues values;
    private Database helper;
    private SQLiteDatabase db;
    private Cursor c;
    int sumPrice=0;
    int yosan=10000;
    String searchWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        moku=(TextView)findViewById(R.id.moku);
        year=(EditText)findViewById(R.id.year);
        month=(EditText)findViewById(R.id.month);
        button=(Button)findViewById(R.id.button);
        sum=(TextView)findViewById(R.id.sum);
        values=new ContentValues();
        helper=new Database(getApplicationContext());
        db=helper.getWritableDatabase();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortSum();
                sum.setText(year.getText().toString()+"年"+month.getText().toString()+"月の使用金額:"+sumPrice);
                onResume();
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        moku.setText("残り金額:"+(yosan-sumPrice));
    }

    public void sortSum(){
        String sql = "select * from myData where date like '%' || ? || '%' escape '$'";
        searchWord = year.getText().toString()+"-"+month.getText().toString();
        c=db.rawQuery(sql,new String[]{searchWord});
        sumPrice=0;
        while(c.moveToNext()){
            int getPrice = c.getInt(c.getColumnIndex(Database.PRICE));
            sumPrice = sumPrice+getPrice;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub, menu);
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
