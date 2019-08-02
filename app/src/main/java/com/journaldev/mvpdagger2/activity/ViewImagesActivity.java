package com.journaldev.mvpdagger2.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.journaldev.mvpdagger2.Data.ImageUrls;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.adapters.ImagesPageAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewImagesActivity extends AppCompatActivity {

    @BindView(R.id.pager)
    ViewPager pager;
    ImagesPageAdapter mCustomPagerAdapter = null;
    @BindView(R.id.deleteImage)
    Button deleteImage;
    LinkedList<Uri> uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("");
        ColorDrawable abDrawable = new ColorDrawable(getResources().getColor(R.color.gray));
        abDrawable.setAlpha(0);
        getSupportActionBar().setBackgroundDrawable(abDrawable);
        super.onCreate(savedInstanceState);
        allScreen();
        setContentView(R.layout.viewimages);
        ButterKnife.bind(this);
        pager.setOffscreenPageLimit(3);
        uri = getIntenlAllUri();
        getOtherIntent();
        mCustomPagerAdapter = new ImagesPageAdapter(this, uri);
        pager.setAdapter(mCustomPagerAdapter);
    }


    private LinkedList<Uri> getIntenlAllUri() {
        LinkedList<Uri> uri = new LinkedList<>();
        ArrayList<String> strUri = getIntent().getStringArrayListExtra("uri");
        if (strUri != null) {
            for (int i = 0; i < strUri.size(); i++)
                uri.add(i, Uri.parse(strUri.get(i)));
            mCustomPagerAdapter = new ImagesPageAdapter(this, uri);
        } else
            uri = ImageUrls.getUrls(getApplicationContext());
        return uri;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.file_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.info:
                Intent intent = new Intent(getApplicationContext(), FIleInfo.class);
                Uri currentUri = mCustomPagerAdapter.getCurrentUri(pager.getCurrentItem());
                intent.putExtra("uri", currentUri.toString());
                getApplicationContext().startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getOtherIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_VIEW.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                selectCurrentItemFromIntent(intent);
            }
        } else
            selectImage();
    }

    private void selectCurrentItemFromIntent(Intent intent) {
        String intentStr = intent.toString();

        final String intentFileName = getFileNameFromPath(intentStr);
        final String[] allFileName = getAllPath();

        int currentItem = getCurrentItem(intentFileName, allFileName);
        selectImageFromId(currentItem);
    }

    private String[] getAllPath() {
        String[] allFileName = new String[uri.size()];

        for (int i = 0; i < uri.size(); i++) {
            allFileName[i] = getFileNameFromPath(uri.get(i).toString());
        }

        return allFileName;
    }

    private int getCurrentItem(String intentFileName, String[] allFileName) {
        for (int i = 0; i < allFileName.length; i++) {
            if (allFileName[i].equals(intentFileName)) {
                return i;
            }
        }
        return -1;
    }

    private String getFileNameFromPath(String str) {

        String[] fileStr = getPath(str).split("/");
        String fileName = fileStr[fileStr.length - 1];
        return fileName;
    }

    private String getPath(String str) {
        String uriStr;
        try {
            uriStr = str.substring(getStartPath(str), getEndPath(str));
        } catch (RuntimeException ex) {
            uriStr = str;
        }
        return uriStr;
    }

    private int getStartPath(String str) {
        String startStr = " dat=";
        int start = str.indexOf(startStr) + startStr.length();
        return start;
    }

    private int getEndPath(String str) {
        String endStr = " typ=";
        int end = str.indexOf(endStr);
        return end;
    }


    private void allScreen() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }


    private void selectImage() {
        final int newStartImageId = getIntent().getIntExtra("idImage", -1);
        selectImageFromId(newStartImageId);
    }

    private void selectImageFromId(final int id) {
        pager.post(new Runnable() {
            @Override
            public void run() {
                pager.setCurrentItem(id, false);
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    @OnClick(R.id.deleteImage)
    public void clickDeleteImage() {
        AlertDialog.Builder dialog = createAlertDialogDeleteImage();
        dialog.show();
    }

    private AlertDialog.Builder createAlertDialogDeleteImage() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setMessage("Вы действительно хотите удалить изображение?");
        ad.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                ContentResolver contentResolver = getContentResolver();
                contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Images.ImageColumns.DATA + "=?", new String[]{uri.get(pager.getCurrentItem()).toString()});
                ImageUrls.isUpdate = true;
                int currentPosition = pager.getCurrentItem();
                uri.remove(currentPosition);
                mCustomPagerAdapter = new ImagesPageAdapter(getApplicationContext(), uri);
                pager.setAdapter(mCustomPagerAdapter);
                pager.invalidate();
                pager.setCurrentItem(currentPosition);
            }
        });
        ad.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
        return ad;
    }
}