package com.lz233.wristvibrator;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;

import androidx.annotation.Nullable;

import com.lz233.wristvibrator.utils.VibratorUtil;

import java.util.ResourceBundle;

public class VibratorService extends Service {
    private Binder binder;
    private VibratorUtil vibratorUtil;
    public enum Control {
        START,STOP
    }
    @Override
    public void onCreate(){
        if(vibratorUtil==null){
            vibratorUtil = new VibratorUtil((Vibrator)getSystemService(Service.VIBRATOR_SERVICE));
        }
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        Control control = (Control) bundle.getSerializable("Key");
        if(control==Control.START){
            vibratorUtil.vibrate(1);
        }else {
            vibratorUtil.stopVibrate();
        }
        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
