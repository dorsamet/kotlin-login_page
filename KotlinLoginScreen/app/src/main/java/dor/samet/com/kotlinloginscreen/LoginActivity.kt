package dor.samet.com.kotlinloginscreen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.i18n.phonenumbers.PhoneNumberUtil
import dor.samet.com.kotlinloginscreen.business_logic.use_case.UseCase
import dor.samet.com.kotlinloginscreen.business_logic.verification.OnSubmitUseCase
import dor.samet.com.kotlinloginscreen.business_logic.verification.VerificationUseCase
import dor.samet.com.kotlinloginscreen.widgets.DefaultTextWatcher
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

        buildEditText(edtTxtName, VerificationUseCase.VerifyUserName()) {
            if(it) {
                edtTxtName.error = null
            } else {
                edtTxtName.error = "Wrong name"
            }
        }

        buildEditText(edtTxtEmail, VerificationUseCase.VerifyEmail()) {
            if(it) {
                edtTxtEmail.error = null
            } else {
                edtTxtEmail.error = "Wrong email"
            }
        }

        buildEditText(edtTxtPhone, VerificationUseCase.VerifyPhoneNumber(PhoneNumberUtil.getInstance())) {
            if(it) {
                edtTxtPhone.error = null
            } else {
                edtTxtPhone.error = "Wrong Phone Number"
            }
        }

        buildEditText(edtTxtPinPassword, VerificationUseCase.VerifyPassword()) {
            if(it) {
                edtTxtPinPassword.error = null
            } else {
                edtTxtPinPassword.error = "Wrong password"
            }
        }

        buildEditText(edtTxtUserName, VerificationUseCase.VerifyUserName()) {
            if(it) {
                edtTxtUserName.error = null
            } else {
                edtTxtUserName.error = "Wrong username"
            }
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

        edtTxtUserName.addTextChangedListener(DefaultTextWatcher()) // Or some other implementation

        edtTxtUserName.setOnFocusChangeListener { v, hasFocus ->  }
    }

    private fun <RESPONSE> buildEditText(editText: EditText, useCase: UseCase<String, RESPONSE>,
                                         callback: (RESPONSE) -> Unit) {
        with(editText) {
            addTextWatcher(
                    onTextChanged = { _, _, _, _ ->
                        useCase.executeAsync(editText.text.toString(), callback)
                    }
            )

            setOnFocusChangeListener {
                _, hasFocus ->
                if (!hasFocus) {
                    useCase.executeAsync(editText.text.toString(), callback)
                }
            }
        }

    }
}
