package dor.samet.com.kotlinloginscreen.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dor.samet.com.kotlinloginscreen.KotlinLoginApplication
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME) annotation class ApplicationScope

@ApplicationScope
@Component(modules = [LoginActivityModule::class, AndroidInjectionModule::class,
    LoginActivityBinder::class])
interface ApplicationComponent {

    fun inject(application: KotlinLoginApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: KotlinLoginApplication): Builder
        fun build(): ApplicationComponent

    }

}