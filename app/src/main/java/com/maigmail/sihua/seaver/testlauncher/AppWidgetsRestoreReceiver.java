package com.maigmail.sihua.seaver.testlauncher; /**
 * Created by Seaver on 7/5/2016.
 */
//package com.android.launcher3;
//package com.maigmail.sihua.seaver.testlauncher;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

//import com.android.launcher3.LauncherSettings.Favorites;
//import com.android.launcher3.compat.AppWidgetManagerCompat;

import java.util.ArrayList;
import java.util.List;

public class AppWidgetsRestoreReceiver extends BroadcastReceiver {

    private static final String TAG = "WidgetRestoredRcvr";

    private static final String APPWIDGET_ID = "appWidgetId";
    private static final int APPWIDGET_HOST_ID = 1024;
    private static final String RESTORED = "restored";

    public static final int FLAG_UI_NOT_READY = 4;
    public static final int FLAG_PROVIDER_NOT_READY = 2;

    public static final String AUTHORITY = "com.maigmail.sihua.seaver.testlauncher.settings";
    public static final String TABLE_NAME = "favorites";

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "RestoreWidget onReceive", Toast.LENGTH_SHORT).show();

        if (AppWidgetManager.ACTION_APPWIDGET_HOST_RESTORED.equals(intent.getAction())) {
            int[] oldIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_OLD_IDS);
            int[] newIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            if (oldIds.length == newIds.length) {
                Log.d("RESTORE", "Restore appWidgetIds");
                // RESTORE APP WIDGET IDS
                restoreAppWidgetIds(context, oldIds, newIds);
            } else {
                Log.e(TAG, "Invalid host restored received");
            }
        }
        //String ii = AppWidgetManager.EXTRA_APPWIDGET_OLD_IDS;
    }

    /**
     * Updates the app widgets whose id has changed during the restore process.
     */
    static void restoreAppWidgetIds(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        final ContentResolver cr = context.getContentResolver();
        final List<Integer> idsToRemove = new ArrayList<Integer>();
        final AppWidgetManager widgets = AppWidgetManager.getInstance(context);

        for (int i = 0; i < oldWidgetIds.length; i++) {
            Log.i(TAG, "Widget state restore id " + oldWidgetIds[i] + " => " + newWidgetIds[i]);

            final AppWidgetProviderInfo provider = widgets.getAppWidgetInfo(newWidgetIds[i]);
            final int state;
            if ((provider != null) && (provider.provider != null)
                    && (provider.provider.getPackageName() != null)) {
                // This will ensure that we show 'Click to setup' UI if required.
                state = FLAG_UI_NOT_READY;
            } else {
                state = FLAG_PROVIDER_NOT_READY;
            }

            ContentValues values = new ContentValues();
            values.put(APPWIDGET_ID, newWidgetIds[i]);
            values.put(RESTORED, state);

            String[] widgetIdParams = new String[] { Integer.toString(oldWidgetIds[i]) };

            final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

            int result = cr.update(CONTENT_URI, values,
                    "appWidgetId=? and (restored & 1) = 1", widgetIdParams);
            if (result == 0) {
                Cursor cursor = cr.query(CONTENT_URI,
                        new String[] {APPWIDGET_ID},
                        "appWidgetId=?", widgetIdParams, null);
                try {
                    if (!cursor.moveToFirst()) {
                        // The widget no long exists.
                        idsToRemove.add(newWidgetIds[i]);
                    }
                } finally {
                    cursor.close();
                }
            }
        }
        // Unregister the widget IDs which are not present on the workspace. This could happen
        // when a widget place holder is removed from workspace, before this method is called.
        if (!idsToRemove.isEmpty()) {
            final AppWidgetHost appWidgetHost =
                    new AppWidgetHost(context, APPWIDGET_HOST_ID);
            new AsyncTask<Void, Void, Void>() {
                public Void doInBackground(Void ... args) {
                    for (Integer id : idsToRemove) {
                        appWidgetHost.deleteAppWidgetId(id);
                        Log.e(TAG, "Widget no longer present, appWidgetId=" + id);
                    }
                    return null;
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        }

//        LauncherAppState app = LauncherAppState.getInstanceNoCreate();
//        if (app != null) {
//            app.reloadWorkspace();
//        }
    }

//    public void resetLoadedState(boolean resetAllAppsLoaded, boolean resetWorkspaceLoaded) {
//        synchronized (lock) {
//            // Stop any existing loaders first, so they don't set mAllAppsLoaded or
//            // mWorkspaceLoaded to true later
//            stopLoaderLocked();
//            if (resetAllAppsLoaded) mAllAppsLoaded = false;
//            if (resetWorkspaceLoaded) mWorkspaceLoaded = false;
//        }
//    }
//
//
//    /**
//     * When the launcher is in the background, it's possible for it to miss paired
//     * configuration changes.  So whenever we trigger the loader from the background
//     * tell the launcher that it needs to re-run the loader when it comes back instead
//     * of doing it now.
//     */
//    public void startLoaderFromBackground() {
//        boolean runLoader = false;
//        Callbacks callbacks = getCallback();
//        if (callbacks != null) {
//            // Only actually run the loader if they're not paused.
//            if (!callbacks.setLoadOnResume()) {
//                runLoader = true;
//            }
//        }
//        if (runLoader) {
//            startLoader(PagedView.INVALID_RESTORE_PAGE);
//        }
//    }
//
//    /**
//     * If there is already a loader task running, tell it to stop.
//     */
//    private void stopLoaderLocked() {
//        LoaderTask oldTask = mLoaderTask;
//        if (oldTask != null) {
//            oldTask.stopLocked();
//        }
//    }
//
//
//
//
//
//    /**
//     * Runnable for the thread that loads the contents of the launcher:
//     *   - workspace icons
//     *   - widgets
//     *   - all apps icons
//     */
//    private class LoaderTask implements Runnable {
//        private Context mContext;
//        @Thunk boolean mIsLoadingAndBindingWorkspace;
//        private boolean mStopped;
//        @Thunk boolean mLoadAndBindStepFinished;
//        private int mFlags;
//
//        LoaderTask(Context context, int flags) {
//            mContext = context;
//            mFlags = flags;
//        }
//
//        public void run() {
//            synchronized (mLock) {
//                if (mStopped) {
//                    return;
//                }
//                mIsLoaderTaskRunning = true;
//            }
//            // Optimize for end-user experience: if the Launcher is up and // running with the
//            // All Apps interface in the foreground, load All Apps first. Otherwise, load the
//            // workspace first (default).
//            keep_running:
//            {
//                if (DEBUG_LOADERS) Log.d(TAG, "step 1: loading workspace");
//                loadAndBindWorkspace();
//
//                if (mStopped) {
//                    break keep_running;
//                }
//
//                waitForIdle();
//
//                // second step
//                if (DEBUG_LOADERS) Log.d(TAG, "step 2: loading all apps");
//                loadAndBindAllApps();
//            }
//
//            // Clear out this reference, otherwise we end up holding it until all of the
//            // callback runnables are done.
//            mContext = null;
//
//            synchronized (mLock) {
//                // If we are still the last one to be scheduled, remove ourselves.
//                if (mLoaderTask == this) {
//                    mLoaderTask = null;
//                }
//                mIsLoaderTaskRunning = false;
//                mHasLoaderCompletedOnce = true;
//            }
//        }
//
//        public void stopLocked() {
//            synchronized (LoaderTask.this) {
//                mStopped = true;
//                this.notify();
//            }
//        }
//    }
}
