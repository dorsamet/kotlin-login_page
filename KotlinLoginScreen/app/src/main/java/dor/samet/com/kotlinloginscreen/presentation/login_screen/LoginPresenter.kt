package dor.samet.com.kotlinloginscreen.presentation.login_screen

import android.support.annotation.IdRes
import android.widget.EditText
import dor.samet.com.kotlinloginscreen.R
import dor.samet.com.kotlinloginscreen.business_logic.VerificationManager
import dor.samet.com.rest.presentation.base_mvp.BasePresenter
import dor.samet.com.rest.presentation.base_mvp.MvpView


class LoginPresenter(private val verificationManager: VerificationManager): BasePresenter<LoginView>(),
        VerificationManager.VerificationObserver{

    override fun attachView(view: LoginView) {
        super.attachView(view)
        verificationManager += this
    }

    override fun detachView() {
        verificationManager -= this
        super.detachView()
    }

    fun onEditTextLostFocus(edtText: EditText, @IdRes id: Int) = verify(edtText.text.toString(), id)

    fun onTextChanged(edtText: EditText, @IdRes id: Int) = verify(edtText.text.toString(), id)

    fun onSubmitClicked(input: List<EditText>) {
        input.map {
            verifySync(it.text.toString(), it.id)
        }.reduce {
                    acc, b -> acc && b
                }.also {
                    if (it) {
                        applyOnView {
                            moveToNextActivity()
                        }
                    } else {
                        applyOnView {
                            showToast("Unable to move to next Activity")
                        }
                    }
                }
    }

    private fun verifySync(inputString: String, id: Int): Boolean {
        return when (id) {
            R.id.edtTxtEmail -> verificationManager.verifyEmailSync(inputString)
            R.id.edtTxtName -> verificationManager.verifyNameSync(inputString)
            R.id.edtTxtPhone -> verificationManager.verifyPhoneNumberSync(inputString)
            R.id.edtTxtPinPassword -> verificationManager.verifyPinNumberSync(inputString)
            R.id.edtTxtRepeatPassword -> verificationManager.verifyPinNumbersMatchSync(inputString)
            R.id.edtTxtUserName -> verificationManager.verifyUserNameSync(inputString)
            else -> {
                return false
            }
        }
    }

    private fun verify(inputString: String, id: Int) {
        when (id) {
            R.id.edtTxtEmail -> verificationManager.verifyEmail(inputString)
            R.id.edtTxtName -> verificationManager.verifyName(inputString)
            R.id.edtTxtPhone -> verificationManager.verifyPhoneNumber(inputString)
            R.id.edtTxtPinPassword -> verificationManager.verifyPinNumber(inputString)
            R.id.edtTxtRepeatPassword -> verificationManager.verifyPinNumbersMatch(inputString)
            R.id.edtTxtUserName -> verificationManager.verifyUserName(inputString)
        }
    }

    override fun onEmailVerified() {
        applyOnView {
            clearErrorMessageFor(R.id.edtTxtEmail)
        }
    }

    override fun onEmailVerificationFailed() {
        applyOnView {
            showErrorMessageFor(R.id.edtTxtEmail, "Email verification failed")
        }
    }

    override fun onUserNameVerified() {
        applyOnView {
            clearErrorMessageFor(R.id.edtTxtUserName)
        }
    }

    override fun onUserNameVerificationFailed() {
        applyOnView {
            showErrorMessageFor(R.id.edtTxtUserName, "Username verification failed")
        }
    }

    override fun onNameVerified() {
        applyOnView {
            clearErrorMessageFor(R.id.edtTxtName)
        }
    }

    override fun onNameVerificationFailed() {
        applyOnView {
            showErrorMessageFor(R.id.edtTxtName, "Name verification failed")
        }
    }

    override fun onPinNumberVerified() {
        applyOnView {
            clearErrorMessageFor(R.id.edtTxtPinPassword)
        }
    }

    override fun onPinNumberVerificationFailed() {
        applyOnView {
            showErrorMessageFor(R.id.edtTxtEmail, "Password verification failed")
        }
    }

    override fun onPhoneNumberVerified() {
        applyOnView {
            clearErrorMessageFor(R.id.edtTxtPhone)
        }
    }

    override fun onPhoneNumberVerificationFailed() {
        applyOnView {
            showErrorMessageFor(R.id.edtTxtPhone, "Phone verification failed")
        }
    }

    override fun onPasswordsMatched() {
        applyOnView {
            clearErrorMessageFor(R.id.edtTxtRepeatPassword)
        }
    }

    override fun onPasswordMismatchFailure() {
        applyOnView {
            showErrorMessageFor(R.id.edtTxtRepeatPassword, "Passwords mismatch")
        }
    }

}

interface LoginView: MvpView {

    fun clearErrorMessageFor(@IdRes id: Int)

    fun showErrorMessageFor(@IdRes id: Int, message: String?)

}
