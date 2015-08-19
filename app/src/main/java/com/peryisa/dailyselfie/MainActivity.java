package com.peryisa.dailyselfie;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.Item;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemSelectedListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int ITEM_ID_DELETE = 1;
    static final int height = 80;
    static final int width = 80;

    String mCurrentPhotoPath;
    private boolean selectAllButtonPressed = false;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Menu menu;

    private PendingIntent pendingIntent;
    private static final long REPEAT_TIME = 1000 * 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(this, this);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        initListImages();

        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(this, DailySelfieReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        start();
    }

    private void start() {

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);

        // Fetch every 30 seconds
        // InexactRepeating allows Android to optimize the energy consumption
        manager.setRepeating(AlarmManager.RTC, cal.getTimeInMillis(), REPEAT_TIME, pendingIntent);
    }

    @Override
    public void itemSelected(Item item) {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra(ImageActivity.IMAGE_PATH, item.getPath());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;

        if(selectAllButtonPressed){
            MenuItem deleteIcon = this.menu.add(0, ITEM_ID_DELETE, 0, "Delete");
            deleteIcon.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            deleteIcon.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
            deleteIcon.setIcon(R.drawable.delete);
        }

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.camera){
            startCamera();
            return true;
        }

        if(item.getItemId() == R.id.select_all){

            if(!selectAllButtonPressed){
                selectAll();
                selectAllButtonPressed = true;

            }else{
                unSelectAll();
                selectAllButtonPressed = false;
            }
            setDeleteIcon();

            return true;
        }

        if(item.getItemId() == ITEM_ID_DELETE){
            mAdapter.removeAllViews();
            this.menu.removeItem(ITEM_ID_DELETE);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDeleteIcon(){

        if(selectAllButtonPressed){
            MenuItem deleteIcon = this.menu.add(0,1,0,"Delete");
            deleteIcon.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            deleteIcon.setIcon(R.drawable.delete);
        }else{
            this.menu.removeItem(ITEM_ID_DELETE);
        }
    }

    private void selectAll(){
        for(int i = 0; i < mRecyclerView.getChildCount(); i++){
            View view = mRecyclerView.getChildAt(i);
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.select_picture);
            checkBox.setChecked(true);
        }
    }

    private void unSelectAll(){
        for(int i = 0; i < mRecyclerView.getChildCount(); i++){
            View view = mRecyclerView.getChildAt(i);
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.select_picture);
            checkBox.setChecked(false);
        }
    }

    private void startCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            saveThumbNail();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void saveThumbNail(){
        File photoFile = new File(mCurrentPhotoPath);
        Item item = new Item();
        item.setName(photoFile.getName());
        item.setImage(getScaledBitmap(mCurrentPhotoPath));
        mAdapter.add(item);
    }

    private Bitmap getScaledBitmap(String path){
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if(bitmap != null){
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        }
        return bitmap;
    }

    private void initListImages(){

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        for(File file : directory.listFiles()){

            Bitmap scaledImage = getScaledBitmap(file.getAbsolutePath());

            if(scaledImage != null){
                Item item = new Item();
                item.setName(file.getName());
                item.setImage(scaledImage);
                item.setPath(file.getAbsolutePath());
                mAdapter.add(item);
            }
        }
    }

}
