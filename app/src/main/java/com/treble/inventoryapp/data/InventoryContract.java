package com.treble.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by DAMILOLA on 2/8/2018.
 */

public final class InventoryContract {
    public static final String CONTENT_AUTHORITY = "com.treble.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY = "inventory";
    private InventoryContract(){}

    public static  abstract class InventEntry implements BaseColumns{

        public static String _ID = BaseColumns._ID;

        public static String TABLE_NAME = "inventory";
        public static String COLUMN_PRODUCT_NAME = "name";
        public static String COLUMN_PRODUCT_PRICE = "price";
        public static String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static  String COLUMN_PRODUCT_SUPPLIER = "supplier";
        public static  String COLUMN_PRODUCT_PICS = "picture";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_INVENTORY);
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of products.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single product.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

    }
}
