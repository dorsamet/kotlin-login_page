package dor.samet.com.kotlinloginscreen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.i18n.phonenumbers.PhoneNumberUtil
import dor.samet.com.kotlinloginscreen.business_logic.use_case.UseCase
import dor.samet.com.kotlinloginscreen.business_logic.verification.OnSubmitUseCase
import dor.samet.com.kotlinloginscreen.business_logic.verification.VerificationUseCase
import dor.samet.com.kotlinloginscreen.widgets.addTextWatcher
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    private val useCases = listOf(
            VerificationUseCase.VerifyUserName(),
            VerificationUseCase.VerifyEmail(),
            VerificationUseCase.VerifyPhoneNumber(PhoneNumberUtil.getInstance()),
            VerificationUseCase.VerifyPassword(),
            VerificationUseCase.VerifyPasswordsMatch(),
            VerificationUseCase.VerifyUserName()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        with(btnSubmit) {
            setOnClickListener {
                useCases.map {
                    verificationUseCase ->
                    when(verificationUseCase) {
                        is VerificationUseCase.VerifyUserName -> {
                            verificationUseCase.executeSync(edtTxtUserName.text.toString())
                        }
                        is VerificationUseCase.VerifyPassword -> {
                            verificationUseCase.executeSync(edtTxtPinPassword.text.toString())
                        }
                        is VerificationUseCase.VerifyPasswordsMatch -> {
                            verificationUseCase.executeSync(
                                    edtTxtPinPassword.text.toString() to
                                            edtTxtRepeatPassword.text.toString())
                        }
                        is VerificationUseCase.VerifyEmail -> {
                            verificationUseCase.executeSync(edtTxtEmail.text.toString())
                        }
                        is VerificationUseCase.VerifyPhoneNumber -> {
                            verificationUseCase.executeSync(edtTxtPhone.text.toString())
                        }
                    }
                }.also {
                    if(OnSubmitUseCase().executeSync(it)) {
                        startActivity<NextActivity>()
                    } else {
                        toast("Some errors were detected")
                    }
                }
            }
        }

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
