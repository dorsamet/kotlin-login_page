package dor.samet.com.kotlinloginscreen.business_logic

import dor.samet.com.kotlinloginscreen.business_logic.observable.Observable
import dor.samet.com.kotlinloginscreen.business_logic.verification.VerificationUseCase
import dor.samet.com.kotlinloginscreen.extensions.Email


interface VerificationManager: Observable<VerificationManager.VerificationObserver> {

    interface VerificationObserver {

        fun onEmailVerified()

        fun onEmailVerificationFailed()

        fun onUserNameVerified()

        fun onUserNameVerificationFailed()

        fun onNameVerified()

        fun onNameVerificationFailed()

        fun onPinNumberVerified()

        fun onPinNumberVerificationFailed()

        fun onPhoneNumberVerified()

        fun onPhoneNumberVerificationFailed()

        fun onPasswordsMatched()

        fun onPasswordMismatchFailure()

    }

    fun verifyEmail(email: Email)
    fun verifyUserName(userName: String)
    fun verifyName(name: String)
    fun verifyPhoneNumber(phoneNumber: String)
    fun verifyPinNumber(pinNumber: String)
    fun verifyPinNumbersMatch(pinNumber: String)

    fun verifyEmailSync(email: Email): Boolean
    fun verifyUserNameSync(userName: String): Boolean
    fun verifyNameSync(name: String): Boolean
    fun verifyPhoneNumberSync(phoneNumber: String): Boolean
    fun verifyPinNumberSync(pinNumber: String): Boolean
    fun verifyPinNumbersMatchSync(pinNumber: String): Boolean

    class Impl(private val observable: Observable<VerificationObserver>,
               private val verifyEmailUseCase: VerificationUseCase.VerifyEmail,
               private val verifyUserNameUseCase: VerificationUseCase.VerifyUserName,
               private val verifyPasswordUseCase: VerificationUseCase.VerifyPassword,
               private val verifyPasswordsMatchUseCase: VerificationUseCase.VerifyPasswordsMatch,
               private val verifyPhoneNumberUseCase: VerificationUseCase.VerifyPhoneNumber):
            Observable<VerificationManager.VerificationObserver> by observable,
            VerificationManager {
        override fun verifyEmailSync(email: Email) =
                verifyEmailUseCase.executeSync(email)

        override fun verifyUserNameSync(userName: String) =
                verifyUserNameUseCase.executeSync(userName)

        override fun verifyNameSync(name: String) =
                verifyUserNameUseCase.executeSync(name)

        override fun verifyPhoneNumberSync(phoneNumber: String) =
                verifyPhoneNumberUseCase.executeSync(phoneNumber)

        override fun verifyPinNumberSync(pinNumber: String) =
                verifyPasswordUseCase.executeSync(pinNumber)

        override fun verifyPinNumbersMatchSync(pinNumber: String) =
                verifyPasswordsMatchUseCase.executeSync(pinNumber to pin)

        private var pin: String = ""

        override fun verifyEmail(email: Email) {
            verifyEmailUseCase.executeAsync(email) { result ->
                if (result) {
                    notifyObservers { it.onEmailVerified() }
                } else {
                    notifyObservers { it.onEmailVerificationFailed() }
                }
            }
        }

        override fun verifyUserName(userName: String) {
            verifyUserNameUseCase.executeAsync(userName) { result ->
                if (result) {
                    notifyObservers { it.onUserNameVerified() }
                } else {
                    notifyObservers { it.onUserNameVerificationFailed() }
                }
            }
        }

        override fun verifyName(name: String) {
            verifyUserNameUseCase.executeAsync(name) { result ->
                if (result) {
                    notifyObservers { it.onNameVerified() }
                } else {
                    notifyObservers { it.onNameVerificationFailed() }
                }
            }
        }

        override fun verifyPhoneNumber(phoneNumber: String) {
            verifyPhoneNumberUseCase.executeAsync(phoneNumber) { result ->
                if (result) {
                    notifyObservers { it.onPhoneNumberVerified() }
                } else {
                    notifyObservers { it.onPhoneNumberVerificationFailed() }
                }
            }
        }

        override fun verifyPinNumber(pinNumber: String) {
            verifyPasswordUseCase.executeAsync(pinNumber) { result ->
                if (result) {
                    pin = pinNumber
                    notifyObservers { it.onPinNumberVerified() }
                } else {
                    notifyObservers { it.onPinNumberVerificationFailed() }
                }
            }
        }

        override fun verifyPinNumbersMatch(pinNumber: String) {
            verifyPasswordsMatchUseCase.executeAsync(pin to pinNumber) { result ->
                if (result) {
                    notifyObservers { it.onPasswordsMatched() }
                } else {
                    notifyObservers { it.onPasswordMismatchFailure() }
                }
            }
        }
    }
}