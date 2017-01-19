package ru.test.doktorov.androidtest;

import android.app.Application;

import ru.test.doktorov.androidtest.di.AppComponent;
import ru.test.doktorov.androidtest.di.modules.ContextModule;
import ru.test.doktorov.androidtest.di.DaggerAppComponent;

public class App extends Application {
    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        sAppComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .build();
    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }
}
