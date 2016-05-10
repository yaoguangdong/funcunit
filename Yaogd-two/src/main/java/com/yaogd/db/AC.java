package com.yaogd.db;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.yaogd.db.BooksDB.DataColumns;

public class AC extends Activity {
    private BooksDB mBooksDB;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
        mBooksDB = new BooksDB(this);
        // 入力するデータの生成
        ContentValues values = new ContentValues();
        values.put(DataColumns.TITLE, "C# 2008 Programmer's Reference");
        values.put(DataColumns.ISBN, "0470285818");
        // 追加
        long id = mBooksDB.insert(values);
        // idを指定して削除
        mBooksDB.delete(id, null, null);

        // 入力するデータの生成
        values = new ContentValues();
        values.put(DataColumns.TITLE, "android hacks");
        values.put(DataColumns.ISBN, "0000000");
        // 追加
        id = mBooksDB.insert(values);

        // id指定で修正
        values = new ContentValues();
        values.put(DataColumns.TITLE, "droid hacks");
        values.put(DataColumns.ISBN, "0100000");
        mBooksDB.update(id, values, null, null);

        // クエリーを直接発行
        // 内容をすべてトーストで表示
        Cursor c = mBooksDB.query(null, null, null, "title desc");
        if (c.moveToFirst()) {
            do {
                Toast.makeText(
                        this,
                        c.getString(c.getColumnIndex(DataColumns._ID))
                                + ", "
                                + c.getString(c
                                        .getColumnIndex(DataColumns.TITLE))
                                + ", "
                                + c.getString(c
                                        .getColumnIndex(DataColumns.ISBN)),
                        Toast.LENGTH_LONG).show();
            } while (c.moveToNext());
        }
    }
}