package com.app.budometer.util;


import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.app.budometer.model.DaoMaster;
import com.app.budometer.model.DaoSession;

import org.greenrobot.greendao.database.Database;

public class BudometerApp extends Application {
    private static Context context;
    private static DaoSession daoSession;
    public static BudometerApp get(Activity activity) {
        return (BudometerApp) activity.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        BudometerApp.context = getApplicationContext();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "market-garden-db");
        Database db = helper.getWritableDb();

        daoSession = new DaoMaster(db).newSession();
    }

    public static Context getAppContext() {
        return BudometerApp.context;
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
