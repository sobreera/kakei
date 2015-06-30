package com.example.era.kakei;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class AccountList extends ActionBarActivity {

    private SQLiteDatabase db;
    private Database helper;

    private ArrayList<String> arr;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private Cursor c;
    private boolean sort = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);

        setTitle("リスト");

        listView=(ListView)findViewById(R.id.listView);
        helper=new Database(getApplicationContext());
        db=helper.getWritableDatabase();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                c.moveToPosition(position);
                Intent i = new Intent(AccountList.this,DataActivity.class);
                i.putExtra("date",c.getString(c.getColumnIndex("date")));
                i.putExtra("category",c.getString(c.getColumnIndex("category")));
                i.putExtra("price",c.getString(c.getColumnIndex("price")));
                i.putExtra("memo",c.getString(c.getColumnIndex("memo")));
                startActivity(i);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                c.moveToPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountList.this);
                builder.setTitle("データの削除");
                builder.setMessage("このデータを削除してもよろしいですか");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.delete(
                                Database.TABLE,
                                Database.ID + " = ?",
                                new String[]{c.getString(c.getColumnIndex(Database.ID))}
                        );
                        setAdapter();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if(!sort) {
            setAdapter();
        }
    }

    public void setAdapter(){
        try {
            arr = new ArrayList<>();
            c = db.rawQuery("select * from " + Database.TABLE,null);
            while(c.moveToNext()){
                String dbDate = c.getString(c.getColumnIndex(Database.DATE));
                String dbCategory = c.getString(c.getColumnIndex(Database.CATEGORY));
                int dbPrice = c.getInt(c.getColumnIndex(Database.PRICE));
                arr.add(dbDate+":"+dbCategory + ":" + dbPrice+"円");
            }
            adapter = new ArrayAdapter<>(
                    getApplicationContext(),
                    R.layout.list_row,
                    arr
            );

        } catch(SQLiteException se){
            Log.e(getClass().getSimpleName(),"Could not create or Open the database");
        }

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.sort){
            sort = true;
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
