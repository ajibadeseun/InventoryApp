package com.treble.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.treble.inventoryapp.data.InventoryContract.InventEntry;
import com.treble.inventoryapp.data.InventoryDBHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private InventoryDBHelper mDbHelper;
    private InventoryCursorAdapter adapter;
    private static final int INVENTORY_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new InventoryDBHelper(this);

//        CircleImageView imageView = findViewById(R.id.photo);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager manager = getSupportFragmentManager();
//                ViewPhoto photo = new ViewPhoto();
//                Bundle i = new Bundle();
//                i.putByteArray("byteArray",);
//            }
//        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.setData(null);
                startActivity(intent);
            }
        });


        ListView listView = (ListView) findViewById(R.id.list_view_product);

        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        adapter = new InventoryCursorAdapter(this,null);
        listView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(INVENTORY_LOADER,null,this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InventEntry._ID,
                InventEntry.COLUMN_PRODUCT_NAME,
                InventEntry.COLUMN_PRODUCT_PRICE,
                InventEntry.COLUMN_PRODUCT_QUANTITY,
                InventEntry.COLUMN_PRODUCT_PICS


        };
        return new CursorLoader(MainActivity.this, InventEntry.CONTENT_URI, projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        adapter.swapCursor(null);
    }
}

