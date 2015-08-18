package com.peryisa.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by igiagante on 18/8/15.
 */
public class ImageActivity extends Activity {

    public static final String IMAGE_PATH = "IMAGE_PATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_full_size);

        Intent intent = getIntent();
        if(intent != null){
            String path = intent.getStringExtra(IMAGE_PATH);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ImageView imageView = (ImageView) findViewById(R.id.image_full_size);
            imageView.setImageBitmap(bitmap);
        }
    }
}
