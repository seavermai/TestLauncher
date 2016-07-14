package com.maigmail.sihua.seaver.testlauncher;

import android.app.Activity;
import android.app.Dialog;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import homescreenshortcutmanager.HomeScreenShortcutManager;

public class MainActivity extends AppCompatActivity {

    HomeScreenShortcutManager mngr;
    private final String TAG = "packagemanager";

    private static final int REQUEST_PICK_APPWIDGET = 80;
    AppWidgetHost appWidgetHost;
    AppWidgetManager appWidgetManager;

    ViewGroup blah;

    int numPackages = 0;
    int numCoreAndroidPackages = 0;
    ArrayAdapter<String> arrayAdapter;
    List<String> applicationNames = new ArrayList<String>();

    // parcelable test
    //private AppWidgetProviderInfo appWidgetProviderInfo;
    private TestParcelable testParcelable;
    private static final String RUNTIME_STATE_PENDING_TESTVALUE1 = "launcher.testvalue1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    final PackageManager pm = getPackageManager();
    ApplicationInfo ai;
    //get a list of installed apps.
    List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

    ArrayAdapter<String> applicationsAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.layout_list, applicationNames);

    appWidgetManager = AppWidgetManager.getInstance(this);
    appWidgetHost = new AppWidgetHost(this, R.id.APPWIDGET_HOST_ID);

    List<AppWidgetProviderInfo> listWidgets = appWidgetManager.getInstalledProviders();
//    for (AppWidgetProviderInfo info : listWidgets) {
//        Log.d(TAG, "Name: " + info.label );
//        Log.d(TAG, "Provider Name: " + info.provider);
//        Log.d(TAG, "Configure Name: " + info.configure);
//    }

    blah = (ViewGroup) findViewById(R.id.blah);
    blah.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//				Toast.makeText(MainActivity.this, "blah clicked", Toast.LENGTH_SHORT).show();

            int appWidgetId = appWidgetHost.allocateAppWidgetId();
            Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
            pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            Bundle dataToSend = new Bundle();
            dataToSend.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            //dataToSend.putInt(LAYOUT_NUMBER, layoutNumber);
            pickIntent.putExtras(dataToSend);
            //startActivityForResult(pickIntent, R.id.REQUEST_PICK_APPWIDGET);
            startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET);

            //appwidgetid = 238

            /*
            Bundle extras = pickIntent.getExtras();
            int appWidgetId2 = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
//                if (resultCode == Activity.RESULT_OK) {
            AppWidgetProviderInfo appWidgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId2);
            //int[] ii = appWidgetManager.getAppWidgetIds(new ComponentName(getPackageName(), Widget.class.getName()), view);

//                Log.d("CHOSEN", "Name: " + appWidgetInfo.label );
//                Log.d("CHOSEN", "Provider Name: " + appWidgetInfo.provider);
//                Log.d("CHOSEN", "Configure Name: " + appWidgetInfo.configure);

*/

//                AppWidgetProviderInfo appWidgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId);
//
//                //AppWidgetHostView hostView = appWidgetHost.createView(MainActivity.this, appWidgetId2, appWidgetInfo);
//                AppWidgetHostView hostView = appWidgetHost.createView(MainActivity.this, appWidgetId, appWidgetInfo);
//                hostView.setAppWidget(appWidgetId, appWidgetInfo);
//
//                blah.addView(hostView);

//             startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET);



        }
    });

/*
    for (ApplicationInfo packageInfo : packages) {
        if (pm.getLaunchIntentForPackage(packageInfo.packageName) != null) {
            String packageName = packageInfo.packageName;
//                String sourceDir = packageInfo.sourceDir;
//                String launchActivity = pm.getLaunchIntentForPackage(packageInfo.packageName).toString();

//                Log.d(TAG, "Installed package: " + packageInfo.packageName);
//                Log.d(TAG, "Source dir: " + packageInfo.sourceDir);
//                Log.d(TAG, "Launch Activity: " + pm.getLaunchIntentForPackage(packageInfo.packageName));
            Log.d(TAG, "Installed package: " + packageName);
//                Log.d(TAG, "Source dir: " + sourceDir);
//                Log.d(TAG, "Launch Activity: " + launchActivity);

            try {
                ai = pm.getApplicationInfo(packageName, 0);
            } catch ( PackageManager.NameNotFoundException e ) {
                ai = null;
            }
            String applicationName = "";
            if (ai != null)
                applicationName = pm.getApplicationLabel(ai).toString();
            else
                applicationName = "(Unknown)";

            applicationNames.add(applicationName);
            numPackages++;

            if (packageName.contains("com.android."))
                numCoreAndroidPackages++;
        }

    }
*/

    Map<String, String> map = new HashMap<String, String>();
    for (ApplicationInfo packageInfo : packages) {
        if (pm.getLaunchIntentForPackage(packageInfo.packageName) != null) {
            String packageName = packageInfo.packageName;
            try {
                ai = pm.getApplicationInfo(packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                ai = null;
            }
            String applicationName = "";
            if (ai != null)
                applicationName = pm.getApplicationLabel(ai).toString();
            else
                applicationName = "(Unknown)";

            map.put(packageName, applicationName);
        }
    }

    Set<Map.Entry<String, String>> set = map.entrySet();
    List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(set);
    Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
        public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
            return (o1.getValue()).compareTo(o2.getValue());
        }
    });
    for (Map.Entry<String, String> en : list) {
        //Log.d("tag", en.getValue());
        applicationNames.add(en.getValue());
    }


    //String[] dynamicList = { "App1", "App2", "App3", "App4", "App5" };
    //mngr = new HomeScreenShortcutManager(this);

    //dynamic allocation into ArrayAdapter
    arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.layout_list,
            R.id.layout_list_element, applicationNames);
//        for (String s : dynamicList)
//            arrayAdapter.add(s);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId2 = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
//                if (resultCode == Activity.RESULT_OK) {
        AppWidgetProviderInfo appWidgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId2);

        AppWidgetHostView hostView = appWidgetHost.createView(MainActivity.this, appWidgetId2, appWidgetInfo);
        hostView.setAppWidget(appWidgetId2, appWidgetInfo);

        blah.addView(hostView);



        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(MainActivity.this, "Destroying", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        //delete mngr;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Toast.makeText(MainActivity.this, "Restoring instance state", Toast.LENGTH_SHORT).show();
        testParcelable = savedInstanceState.getParcelable(RUNTIME_STATE_PENDING_TESTVALUE1);
        Log.d("MainActivity", "Restoring instance state");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Toast.makeText(MainActivity.this, "Saving instance state", Toast.LENGTH_SHORT).show();
        outState.putParcelable(RUNTIME_STATE_PENDING_TESTVALUE1, testParcelable);
        //Log.d("MainActivity", "Saving instance state");
    }

//    public void onClickShowListPopUpWindow(int anchorViewId) {
    public void onClickShowListPopUpWindow(View view) {
        //Toast.makeText(this, "showList pressed: " , Toast.LENGTH_SHORT).show();

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle("title");
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //String s = arrayAdapter.getItem(which);
                Toast.makeText(MainActivity.this, arrayAdapter.getItem(which), Toast.LENGTH_SHORT)
                        .show();
//                TextView textView = (TextView)findViewById(R.id.testText);
//                textView.setText(arrayAdapter.getItem(which));

            }
        });
//                builder.setItems(R.array.items, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(this, )
//                            }
//                        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();

    }

    public void onClickHomeScreenShortcutManager(View view) {
        //Toast.makeText(this, "CLICK", Toast.LENGTH_SHORT).show();

        //mngr.createShortcut();
/*
        int numPackages = 0;
        int numCoreAndroidPackages = 0;

        final PackageManager pm = getPackageManager();
        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            if (pm.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                String packageName = packageInfo.packageName;
                String sourceDir = packageInfo.sourceDir;
//                String launchActivity = pm.getLaunchIntentForPackage(packageInfo.packageName).toString();

//                Log.d(TAG, "Installed package: " + packageInfo.packageName);
//                Log.d(TAG, "Source dir: " + packageInfo.sourceDir);
//                Log.d(TAG, "Launch Activity: " + pm.getLaunchIntentForPackage(packageInfo.packageName));
                Log.d(TAG, "Installed package: " + packageName);
                Log.d(TAG, "Source dir: " + sourceDir);
//                Log.d(TAG, "Launch Activity: " + launchActivity);

                numPackages++;

                if (packageName.contains("com.android."))
                    numCoreAndroidPackages++;
            }
//            if (pm.getLaunchIntentForPackage(packageInfo.packageName) != null)
//                i++;
        }
        // the getLaunchIntentForPackage returns an intent that you can use with startActivity()
*/

        Toast.makeText(MainActivity.this,
                "# of packages: " + numPackages
                       + "; # of core android packages: " + numCoreAndroidPackages,
                Toast.LENGTH_SHORT).show();
    }

//    public static String createAppPicker(Context context) {
//        final String packageName;
//        Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        final ListView appList;
//        final PackageManager pm = context.getPackageManager();
//        final List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//        final ArrayList<String> titles = new ArrayList<String>();
//        for(int i = 0; i < packages.size(); i++) {
//            titles.add(packages.get(i).loadLabel(pm).toString());
//        }
//        Collections.sort(titles);
//        final String[] values = new String[packages.size()];
//        for(int i = 0; i < titles.size(); i++) {
//            values[i] = titles.get(i);
//        }
//        dialog.setContentView(R.layout.activity_main);
//        appList = (ListView)dialog.findViewById(R.id.applicationList);
//        appList.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, values));
//        appList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                packageName = packages.get(position).packageName;
//                packageName.getItemAtPosition
//            }
//        });
//        dialog.show();
//        return packageName;
//
//    }
}
