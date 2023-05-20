package com.loliwe.lfmapapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DbManager extends SQLiteOpenHelper {

    Context context;
    private static final String dbname = "AllItems.db";
    private static final String tablename = "tbl_all";

    public DbManager(Context context) {
        super(context, dbname, null, 1);
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String qry = "create table " + tablename + "(id integer primary key autoincrement, type text, name text, phone text, description text, date text, location text, latLng text)";
        db.execSQL(qry);
    }

    public boolean addRecord(String p1, String p2, String p3, String p4, String p5, String p6, String p7) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("type",p1);
        cv.put("name",p2);
        cv.put("phone",p3);
        cv.put("description",p4);
        cv.put("date",p5);
        cv.put("location",p6);
        cv.put("latLng",p7);
        //cv.put("lng",p8);

        long res = db.insert("tbl_all", null, cv);
        //db.close();

        if(res == -1) return false;
        else
            return true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tablename);
        onCreate(db);
    }

    public Cursor viewData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from tbl_all";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public int deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete("tbl_all", "id = ?", new String[]{id});
    }

    public void updateData(String p0, String p1, String p4) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("type",p0);
        cv.put("type",p1);
        cv.put("description",p4);

        db.update(tablename, cv, "id = ?", null);
    }
}
