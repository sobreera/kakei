package com.example.era.kakei;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by era on 15/06/24.
 */
public class Database extends SQLiteOpenHelper {
    private static final String DB_NAME="kakeibo.db";
    private static final int DB_VERSION=2;
    public static String TABLE="myData";
    public static String ID="_id";
    public static String CATEGORY="category";
    public static String PRICE="price";
    public static String MEMO="memo";
    public static String DATE="date";
    public static String LASTDATE="lastDate";

    public Database(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(
                "create table " + TABLE + "("
                        + ID + " integer primary key,"
                        + CATEGORY + " text,"
                        + PRICE + " integer,"
                        + DATE + " text,"
                        + LASTDATE +" text,"
                        + MEMO + " text);"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("drop table if exists "+TABLE);
        onCreate(db);
    }
}
