package com.jakdor.labday.robolectric;


import android.app.Activity;

import com.jakdor.labday.App;
import com.jakdor.labday.di.DaggerAppComponent;
import com.jakdor.labday.viewmodel.SplashViewModel;

import org.mockito.Mockito;

import dagger.android.AndroidInjector;

/**
 * Robolectric test app
 */
public class TestApp extends App {
    @Override
    public void onCreate() {
        DaggerAppComponent.builder()
                .build()
                .inject(this);
    }

    /*
    @Override
    public AndroidInjector<Activity> activityInjector() {
        return instance -> {
            SplashViewModel splashViewModel = Mockito.mock(SplashViewModel.class);
            ((TestActivity)instance).viewModelFactory = ViewModel;
        };
    }
    */
}
