package dor.samet.com.kotlinloginscreen.presentation.login_screen

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import android.widget.EditText
import dagger.android.AndroidInjection
import dor.samet.com.kotlinloginscreen.KotlinLoginApplication
import dor.samet.com.kotlinloginscreen.R
import dor.samet.com.kotlinloginscreen.di.ApplicationComponent
import dor.samet.com.kotlinloginscreen.extensions.addTextWatcher
import dor.samet.com.kotlinloginscreen.presentation.next_screen.NextActivity
import dor.samet.com.rest.presentation.base_mvp.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity

class LoginActivity : BaseActivity<LoginView, LoginPresenter>(), LoginView, LifecycleOwner {

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        lifecycle.addObserver(presenter)
        setContentView(R.layout.activity_login)


        with(btnSubmit) {
            setOnClickListener {
                presenter.onSubmitClicked(listOf(edtTxtName,
                        edtTxtEmail, edtTxtPhone, edtTxtUserName,
                        edtTxtPinPassword, edtTxtRepeatPassword))
            }
        }

        buildEditText(edtTxtName)
        buildEditText(edtTxtEmail)
        buildEditText(edtTxtPhone)
        buildEditText(edtTxtUserName)
        buildEditText(edtTxtPinPassword)
        buildEditText(edtTxtRepeatPassword)

    }

    private fun buildEditText(editText: EditText) {
        with(editText) {
            addTextWatcher(
                    onTextChanged = { _, _, _, _ ->
                        presenter.onTextChanged(this, this.id)
                    }
            )

            setOnFocusChangeListener {
                _, hasFocus ->
                if (!hasFocus) {
                    presenter.onEditTextLostFocus(this, this.id)
                }
            }
        }

    }

    override fun onDestroy() {
        lifecycle.removeObserver(presenter)
        super.onDestroy()
    }

    override fun moveToNextActivity() = startActivity<NextActivity>()

}
