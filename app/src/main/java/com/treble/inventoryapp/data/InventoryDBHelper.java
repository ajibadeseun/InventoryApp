package com.treble.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.treble.inventoryapp.data.InventoryContract.InventEntry;

/**
 * Created by DAMILOLA on 2/8/2018.
 */

public class InventoryDBHelper extends SQLiteOpenHelper {
    public static  final int DATABASE_VERSION  = 1;
    public  static  final String DATABASE_NAME = " stock.db ";
    private static final String TEXT_TYPE = " TEXT NOT NULL ";
    private static  final String COMMA_SEP = ",";
    private static  final String BLOB_TYPE = " BLOB ";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "+
            InventEntry.TABLE_NAME+"("+ InventEntry._ID+" INTEGER PRIMARY KEY "+COMMA_SEP
            + InventEntry.COLUMN_PRODUCT_NAME + TEXT_TYPE +COMMA_SEP+
            InventEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL " +COMMA_SEP+
            InventEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL " +COMMA_SEP+
            InventEntry.COLUMN_PRODUCT_PICS+ BLOB_TYPE + COMMA_SEP +
            InventEntry.COLUMN_PRODUCT_SUPPLIER + TEXT_TYPE+")";

    private static final String SQL_DELETE_ENTRIES = " DROP TABLE IF EXISTS " + InventEntry.TABLE_NAME;

    public InventoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion,newVersion);
    }
}
