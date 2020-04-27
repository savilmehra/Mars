package com.in.mars.ui;


import android.app.Application;
import com.facebook.stetho.Stetho;


public class MyApplication extends Application {
    private RoomDatabase db;


    @Override
    public void onCreate() {
        super.onCreate();
        db = RoomDatabase.getAppDatabase(getApplicationContext());
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

    }


}
