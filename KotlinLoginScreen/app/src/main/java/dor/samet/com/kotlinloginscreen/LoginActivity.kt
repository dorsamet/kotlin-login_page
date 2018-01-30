package dor.samet.com.kotlinloginscreen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.i18n.phonenumbers.PhoneNumberUtil
import dor.samet.com.kotlinloginscreen.business_logic.use_case.UseCase
import dor.samet.com.kotlinloginscreen.business_logic.verification.VerificationUseCase
import dor.samet.com.kotlinloginscreen.widgets.addTextWatcher
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        with(edtTxtName) {
            addTextWatcher(
                    onTextChanged = {
                        s: CharSequence?, _: Int, _: Int, _: Int ->
                        val useCase: UseCase<String, Boolean> = VerificationUseCase.VerifyUserName()
                        useCase.executeAsync(s.toString()) {
                            if(it) {
                                this@with.error = null
                            } else {
                                this@with.error = "Wrong name"
                            }
                        }
                    }
            )
        }

        with(edtTxtEmail) {
            addTextWatcher(
                    onTextChanged = {
                        s: CharSequence?, _: Int, _: Int, _: Int ->
                        val useCase: UseCase<String, Boolean> = VerificationUseCase.VerifyEmail()
                        useCase.executeAsync(s.toString()) {
                            if(it) {
                                this@with.error = null
                            } else {
                                this@with.error = "Wrong email"
                            }
                        }
                    }
            )
        }

        with(edtTxtPhone) {
            addTextWatcher(
                    onTextChanged = {
                        s: CharSequence?, _: Int, _: Int, _: Int ->
                        val useCase: UseCase<String, Boolean> = VerificationUseCase.VerifyPhoneNumber(PhoneNumberUtil.getInstance())
                        useCase.executeAsync(s.toString()) {
                            if(it) {
                                this@with.error = null
                            } else {
                                this@with.error = "Wrong Phone Number"
                            }
                        }
                    }
            )
        }

        with(edtTxtPinPassword) {
            addTextWatcher(
                    onTextChanged = {
                        s: CharSequence?, _: Int, _: Int, _: Int ->
                        val useCase: UseCase<String, Boolean> = VerificationUseCase.VerifyPassword()
                        useCase.executeAsync(s.toString()) {
                            if(it) {
                                this@with.error = null
                            } else {
                                this@with.error = "Wrong password"
                            }
                        }
                    }
            )
        }

        with(edtTxtRepeatPassword) {
            addTextWatcher(
                    onTextChanged = {
                        s: CharSequence?, _: Int, _: Int, _: Int ->
                        val useCase: UseCase<Pair<String, String>, Boolean> = VerificationUseCase.VerifyPasswordsMatch()
                        useCase.executeAsync(this.text.toString() to edtTxtPinPassword.text.toString()) {
                            if(it) {
                                this@with.error = null
                            } else {
                                this@with.error = "Passwords mismatch"
                            }
                        }
                    }
            )
        }

        with(edtTxtUserName) {
            addTextWatcher(
                    onTextChanged = {
                        s: CharSequence?, _: Int, _: Int, _: Int ->
                        val useCase: UseCase<String, Boolean> = VerificationUseCase.VerifyUserName()
                        useCase.executeAsync(s.toString()) {
                            if(it) {
                                this@with.error = null
                            } else {
                                this@with.error = "Wrong username"
                            }
                        }
                    }
            )
        }
    }
}
