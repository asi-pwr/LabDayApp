package com.jakdor.labday.robolectric;

import android.view.View;
import android.widget.ImageView;

import com.jakdor.labday.R;
import com.jakdor.labday.view.ui.SplashFragment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApp.class)
public class SplashFragmentTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    //@Rule
    //public final DaggerMockRule<AppComponent> rule = new RobolectricMockTestRule();

    private TestActivity testActivity;
    private SplashFragment splashFragment;

    @Before
    public void setUp(){
        testActivity = Robolectric.setupActivity(TestActivity.class);
        splashFragment = new SplashFragment();
        Assert.assertNotNull(splashFragment);
        testActivity.setFragment(splashFragment);
    }

    @Test
    public void viewTest() throws Exception {
        View view = splashFragment.getView();
        Assert.assertNotNull(view);

        ImageView splashImage = view.findViewById(R.id.splash_logo);
        Assert.assertNotNull(splashImage);
    }
}
