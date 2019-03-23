package com.example.yizu.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;

import com.example.yizu.MainActivity;
import com.example.yizu.R;
import com.example.yizu.UserGoodsActivity;
import com.example.yizu.tool.ActivityCollecter;

public class StateChangeService extends Service {
    public StateChangeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intent1 = new Intent(this, UserGoodsActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0, intent1,0);
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("YI租在线平台")
                .setContentText("您的出租物品有了新变化")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)//用户点击就自动消失
                .build();
        startForeground(1,notification);

        manager.cancel(1);

        //ActivityCollecter.finishALL();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
}
