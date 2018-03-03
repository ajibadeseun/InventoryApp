package com.treble.inventoryapp;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.treble.inventoryapp.data.InventoryContract.InventEntry;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri uri;
    private ImageView productPic;
    private EditText productName;
    private EditText productPrice;
    private EditText supplierName;
    private Button decreaseBtn;
    private TextView qtnDisplay;
    private Button increaseBtn;
    private Button orderBtn;
    private Button deleteBtn;
    private static final int EXISTING_INVENTORY_LOADER = 0;
    private boolean mInventHasChanged = false;
    int quantity = 1;
    private static final int READ_REQUEST_CODE = 42;



// OnTouchListener that listens for any user touches on a View, implying that they are modifying
// the view, and we change the mPetHasChanged boolean to true.

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mInventHasChanged = true;
            return false;
        }
    };
    private Uri uri1;
    private byte[] compressedData;
    private byte[] dataOne;
    private TextView changePics;
    private byte[] pics;
    private String name;
    private String price;
    private int priceInt;
    private String supplier;
    private int quantityInt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        uri = getIntent().getData();
        Log.v("EditorActivity", "Uri: " + uri);

        productPic = (ImageView) findViewById(R.id.image_view);
        productName = (EditText) findViewById(R.id.product_name);
        productPrice = (EditText) findViewById(R.id.product_price);
        supplierName = (EditText) findViewById(R.id.supplier_name);
        decreaseBtn = (Button) findViewById(R.id.decrease_btn);
        qtnDisplay = (TextView) findViewById(R.id.quantity_text_view);
        increaseBtn = (Button) findViewById(R.id.increase_btn);
        orderBtn = (Button) findViewById(R.id.order_btn);
        deleteBtn = (Button) findViewById(R.id.delete_btn);
        changePics = (TextView) findViewById(R.id.click_image);


        if (uri == null) {
            setTitle("Add a product");

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a pet that hasn't been created yet.)
            invalidateOptionsMenu();


        } else {
            setTitle("Edit product");
            changePics.setText(getString(R.string.edit_photo_text));
            getSupportLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }



        productPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);

                fileintent.addCategory(Intent.CATEGORY_OPENABLE);
                fileintent.setType("image/*");


                try {

                    startActivityForResult(fileintent, READ_REQUEST_CODE);


                } catch (ActivityNotFoundException e) {
                    Log.e("tag", "No activity can handle picking a file. Showing alternatives.");
                }

            }
        });

        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity == 100) {
                    Toast.makeText(EditorActivity.this, "you cannot have more than 100 products", Toast.LENGTH_SHORT).show();

                }
                quantity += 1;
                qtnDisplay.setText(String.valueOf(quantity));

            }
        });

        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantity > 1) {
                    quantity -= 1;
                    qtnDisplay.setText(String.valueOf(quantity));
                } else {
                    Toast.makeText(EditorActivity.this, "You cannot have less than" +
                            " 1 product", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

public void saveProduct(){
    name = productName.getText().toString().trim();
    price = productPrice.getText().toString().trim();
    priceInt = 0;
        if(!TextUtils.isEmpty(price)){
             priceInt = Integer.parseInt(price);
        }

    supplier = supplierName.getText().toString().trim();
    quantityInt = quantity;
        byte [] picsOne ;
        if(uri == null){
            picsOne = dataOne;
        }
        else {
            if(dataOne == null){
                picsOne = pics;
            }
            else{
                picsOne = dataOne;
            }

        }

    ContentValues values = new ContentValues();
    values.put(InventEntry.COLUMN_PRODUCT_NAME, name);
    values.put(InventEntry.COLUMN_PRODUCT_PRICE, priceInt);
    values.put(InventEntry.COLUMN_PRODUCT_QUANTITY, quantityInt);
    values.put(InventEntry.COLUMN_PRODUCT_SUPPLIER, supplier);
    values.put(InventEntry.COLUMN_PRODUCT_PICS,picsOne);


    if(uri == null){



        try{

            if (TextUtils.isEmpty(name) && TextUtils.isEmpty(price)
                    && TextUtils.isEmpty(supplier) && pics == null) {

                Toast.makeText(EditorActivity.this, "Product not saved", Toast.LENGTH_LONG).show();
                return;
            } else {
                Uri mNewUri = getContentResolver().insert(InventEntry.CONTENT_URI, values);
                Toast.makeText(EditorActivity.this, "Pet saved", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Toast.makeText(EditorActivity.this,"Error: "+e,Toast.LENGTH_LONG).show();
            Log.v("Error","Error crash "+e);
        }


    }
    else{


        int rowsAffected = getContentResolver().update(uri, values, null, null);

        // Show a toast message depending on whether or not the update was successful.
        if (rowsAffected == 0) {
            // If no rows were affected, then there was an error with the update.
            Toast.makeText(this,"Error with updating product" ,
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the update was successful and we can display a toast.
            Toast.makeText(this,"Product updated",
                    Toast.LENGTH_SHORT).show();
        }

    }

}



    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver()
                .openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (requestCode) {
            case READ_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        uri1 = data.getData();

                        Bitmap bitmap = getBitmapFromUri(uri1);

                        ByteArrayOutputStream one = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, one);

                            dataOne = one.toByteArray();









//                        Log.v("EditorActivity", "Original byte "+convertToLong(dataOne)
//                               +" size: "+dataOne.length);
//                        Deflater compressor = new Deflater();
//                        compressor.setLevel(Deflater.BEST_COMPRESSION);
//                        compressor.setInput(dataOne);
//                        compressor.finish();
//                        ByteArrayOutputStream two = new ByteArrayOutputStream(dataOne.length);
//                        byte[] buff = new byte[1024];
//                        while (!compressor.finished()) {
//                            int count = compressor.deflate(buff);
//                            two.write(buff, 0, count);
//                        }
//                        try {
//                            two.close();
//                        } catch (Exception e) {
//                            Toast.makeText(EditorActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
//                        }

//                        compressedData = two.toByteArray();



//                        Log.v("EditorActivity", "Compressed byte "
//                               +convertToLong(compressedData)+" size: "
//                                +bytesIntoHumanReadable(convertToLong(compressedData)));
                    } catch (Exception e) {
                        Toast.makeText(EditorActivity.this, "Error "
                                + e, Toast.LENGTH_LONG)
                                .show();
                    }


                }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveProduct();
                finish();
                return true;

            case R.id.home:
                 return true;
            case R.id.delete_product:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InventEntry._ID,
                InventEntry.COLUMN_PRODUCT_NAME,
                InventEntry.COLUMN_PRODUCT_PRICE,
                InventEntry.COLUMN_PRODUCT_QUANTITY,
                InventEntry.COLUMN_PRODUCT_SUPPLIER,
                InventEntry.COLUMN_PRODUCT_PICS
        };
        return new CursorLoader(this, uri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.moveToFirst()) {
            int nameColumnIndex = data.getColumnIndex(InventEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = data.getColumnIndex(InventEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = data.getColumnIndex(InventEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierColumnIndex = data.getColumnIndex(InventEntry.COLUMN_PRODUCT_SUPPLIER);
            int photoColumnIndex = data.getColumnIndex(InventEntry.COLUMN_PRODUCT_PICS);


            String name = data.getString(nameColumnIndex);
            String price = String.valueOf(data.getInt(priceColumnIndex));
            String quantity = String.valueOf(data.getInt(quantityColumnIndex));
            String supplier = data.getString(supplierColumnIndex);
            pics = data.getBlob(photoColumnIndex);
            Bitmap bmp = BitmapFactory.decodeByteArray(pics,0, pics.length);

            productName.setText(name);
            productPrice.setText(price);
            qtnDisplay.setText(quantity);
            supplierName.setText(supplier);
            productPic.setImageBitmap(bmp);




        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader.reset();

    }
}
