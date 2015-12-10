package com.example.era.kakei;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private SQLiteDatabase db;
    private Database helper;

    private ArrayList<String> arr;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private Cursor c;

    private boolean sort = false;
    TextView textView, textView2;
    String searchWord;
    int getYosan;
    String sql;
    static int sum = 0;


    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new Database(getActivity());
        db = helper.getWritableDatabase();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        final String Date = bundle.getString("date");

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        textView=(TextView)view.findViewById(R.id.textView);
        textView2=(TextView)view.findViewById(R.id.textView2);
        listView=(ListView)view.findViewById(R.id.list_item);

        setGetYosan();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                c.moveToPosition(position);
                Intent i = new Intent(getActivity(), DataActivity.class);
                i.putExtra("date", c.getString(c.getColumnIndex(Database.DATE)));
                i.putExtra("category", c.getString(c.getColumnIndex(Database.CATEGORY)));
                i.putExtra("price", c.getString(c.getColumnIndex(Database.PRICE)));
                i.putExtra("memo", c.getString(c.getColumnIndex(Database.MEMO)));
                i.putExtra("id", c.getString(c.getColumnIndex(Database.ID)));
                i.putExtra("last", c.getString(c.getColumnIndex(Database.LASTDATE)));
                startActivity(i);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                c.moveToPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("データの削除");
                builder.setMessage("このデータを削除してもよろしいですか");
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.delete(
                                Database.TABLE,
                                Database.ID + " = ?",
                                new String[]{c.getString(c.getColumnIndex(Database.ID))}
                        );
                        setAdapter(Date);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                return true;
            }
        });


        return view;
    }

    public void onResume(){
        super.onResume();
        Bundle bundle = getArguments();
        final String Date = bundle.getString("date");
        setAdapter(Date);
        setGetYosan();
    }

    public void setGetYosan(){
        Bundle bundle = getArguments();
        final String Date = bundle.getString("date");
        int newYosan = bundle.getInt("newYosan");

        sql = "select * from myData where date like '%' || ? || '%' escape '$' order by date desc limit 1";
        searchWord = Date;
        c = db.rawQuery(sql, new String[]{searchWord});
        while (c.moveToNext()) {
            getYosan = c.getInt(c.getColumnIndex(Database.YOSAN));
        }

        String[] nowSplit = Date.split("-", 0);
        int nowMon = Integer.parseInt(nowSplit[1]);
        nowSplit[1] = String.valueOf(nowMon);

        String getMonth = getNowMonth();
        getMonth = getMonth.replace("-","0");
        int nowInt = Integer.parseInt(getMonth);
        String[] thisSplit = Date.split("-",0);
        String thisMonth = thisSplit[0]+"0"+thisSplit[1];
        int thisInt = Integer.parseInt(thisMonth);
        Log.d(null,"nowInt:"+nowInt+" thisInt:"+thisInt);

        if(getYosan==0 && nowInt<thisInt){
            getYosan=newYosan;
        }else if(getYosan==0 && nowInt==thisInt){
            SharedPreferences data = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
            getYosan=Integer.parseInt(data.getString("old_yosan","10000"));
        }

        textView.setText(nowSplit[1] + "月の予算額:" + getYosan + "円");

        sum = 0;
        setAdapter(Date);
        Log.d(null, "sum2:" + sum);

        String yosan = (getYosan - sum == 0)?"0":String.valueOf(getYosan-sum); //TODO 要検証:0円ジャストになると何故かデフォルトテキスト

        if(getMonth.equals(Date)){
            textView2.setText("今月の残り予算" + yosan + "円");
        }else if(nowInt<thisInt){
            if((getYosan-sum)>0) {
                textView2.setText("貯金予定額" + yosan + "円");
            }else if((getYosan-sum)<0){
                String tex = String.format(getText(R.string.result).toString(),((getYosan-sum)*-1));
                textView2.setText(Html.fromHtml(tex));
            }
        }else{
            if((getYosan-sum)>0) {
                textView2.setText("貯金可能額" + yosan + "円");
            }else if((getYosan-sum)<0){
                String tex = String.format(getText(R.string.result).toString(),((getYosan-sum)*-1));
                textView2.setText(Html.fromHtml(tex));
            }
        }
    }

    public static String getNowMonth(){
        final DateFormat df = new SimpleDateFormat("yyyy-MM");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    public void setAdapter(String _date) {
        arr = new ArrayList<>();
        String[] nowSplit = _date.split("-", 0);
        int nowMon = Integer.parseInt(nowSplit[1]);
        int nowDay = Integer.parseInt(nowSplit[2]);
        nowSplit[1] = String.valueOf(nowMon);
        nowSplit[2] = String.valueOf(nowDay);
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
                getActivity(),
                R.layout.custom_text_list_item,
                arr
        );

        listView.setAdapter(adapter);
    }


}
