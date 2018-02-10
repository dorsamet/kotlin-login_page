package dor.samet.com.kotlinloginscreen.presentation.login_screen

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import android.support.annotation.IdRes
import android.widget.EditText
import dagger.android.AndroidInjection
import dor.samet.com.kotlinloginscreen.R
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

    override fun clearErrorMessageFor(@IdRes id: Int) {
        showErrorMessageFor(id, null)
    }

    override fun showErrorMessageFor(@IdRes id: Int, message: String?) {
        when(id) {
            R.id.edtTxtEmail -> edtTxtEmail.error = message
            R.id.edtTxtUserName -> edtTxtUserName.error = message
            R.id.edtTxtName -> edtTxtName.error = message
            R.id.edtTxtPhone -> edtTxtPhone.error = message
            R.id.edtTxtPinPassword -> edtTxtPinPassword.error = message
            R.id.edtTxtRepeatPassword -> edtTxtRepeatPassword.error = message
        }
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
