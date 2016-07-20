package com.regis.gway.register;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;


    private static final String DATABASE_NAME = "dbReg";

    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DATE = "date";

    private static final String TABLE_NAME = "users_data";

    DBHandler(Context context) {
        super(context , DATABASE_NAME , null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE "+TABLE_NAME+"("+KEY_ID+" INTEGER PRIMARY KEY,"+KEY_USER_ID+" TEXT,"+KEY_NAME+" TEXT,"+KEY_ADDRESS+" TEXT,"+KEY_PHONE+" TEXT,"+KEY_DATE+" DATE)";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    void insertUser(userDataBean item)
    {
        SQLiteDatabase db = this.getWritableDatabase();


        String query = "SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_NAME;
        //Cursor c = db.rawQuery(query , new String[]{item.getName()});


        Boolean exists = DatabaseUtils.longForQuery(db ,  "select count(*) from " + TABLE_NAME + " where "+KEY_NAME+"=? limit 1" , new String[]{item.getName()})  > 0;

        if (!exists)
        {
            ContentValues cv = new ContentValues();
            cv.put(KEY_USER_ID , item.getUserId());
            cv.put(KEY_NAME , item.getName());
            cv.put(KEY_ADDRESS , item.getAddress());
            cv.put(KEY_PHONE , item.getPhone());
            cv.put(KEY_DATE , item.getDate());
            db.insert(TABLE_NAME , null , cv);
            db.close();
      }

        //c.close();


    }


    List<userDataBean> getAllusers()
    {
        List<userDataBean> list = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query , null);
        if (cursor.moveToFirst())
        {
            do {
                userDataBean item = new userDataBean();
                item.setUserId(cursor.getString(1));
                item.setName(cursor.getString(2));
                item.setAddress(cursor.getString(3));
                item.setPhone(cursor.getString(4));
                item.setDate(cursor.getString(5));
                // Adding contact to list
                list.add(item);
            } while (cursor.moveToNext());

        }

        cursor.close();
        return list;

    }

    int getUsersCount() {
        String countQuery = "SELECT * FROM " +TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        // return count
        return count;
    }

}
