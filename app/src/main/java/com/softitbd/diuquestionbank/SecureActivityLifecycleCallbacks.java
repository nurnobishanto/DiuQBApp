package com.softitbd.diuquestionbank;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

public class SecureActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private final WindowManager.LayoutParams secureLayoutParams;

    public SecureActivityLifecycleCallbacks(WindowManager.LayoutParams secureLayoutParams) {
        this.secureLayoutParams = secureLayoutParams;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            activity.getWindow().setAttributes(secureLayoutParams);
        }
    }

    // Implement other methods if needed

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
