package com.lz233.wristvibrator;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.util.AndroidException;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.wear.widget.BoxInsetLayout;

import com.lz233.wristvibrator.utils.VibratorUtil;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class MainActivity extends WearableActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private BoxInsetLayout mainLayout;
    private ImageView vibratorImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //fb
        mainLayout = findViewById(R.id.mainLayout);
        vibratorImageView = findViewById(R.id.vibratorImageView);
        //
        AppCenter.start(getApplication(), "b9095fc1-d43e-451b-9253-e5c19b14ec45", Analytics.class, Crashes.class);
        Analytics.trackEvent("+1");
        sharedPreferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setAmbientEnabled();
        TypedArray array = getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorBackground,});
        final int backgroundcolor = array.getColor(0, 0xFF00FF);
        if(sharedPreferences.getBoolean("isVibrate",false)){
            mainLayout.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        }else {
            mainLayout.setBackgroundColor(backgroundcolor);
        }
        //
        new Thread(new Runnable() {
            @Override
            public void run() {
                vibratorImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, VibratorService.class);
                        Bundle bundle = new Bundle();
                        if(sharedPreferences.getBoolean("isVibrate",false)){
                            bundle.putSerializable("Key", VibratorService.Control.STOP);
                            intent.putExtras(bundle);
                            startService(intent);
                            editor.putBoolean("isVibrate",false);
                            mainLayout.setBackgroundColor(backgroundcolor);
                        }else {
                            bundle.putSerializable("Key", VibratorService.Control.START);
                            intent.putExtras(bundle);
                            startService(intent);
                            editor.putBoolean("isVibrate",true);
                            mainLayout.setBackgroundColor(getColor(R.color.colorPrimaryDark));
                        }
                        editor.apply();
                    }
                });
            }
        }).start();
    }
}
