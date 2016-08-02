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
import android.content.SharedPreferences;
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
import android.widget.EditText;
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
    //private int appWidgetId;
    private AppWidgetHost appWidgetHost;
    private AppWidgetManager appWidgetManager;

    private AppWidgetProviderInfo pendingAppWidgetProviderInfo;
    private int pendingAppWidgetId = -1;
    AppWidgetHostView hostView;

    ViewGroup blah;
    EditText editText;

    int numPackages = 0;
    int numCoreAndroidPackages = 0;
    ArrayAdapter<String> arrayAdapter;
    List<String> applicationNames = new ArrayList<String>();

    // SharedPreferences
    SharedPreferences sharedPrefs;
    private static final String testLauncherPreferences = "TestLauncherPreferences";
    private static final String editTextStringKey = "editTextStringKey";
    private String editTextString;
    private static final String pendingAppWidgetIdKey = "pendingAppWidgetIdKey";

    // parcelable test
    //private AppWidgetProviderInfo appWidgetProviderInfo;
    private TestParcelable testParcelable;
    private static final String RUNTIME_STATE_PENDING_TESTVALUE1 = "launcher.testvalue1";
    private static final String RUNTIME_STATE_PENDING_ADD_WIDGET_INFO = "launcher.add_widget_info";
    private static final String RUNTIME_STATE_PENDING_ADD_WIDGET_ID = "launcher.add_widget_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText = (EditText) findViewById(R.id.editText);


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

            }
        });


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


        sharedPrefs = getSharedPreferences(testLauncherPreferences, Context.MODE_PRIVATE);

        // conditionals may not be needed
        if (sharedPrefs.contains(editTextStringKey)) {
            editText.setText(sharedPrefs.getString(editTextStringKey, ""));
        }
        if (sharedPrefs.contains(pendingAppWidgetIdKey)) {
            pendingAppWidgetId = sharedPrefs.getInt(pendingAppWidgetIdKey, -1);
        }

        // restore widget
        if (pendingAppWidgetId != -1) {
            pendingAppWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(pendingAppWidgetId);

            hostView = appWidgetHost.createView(MainActivity.this, pendingAppWidgetId, pendingAppWidgetProviderInfo);
            hostView.setAppWidget(pendingAppWidgetId, pendingAppWidgetProviderInfo);

            blah.addView(hostView);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras = data.getExtras();
        int appWidgetId2 = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
//                if (resultCode == Activity.RESULT_OK) {
        pendingAppWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetId2);
        pendingAppWidgetId = appWidgetId2;

        hostView = appWidgetHost.createView(MainActivity.this, appWidgetId2, pendingAppWidgetProviderInfo);
        hostView.setAppWidget(appWidgetId2, pendingAppWidgetProviderInfo);

        blah.addView(hostView);

        //Log.d("AppWidgetProviderInfo", pendingAppWidgetProviderInfo

    }

    @Override
    protected void onPause() {
        super.onPause();

        Toast.makeText(MainActivity.this, "Pausing", Toast.LENGTH_SHORT).show();

        // save data here
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editTextString = editText.getText().toString();
        editor.putString(editTextStringKey, editTextString);
        if (pendingAppWidgetId != -1)
            editor.putInt(pendingAppWidgetIdKey, pendingAppWidgetId);

        editor.commit();
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(MainActivity.this, "Destroying", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        //delete mngr;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Toast.makeText(MainActivity.this, "Resuming", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Toast.makeText(MainActivity.this, "Restoring instance state", Toast.LENGTH_SHORT).show();

        //testParcelable = savedInstanceState.getParcelable(RUNTIME_STATE_PENDING_TESTVALUE1);
        //editText.setText(testParcelable.getTestString());
        //if (blah.getChildCount() != 0 ) {
        //    Toast.makeText(MainActivity.this, "blah is not empty", Toast.LENGTH_SHORT).show();
        if (blah.getChildCount() != 0) {
            Toast.makeText(MainActivity.this, "blah is not empty", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "blah is empty", Toast.LENGTH_SHORT).show();
        }
            pendingAppWidgetProviderInfo = savedInstanceState.getParcelable(RUNTIME_STATE_PENDING_ADD_WIDGET_INFO);
            pendingAppWidgetId = savedInstanceState.getInt(RUNTIME_STATE_PENDING_ADD_WIDGET_ID);

            hostView = appWidgetHost.createView(MainActivity.this, pendingAppWidgetId, pendingAppWidgetProviderInfo);
            hostView.setAppWidget(pendingAppWidgetId, pendingAppWidgetProviderInfo);

            blah.addView(hostView);
        //}
        //else {
        //    Toast.makeText(MainActivity.this, "blah is empty", Toast.LENGTH_SHORT).show();
        //}
        //Log.d("MainActivity", "Restoring instance state");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //testParcelable = new TestParcelable(editText.getText().toString());
        //Toast.makeText(MainActivity.this, "editText = " + editText.getText().toString(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(MainActivity.this, "Saving instance state", Toast.LENGTH_SHORT).show();


        //outState.putParcelable(RUNTIME_STATE_PENDING_TESTVALUE1, testParcelable);
//        if (blah.getChildCount() != 0) {
//            Toast.makeText(MainActivity.this, "blah is not empty", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(MainActivity.this, "blah is empty", Toast.LENGTH_SHORT).show();
//        }
        outState.putParcelable(RUNTIME_STATE_PENDING_ADD_WIDGET_INFO, pendingAppWidgetProviderInfo);
        outState.putInt(RUNTIME_STATE_PENDING_ADD_WIDGET_ID, pendingAppWidgetId);
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
