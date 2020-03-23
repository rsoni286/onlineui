package com.purutaltechs.onlineui;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import androidx.multidex.MultiDexApplication;

public class Application extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
