package com.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.constants.Actions;
import com.utils.Notifier;


/**
 * 音乐播放后台服务
 * Created by wcy on 2015/11/27.
 */
public class PlayService extends Service {
    private static final String TAG = "Service";

    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: " + getClass().getSimpleName());
        AudioPlayer.getInstance().init(this);
        MediaSessionManager.getInstance().init(this);
        Notifier.getInstance().init(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    public static void startCommand(Context context, String action) {
        Intent intent = new Intent(context, PlayService.class);
        intent.setAction(action);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case Actions.ACTION_STOP:
                    stop();
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    private void stop() {
        AudioPlayer.getInstance().stopPlayer();
        Notifier.getInstance().cancelAll();
    }
}
