package dor.samet.com.kotlinloginscreen.business_logic

import dor.samet.com.kotlinloginscreen.business_logic.observable.Observable


interface VerificationManager {

    interface VerificationObserver {

        fun onVerificationComplete()

        fun onEmailVerificationFailed()

        fun onNameVerificationFailed()

        fun onPinNumberVerificationFailed()

        fun onPhoneNumberVerificationFailed()

        fun onPasswordMismatchFailure()

    }

    class Impl(private val observable: Observable<VerificationObserver>):
            Observable<VerificationManager.VerificationObserver> by observable {
    }
}