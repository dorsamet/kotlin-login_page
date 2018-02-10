package dor.samet.com.kotlinloginscreen.presentation.login_screen

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.android.AndroidInjection
import dor.samet.com.kotlinloginscreen.R
import dor.samet.com.kotlinloginscreen.presentation.next_screen.NextActivity
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class LoginActivity : AppCompatActivity(), LoginViewModel.ObservableLoginView {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var disposable: Disposable

    override lateinit var edtTxtNameObservable: Observable<Pair<String, Int>>
    override lateinit var edtTxtUserNameObservable: Observable<Pair<String, Int>>
    override lateinit var edtTxtEmailObservable: Observable<Pair<String, Int>>
    override lateinit var edtTxtPhoneObservable: Observable<Pair<String, Int>>
    override lateinit var edtTxtPasswordObservable: Observable<Pair<String, Int>>
    override lateinit var edtTxtPasswordRepeatObservable: Observable<Pair<String, Int>>
    override lateinit var onClickObservable: Observable<List<Pair<String, Int>>>

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_login)
        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
        createBindings()
    }

    private fun createBindings() {
        onClickObservable = RxView.clicks(btnSubmit)
                .map {
                    listOf(edtTxtName,
                            edtTxtEmail, edtTxtPhone, edtTxtUserName,
                            edtTxtPinPassword, edtTxtRepeatPassword)

                }.map {
                    it.map {
                        it.text.toString() to it.id
                    }
                }

        edtTxtNameObservable = buildEditText(edtTxtName)
        edtTxtEmailObservable = buildEditText(edtTxtEmail)
        edtTxtPhoneObservable = buildEditText(edtTxtPhone)
        edtTxtUserNameObservable = buildEditText(edtTxtUserName)
        edtTxtPasswordObservable = buildEditText(edtTxtPinPassword)
        edtTxtPasswordRepeatObservable = buildEditText(edtTxtRepeatPassword)

        disposable = loginViewModel.bindViewStreams(this)

        loginViewModel.moveToNextActivity.observe(this, Observer {
            startActivity<NextActivity>()
        })

        loginViewModel.unableToMoveToNextActivity.observe(this, Observer {
            toast("Unable to move to next activity")
        })

        loginViewModel.emailWasVerified.subscribe {
            edtTxtEmail.reactToEvent(it!!, "Email was not verified")
        }

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

    override fun onDestroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onDestroy()
    }


    private fun EditText.reactToEvent(wasVerified: Boolean, errorMessage: String) {
        if (wasVerified) {
            this.error = null
        } else {
            this.error = errorMessage
        }
    }

    private fun buildEditText(editText: EditText): Observable<Pair<String, Int>> {
        val textChangeObservable: Observable<Pair<String, Int>> = RxTextView.textChangeEvents(editText)
                .debounce(300, TimeUnit.MILLISECONDS)
                .map { textChangedEvents ->
                    textChangedEvents.text().toString() to textChangedEvents.view().id
                }

        val focusChangeObservable: Observable<Pair<String, Int>> = RxView.focusChanges(editText)
                .map { focus ->
                    if (!focus) {
                        editText.text.toString() to editText.id
                    } else {
                        "" to -1
                    }
                }.filter {
                    it.first == "" && it.second == -1
                }

        return Observable.merge(textChangeObservable, focusChangeObservable)
    }
}
