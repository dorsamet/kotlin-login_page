package dor.samet.com.kotlinloginscreen

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dor.samet.com.kotlinloginscreen.di.ApplicationComponent
import dor.samet.com.kotlinloginscreen.di.DaggerApplicationComponent
import javax.inject.Inject


class KotlinLoginApplication: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector


    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
                .application(this)
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        applicationComponent.inject(this)
    }
}