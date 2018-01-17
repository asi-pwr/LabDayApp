package com.jakdor.labday.robolectric;

import com.jakdor.labday.di.AppComponent;
import com.jakdor.labday.di.BuildersModule;
import com.jakdor.labday.di.RepositoryModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        TestAppModule.class,
        BuildersModule.class,
        RepositoryModule.class})
public interface TestAppComponent extends AppComponent {

    void inject(TestApp app);
}
