package com.example.era.kakei;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class GraphActivity extends ActionBarActivity {
    Cursor c;
    Database helper;
    SQLiteDatabase db;
    int sum;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        textView=(TextView)findViewById(R.id.textView);

        c=db.rawQuery("select * from myData where datetime(date, 'localtime') < datetime('2011-10-22', 'localtime')",null);
        while(c.moveToNext()){
            sum=c.getInt(c.getColumnIndex(Database.PRICE));
        }
        textView.setText(sum);


        //グラフ作成用にActivity作成したもののまだよくわかってないので作っただけ状態
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph, menu);
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
