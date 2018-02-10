package dor.samet.com.kotlinloginscreen.presentation.login_screen

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import dor.samet.com.kotlinloginscreen.R
import dor.samet.com.kotlinloginscreen.extensions.addTextWatcher
import dor.samet.com.kotlinloginscreen.presentation.next_screen.NextActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import android.arch.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import javax.inject.Inject



class LoginActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var loginViewModel: LoginViewModel

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_login)
        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
        createBindings()

    }

    private fun createBindings() {
        with(btnSubmit) {
            setOnClickListener {
                loginViewModel.onSubmitClicked(listOf(edtTxtName,
                        edtTxtEmail, edtTxtPhone, edtTxtUserName,
                        edtTxtPinPassword, edtTxtRepeatPassword))
            }
        }

        edtTxtName.buildEditText()
        edtTxtEmail.buildEditText()
        edtTxtPhone.buildEditText()
        edtTxtUserName.buildEditText()
        edtTxtPinPassword.buildEditText()
        edtTxtRepeatPassword.buildEditText()

        loginViewModel.moveToNextActivity.observe(this, Observer {
            startActivity<NextActivity>()
        })

        loginViewModel.unableToMoveToNextActivity.observe(this, Observer {
            toast("Unable to move to next activity")
        })

        loginViewModel.emailWasVerified.observe(this, Observer {
            edtTxtEmail.reactToEvent(it!!, "Email was not verified")
        })

        loginViewModel.namedWasVerified.observe(this, Observer {
            edtTxtName.reactToEvent(it!!, "Name was not verified")
        })

        loginViewModel.userNamedWasVerified.observe(this, Observer {
            edtTxtUserName.reactToEvent(it!!, "Username was not verified")
        })

        loginViewModel.passwordWasVerified.observe(this, Observer {
            edtTxtPinPassword.reactToEvent(it!!, "Password was not verified")
        })

        loginViewModel.passwordRepeatWasVerified.observe(this, Observer {
            edtTxtRepeatPassword.reactToEvent(it!!, "Passwords do not match")
        })

        loginViewModel.phoneWasVerified.observe(this, Observer {
            edtTxtPhone.reactToEvent(it!!, "Phone was not verified")
        })

    }


    private fun EditText.reactToEvent(wasVerified: Boolean, errorMessage: String) {
        if (wasVerified) {
            this.error = null
        } else {
            this.error = errorMessage
        }
    }

    private fun EditText.buildEditText() {
        addTextWatcher(
                onTextChanged = { _, _, _, _ ->
                    loginViewModel.onTextChanged(this.text.toString(), this.id)
                }
        )

        setOnFocusChangeListener {
            _, hasFocus ->
            if (!hasFocus) {
                loginViewModel.onEditTextLostFocus(this.text.toString(), this.id)
            }
        }
    }
}
