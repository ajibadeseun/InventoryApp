package com.treble.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.treble.inventoryapp.data.InventoryContract.InventEntry;

/**
 * Created by DAMILOLA on 2/8/2018.
 */

public class InventoryProvider extends ContentProvider {

    private static final int INVENT = 100;
    private static final int INVENT_ID = 101;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY, INVENT);

        mUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY + "/#", INVENT_ID);
    }

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();
    private InventoryDBHelper mDBHelper;


    @Override
    public boolean onCreate() {
        mDBHelper = new InventoryDBHelper(getContext());
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDBHelper.getReadableDatabase();
        Cursor cursor = null;

        int match = mUriMatcher.match(uri);
        switch (match) {
            case INVENT:
                cursor = database.query(InventEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case INVENT_ID:
                selection = InventEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(InventEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match){
            case INVENT:
                return InventEntry.CONTENT_LIST_TYPE;
            case INVENT_ID:
                return  InventEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: "+uri+" with match "+match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case INVENT:
                return insertInvent(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }

    private Uri insertInvent(Uri uri, ContentValues values) {

        Integer quantity = values.getAsInteger(InventEntry.COLUMN_PRODUCT_QUANTITY);
        Integer price = values.getAsInteger(InventEntry.COLUMN_PRODUCT_PRICE);
        String name = values.getAsString(InventEntry.COLUMN_PRODUCT_NAME);
        byte[] pics = values.getAsByteArray(InventEntry.COLUMN_PRODUCT_PICS);
        String supplier = values.getAsString(InventEntry.COLUMN_PRODUCT_SUPPLIER);

        if (quantity == null && quantity < 0) {
            throw new IllegalArgumentException("Product quantity requires a valid amount");
        }

        if (price == null && price < 0) {
            throw new IllegalArgumentException("Product price requires a valid amount");
        }

        if (name == null) {
            throw new IllegalArgumentException("Product requires a name");
        }

        if (pics == null) {
            throw new IllegalArgumentException("Product requires a photo");
        }

        if(supplier == null){
            throw new IllegalArgumentException("Product requires a supplier name");
        }



        SQLiteDatabase mBase = mDBHelper.getWritableDatabase();

        long rowId = mBase.insert(InventEntry.TABLE_NAME, null, values);

        if (rowId == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            Toast.makeText(getContext(), "Failed to insert row for " + uri, Toast.LENGTH_LONG).show();
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, rowId);

    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDBHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);
        int deletedRows;

        switch (match) {
            case INVENT:
                deletedRows = database.delete(InventEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case INVENT_ID:
                selection = InventEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                deletedRows = database.delete(InventEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);

        }
        if (deletedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case INVENT:
                return updateInvent(uri, values, selection, selectionArgs);
            case INVENT_ID:
                selection = InventEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateInvent(uri, values, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }

    private int updateInvent(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(InventEntry.COLUMN_PRODUCT_QUANTITY)) {
            Integer quantity = values.getAsInteger(InventEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantity == null) {
                throw new IllegalArgumentException("Product quantity requires a valid amount");
            }

        }

        if(values.containsKey(InventEntry.COLUMN_PRODUCT_PRICE)){
            Integer price = values.getAsInteger(InventEntry.COLUMN_PRODUCT_PRICE);
            if(price == null && price < 0){
               throw new IllegalArgumentException("Product price requires a valid amount");
            }
        }

        if(values.containsKey(InventEntry.COLUMN_PRODUCT_NAME)){
            String name = values.getAsString(InventEntry.COLUMN_PRODUCT_NAME);
            if(name == null){
                throw new IllegalArgumentException("Product requires a name");
            }
        }

        if(values.containsKey(InventEntry.COLUMN_PRODUCT_PICS)){
            byte[] pics = values.getAsByteArray(InventEntry.COLUMN_PRODUCT_PICS);
            if(pics == null){
                throw new IllegalArgumentException("Product requires a photo");
            }
        }

        if(values.containsKey(InventEntry.COLUMN_PRODUCT_SUPPLIER)){
            String supplier = values.getAsString(InventEntry.COLUMN_PRODUCT_SUPPLIER);
            if(supplier == null){
                throw new IllegalArgumentException("Product requires a supplier name");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDBHelper.getWritableDatabase();

        int rowsUpdated = database.update(InventEntry.TABLE_NAME, values, selection, selectionArgs);
        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        // Returns the number of database rows affected by the update statement
        return rowsUpdated;


    }
}
