package com.yaogd.contentprovider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;
/**
 * 应用程序间共享数据的方法
 * @author yaoguangdong
 * 2014-5-1
 */
public class BooksProvider extends ContentProvider {
    // プロバイダー名
    public static final String PROVIDER_NAME = "jp.co.brilliantservice.hacks.Books";
    // URI
    public static final Uri CONTENT_URI = Uri.parse("content://"
            + PROVIDER_NAME + "/books");

    private static final int BOOKS = 1;
    private static final int BOOK_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "books", BOOKS);
        uriMatcher.addURI(PROVIDER_NAME, "books/#", BOOK_ID);
    }

    /**
     * UriをのMIMEを返す. vnd.android.cursor.dir が複数レコード 
     * vnd.android.cursor.item が単一レコード
     * 
     * @see ContentProvider#getType(Uri)
     */
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
        // ---get all books---
        case BOOKS:
            return "vnd.android.cursor.dir/books ";
            // ---get a particular book---
        case BOOK_ID:
            return "vnd.android.cursor.item/books ";
        default:
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    /**
     * URIを比較して動作を変更する。
     * 
     * @see ContentProvider#delete(Uri,
     *      String, String[])
     */
    @Override
    public int delete(Uri Uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(Uri)) {
        case BOOKS:
            count = booksDB.delete(DATABASE_TABLE, selection, selectionArgs);
            break;
        case BOOK_ID:
            String id = Uri.getPathSegments().get(1);
            count = booksDB.delete(DATABASE_TABLE, DataColumns._ID
                    + " = "
                    + id
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                            + ')' : ""), selectionArgs);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + Uri);
        }
        getContext().getContentResolver().notifyChange(Uri, null);
        return count;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // ---add a new book---
        long rowID = booksDB.insert(DATABASE_TABLE, "", values);

        // ---if added successfully---
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        booksDB = dbHelper.getWritableDatabase();
        return (booksDB == null) ? false : true;
    }
    
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        Log.d("TEST", "BooksProvider:query: Uri:" + uri + projection
                + selection);
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables(DATABASE_TABLE);

        if (uriMatcher.match(uri) == BOOK_ID)
            // ---if getting a particular book---
            sqlBuilder.appendWhere(DataColumns._ID + " = "
                    + uri.getPathSegments().get(1));

        if (sortOrder == null || sortOrder == "")
            sortOrder = DataColumns.TITLE;

        Cursor c = sqlBuilder.query(booksDB, projection, selection,
                selectionArgs, null, null, sortOrder);

        // ---register to watch a content URI for changes---
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
        case BOOKS:
            count = booksDB.update(DATABASE_TABLE, values, selection,
                    selectionArgs);
            break;
        case BOOK_ID:
            count = booksDB.update(DATABASE_TABLE, values, DataColumns._ID
                    + " = "
                    + uri.getPathSegments().get(1)
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                            + ')' : ""), selectionArgs);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    
    /**
     * 通过CP对文件读写
     */
    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode)
            throws FileNotFoundException {
        URI _uri = URI.create("file:///sdcard/"+PROVIDER_NAME+"/" + uri.getPathSegments().get(1) + ".png");
        File file = new File(_uri);
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ParcelFileDescriptor parcel = ParcelFileDescriptor.open(file,
                ParcelFileDescriptor.MODE_READ_WRITE);
        return parcel;
    }
    
    private SQLiteDatabase booksDB;
    private static final String DATABASE_TABLE = "titles";

    /**
     * データーベース定義
     */
    public interface DataColumns extends BaseColumns {
        public static final String DISPLAY_NAME = "_display_name";
        public static final String TITLE = "title";
        public static final String _ID = "_id";
        public static final String ISBN = "isbn";
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        /**
         * データベース名
         */
        private static final String DATABASE_NAME = "Books";
        
        /**
         * データベースバージョン（ローカル用） インクリメントすると onUpgrade() が呼び出される
         */
        private static final int DATABASE_VERSION = 2;
        /**
         *  DB作成用string
         */
        private static final String DATABASE_CREATE = "create table "
                + DATABASE_TABLE + " (" 
                + DataColumns._ID + " integer primary key autoincrement, " 
                + DataColumns._COUNT + " integer,"
                + DataColumns.DISPLAY_NAME + " text,"
                + DataColumns.ISBN + " text not null,"
                + DataColumns.TITLE + " text not null"
                + ");" ;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Content provider database",
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }
}
