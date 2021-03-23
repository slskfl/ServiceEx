package com.example.serviceex;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MusicService extends Service {
    MediaPlayer mp;

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundService();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mp=MediaPlayer.create(this, R.raw.sky);
        mp.setLooping(true);
        mp.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mp.stop();
        super.onDestroy();
    }

    void startForegroundService(){
        Intent notiIntent=new Intent(this, MainActivity.class);
        PendingIntent pIntent=PendingIntent.getActivity(this, 0, notiIntent, 0);
        //xml과 연동하기
        RemoteViews remoteViews=new RemoteViews(getPackageName(), R.layout.notification);
        NotificationCompat.Builder builder;
        if(Build.VERSION.SDK_INT>=26){
            String channelID="musicServiceChannel";
            //IMPORTANCE_DEFAULT>> 매니저가 알아서 우선순위를 결정함
            NotificationChannel channel=new NotificationChannel(channelID,
                    "음악 채널", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("채널 정의하는 곳");
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 100, 200});
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(channel);
            builder=new NotificationCompat.Builder(this, channelID);
        }else {
            builder=new NotificationCompat.Builder(this);
        }
        builder.setSmallIcon(R.drawable.guitar)
                .setContent(remoteViews)
                .setContentIntent(pIntent);
        startForeground(1, builder.build());
    }
}






















