package dor.samet.com.kotlinloginscreen.di

import com.google.i18n.phonenumbers.PhoneNumberUtil
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dor.samet.com.kotlinloginscreen.business_logic.VerificationManager
import dor.samet.com.kotlinloginscreen.business_logic.observable.BaseObservable
import dor.samet.com.kotlinloginscreen.business_logic.observable.Observable
import dor.samet.com.kotlinloginscreen.business_logic.verification.VerificationUseCase
import dor.samet.com.kotlinloginscreen.presentation.login_screen.LoginActivity
import dor.samet.com.kotlinloginscreen.presentation.login_screen.LoginPresenter
import javax.inject.Scope
import kotlin.math.abs

@Module abstract class LoginActivityBinder {

    @ContributesAndroidInjector
    abstract fun loginActivity(): LoginActivity

}

@Module class LoginActivityModule {

    @Provides fun provideLoginActivityPresenter(verificationManager: VerificationManager) = LoginPresenter(verificationManager)

    @Provides fun provideObservable(): Observable<VerificationManager.VerificationObserver> =
            BaseObservable()

    @Provides fun provideVerficationManager(observable: Observable<VerificationManager.VerificationObserver>,
                                            verifyEmailUseCase: VerificationUseCase.VerifyEmail,
                                            verifyUserNameUseCase: VerificationUseCase.VerifyUserName,
                                            verifyPasswordUseCase: VerificationUseCase.VerifyPassword,
                                            verifyPasswordsMatchUseCase: VerificationUseCase.VerifyPasswordsMatch,
                                            verifyPhoneNumberUseCase: VerificationUseCase.VerifyPhoneNumber):
            VerificationManager =

            VerificationManager.Impl(observable, verifyEmailUseCase, verifyUserNameUseCase,
                    verifyPasswordUseCase, verifyPasswordsMatchUseCase, verifyPhoneNumberUseCase)



    @Provides fun providePhonNumberUtilInstance() = PhoneNumberUtil.getInstance()

    @Provides fun provideVerifyEmailUseCase() = VerificationUseCase.VerifyEmail()

    @Provides fun provideVerifyUserUseCase() =  VerificationUseCase.VerifyUserName()

    @Provides fun provideVerifyPhoneNumberUseCase(phoneNumberUtil: PhoneNumberUtil) =
            VerificationUseCase.VerifyPhoneNumber(phoneNumberUtil)

    @Provides fun provideVerifyPasswordUseCase() = VerificationUseCase.VerifyPassword()

    @Provides fun providePasswordsMatch() = VerificationUseCase.VerifyPasswordsMatch()

}