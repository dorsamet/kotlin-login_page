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

            } else {

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
        // TODO: Add view operations here
    }

    override fun onEmailVerificationFailed() {
        // TODO: Add view operations here
    }

    override fun onUserNameVerified() {
        // TODO: Add view operations here
    }

    override fun onUserNameVerificationFailed() {
        // TODO: Add view operations here
    }

    override fun onNameVerified() {
        // TODO: Add view operations here
    }

    override fun onNameVerificationFailed() {
        // TODO: Add view operations here
    }

    override fun onPinNumberVerified() {
        // TODO: Add view operations here
    }

    override fun onPinNumberVerificationFailed() {
        // TODO: Add view operations here
    }

    override fun onPhoneNumberVerified() {
        // TODO: Add view operations here
    }

    override fun onPhoneNumberVerificationFailed() {
        // TODO: Add view operations here
    }

    override fun onPasswordsMatched() {
        // TODO: Add view operations here
    }

    override fun onPasswordMismatchFailure() {
        // TODO: Add view operations here
    }

}

interface LoginView: MvpView {

    // TODO: Add view methods

}
