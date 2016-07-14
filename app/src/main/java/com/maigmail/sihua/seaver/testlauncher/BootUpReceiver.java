package com.maigmail.sihua.seaver.testlauncher;

/**
 * Created by Seaver on 7/5/2016.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent i = new Intent(context, MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(i);
//
//        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
//        ComponentName widgetComponent = new ComponentName(context,MyWidgetProvider.class);
//        int[] appWidgetIds = widgetManager.getAppWidgetIds(widgetComponent);
//
//        for(int i=0;i<appWidgetIds.length;i++) {
//
//            //Updating code here
//        }

        Toast.makeText(context, "BootUpReceiver received", Toast.LENGTH_SHORT).show();
    }
}