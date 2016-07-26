package com.maigmail.sihua.seaver.testlauncher;

import android.appwidget.AppWidgetProviderInfo;

/**
 * Created by Seaver on 7/21/2016.
 */
public class BaseWidget implements BaseWidgetInterface {
    @Override
    public String getLabel() {
        return "Dumb Launcher Widget";
    }

    @Override
    public int getPreviewImage() {
        return 0;
    }

    @Override
    public int getIcon() {
        return 0;
    }

    @Override
    public int getWidgetLayout() {
        return R.layout.base_widget;
    }

    @Override
    public int getSpanX() {
        return 1;
    }

    @Override
    public int getSpanY() {
        return 1;
    }

    @Override
    public int getMinSpanX() {
        return 1;
    }

    @Override
    public int getMinSpanY() {
        return 1;
    }

    @Override
    public int getResizeMode() {
        return AppWidgetProviderInfo.RESIZE_BOTH;
    }
}
