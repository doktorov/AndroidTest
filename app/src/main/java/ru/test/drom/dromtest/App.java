package ru.test.drom.dromtest;

import android.app.Application;

import ru.test.drom.dromtest.di.AppComponent;
import ru.test.drom.dromtest.di.DaggerAppComponent;
import ru.test.drom.dromtest.di.modules.ContextModule;

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
