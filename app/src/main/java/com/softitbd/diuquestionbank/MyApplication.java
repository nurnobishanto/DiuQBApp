package com.softitbd.diuquestionbank;

import android.app.Application;
import android.os.Build;
import android.view.WindowManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Protect against screenshots for the entire app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Use the FLAG_SECURE for Android P (API level 28) and above
            WindowManager.LayoutParams attrs = new WindowManager.LayoutParams();
            attrs.flags = WindowManager.LayoutParams.FLAG_SECURE;
            registerActivityLifecycleCallbacks(new SecureActivityLifecycleCallbacks(attrs));
        } else {
            // For versions below Android P, consider alternative solutions
            // as FLAG_SECURE has some limitations on earlier versions
        }
    }
}
