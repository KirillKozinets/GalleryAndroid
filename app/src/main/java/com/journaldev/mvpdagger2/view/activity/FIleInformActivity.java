package com.journaldev.mvpdagger2.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.journaldev.mvpdagger2.application.App;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.view.utils.OnSwipeTouchListener;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import butterknife.BindView;
import butterknife.ButterKnife;


public class FIleInformActivity extends BaseThemeActivity {

    @BindView(R.id.path)
    TextView path;
    @BindView(R.id.change)
    TextView chandge;
    @BindView(R.id.resolution)
    TextView resolution;
    @BindView(R.id.size)
    TextView size;
    @BindView(R.id.root)
    LinearLayout root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.file_info);
        ButterKnife.bind(this);
        Uri uriFile = getUri();
        if(uriFile != null) {
            viewFileInfo(uriFile);
        }
        setOnSwipeTouchListener(root);
    }

    private void setOnSwipeTouchListener(View view) {
        view.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                onBackPressed();
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void viewFileInfo(Uri file) {
        File FileByUri = new File(String.valueOf(file));
        Bitmap bmp = BitmapFactory.decodeFile(FileByUri.getPath());
        if (!FileByUri.exists()) return;
        path.setText(file.getPath());
        Calendar date = new GregorianCalendar();
        date.setTime(new Date(FileByUri.lastModified()));
        chandge.setText(date.get(
                Calendar.HOUR_OF_DAY) + " : "
                + date.get(Calendar.MINUTE) + " , "
                + date.get(Calendar.DAY_OF_MONTH) + "."
                + (date.get(Calendar.MONTH) + 1) + "."
                + date.get(Calendar.YEAR));

        resolution.setText(bmp.getWidth() + "×" + bmp.getHeight());
        size.setText(convertFileWeightToString(FileByUri.length()));
    }

    private String convertFileWeightToString(double fileWeight) {
        int prefixLength = 1024;
        int prefixId = 0;
        String[] prefix = new String[]{this.getString(R.string.bytes),
                this.getString(R.string.kilobytes),this.getString(R.string.megabytes)};

        while (fileWeight / prefixLength >= 1) {
            prefixId++;
            fileWeight /= prefixLength;
        }

        double roundFileWeight = new BigDecimal(fileWeight).setScale(2, RoundingMode.UP).doubleValue();
        String resultWeight = roundFileWeight + " " + prefix[prefixId];
        return resultWeight;
    }

    private Uri getUri() {
        Intent intent = getIntent();
        String uri = intent.getStringExtra("uri");
        if (uri != null)
            return Uri.parse(uri);
        else
            return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (App.getAppPreference().getIsAnim())
            overridePendingTransition(R.anim.back2, R.anim.next2);
    }

    @Override
    int getDarkTheme() {
        return R.style.DarkTheme;
    }

    @Override
    int getLightTheme() {
        return R.style.LightTheme;
    }
}
