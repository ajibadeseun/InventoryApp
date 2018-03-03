package com.treble.inventoryapp;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.treble.inventoryapp.data.InventoryContract.InventEntry;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by DAMILOLA on 2/9/2018.
 */

public class InventoryCursorAdapter extends CursorAdapter  {
       private Context mContext;
       private Cursor mCursor;

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        this.mContext = context;
        this.mCursor = c;
    }

    @Override
    public View newView(Context context,  Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_item,null);
        }
        convertView.setTag(position);
        return super.getView(position, convertView, parent);
    }

    @Override
    public void bindView(View view,  Context context, Cursor cursor) {


        LinearLayout layout = view.findViewById(R.id.list_item_layout);
        TextView nameText = view.findViewById(R.id.name);
        TextView quantityText = view.findViewById(R.id.quantity);
        TextView priceText = view.findViewById(R.id.price);
        Button saleBtn = view.findViewById(R.id.sale);
        CircleImageView photo = view.findViewById(R.id.photo);



        final String name = cursor.getString(cursor.getColumnIndexOrThrow(InventEntry.COLUMN_PRODUCT_NAME));
        String quantity = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(InventEntry.COLUMN_PRODUCT_QUANTITY)));
        String price = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(InventEntry.COLUMN_PRODUCT_PRICE)));

        final byte[] pics = cursor.getBlob(cursor.getColumnIndexOrThrow(InventEntry.COLUMN_PRODUCT_PICS));
        String string = new String(pics);
        Log.v("Cursor", "byte: " + string);
        Bitmap bmp = BitmapFactory.decodeByteArray(pics, 0, pics.length);
        int photoSize = bmp.getByteCount();
        Log.v("CursorClass","Photosize: "+photoSize);
        photo.setImageBitmap(bmp);


        nameText.setText(name);
        quantityText.setText(quantity);
        priceText.setText(price);
//        layout.setTag(cursor.getPosition());

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context pContext = v.getContext();
                int position = (Integer) v.getTag();

                Log.v("Position","pos: "+position);
                Intent intent = new Intent(pContext, EditorActivity.class);
                Uri uri = ContentUris.withAppendedId(InventEntry.CONTENT_URI,  position+1);
                intent.setData(uri);
                intent.putExtra("id", position+1);
                pContext.startActivity(intent);

            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context vContext = v.getContext();
                FragmentActivity activity = (FragmentActivity)(vContext);

                FragmentManager manager = activity.getSupportFragmentManager();
                ViewPhoto viewPhoto = new ViewPhoto();
                Bundle i = new Bundle();
                i.putByteArray("byteArray",pics);
                i.putString("productName",name);
                viewPhoto.setArguments(i);
                viewPhoto.show(manager,"view_photo_frag");
            }
        });











    }
}
