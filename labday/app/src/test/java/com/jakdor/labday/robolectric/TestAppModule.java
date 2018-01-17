package com.jakdor.labday.robolectric;

import com.jakdor.labday.di.AppModule;

import dagger.Module;
import dagger.Provides;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public class TestAppModule extends AppModule {
    @Provides
    @IntoMap
    @ActivityKey(TestActivity.class)
    AndroidInjector.Factory<TestActivity> factory() {
        return instance -> (AndroidInjector<TestActivity>) instance1 -> {

        };
    }
}