package com.treble.inventoryapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by DAMILOLA on 2/12/2018.
 */

public class ViewPhoto extends DialogFragment {

    private Bitmap bmp;
    private String productName;
    private ImageView photoView;
    private TextView photoText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.full_photo_view,container,false);
        photoView = view.findViewById(R.id.full_view);
        photoText = view.findViewById(R.id.product_photo_text);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle i = getArguments();
        byte[] pics = i.getByteArray("byteArray");
        productName = i.getString("productName");
        bmp = BitmapFactory.decodeByteArray(pics,0,pics.length);
        photoView.setImageBitmap(bmp);
        photoText.setText(productName);

    }

    public void onResume(){
        super.onResume();
        Window window = getDialog().getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = size.x;
        int height = size.y ;

        window.setLayout((int)  (0.8*width), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setLayout((int) (0.5*height),WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.TOP);

    }
}
