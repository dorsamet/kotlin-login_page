package dor.samet.com.kotlinloginscreen.presentation.login_screen

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.IdRes
import android.widget.EditText
import dor.samet.com.kotlinloginscreen.R
import dor.samet.com.kotlinloginscreen.business_logic.VerificationManager
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


class LoginViewModel(private val verificationManager: VerificationManager): ViewModel(),
        VerificationManager.VerificationObserver {

    // Events/LiveData streams
    val namedWasVerified: MutableLiveData<Boolean> = MutableLiveData()
    val userNamedWasVerified: MutableLiveData<Boolean> = MutableLiveData()
    val passwordWasVerified: MutableLiveData<Boolean> = MutableLiveData()
    val passwordRepeatWasVerified: MutableLiveData<Boolean> = MutableLiveData()
    val phoneWasVerified: MutableLiveData<Boolean> = MutableLiveData()
    val moveToNextActivity: MutableLiveData<Void> = MutableLiveData()
    val unableToMoveToNextActivity: MutableLiveData<Void> = MutableLiveData()
    val emailWasVerified: Observable<Boolean> = verificationManager.emailVerified

    init {
        verificationManager += this
    }

    fun bindViewStreams(observableLoginView: ObservableLoginView): Disposable {
        // Example
        // TODO: Expand this!
        val disposable = CompositeDisposable()
        val observable = observableLoginView.edtTxtEmailObservable.map {
            it.first
        }
        verificationManager.subscribeToEmailEvents(observable)
        return disposable
    }


    fun onSubmitClicked(input: List<EditText>) {
        input.map {
            verifySync(it.text.toString(), it.id)
        }.reduce { acc, b ->
                    acc && b
                }.also {
                    if (it) {
                        moveToNextActivity.postValue(null)
                    } else {
                        unableToMoveToNextActivity.postValue(null)
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
            R.id.edtTxtName -> verificationManager.verifyName(inputString)
            R.id.edtTxtPhone -> verificationManager.verifyPhoneNumber(inputString)
            R.id.edtTxtPinPassword -> verificationManager.verifyPinNumber(inputString)
            R.id.edtTxtRepeatPassword -> verificationManager.verifyPinNumbersMatch(inputString)
            R.id.edtTxtUserName -> verificationManager.verifyUserName(inputString)
        }
    }

    override fun onUserNameVerified() = userNamedWasVerified.postValue(true)

    override fun onUserNameVerificationFailed() = userNamedWasVerified.postValue(false)

    override fun onNameVerified() = namedWasVerified.postValue(true)

    override fun onNameVerificationFailed() = namedWasVerified.postValue(false)

    override fun onPinNumberVerified() = passwordWasVerified.postValue(true)

    override fun onPinNumberVerificationFailed() = passwordWasVerified.postValue(false)

    override fun onPhoneNumberVerified() = phoneWasVerified.postValue(true)

    override fun onPhoneNumberVerificationFailed() = phoneWasVerified.postValue(false)

    override fun onPasswordsMatched() = passwordRepeatWasVerified.postValue(true)

    override fun onPasswordMismatchFailure() = passwordRepeatWasVerified.postValue(false)


    class ViewModelFactory(private val verificationManager: VerificationManager):
            ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(verificationManager) as T
            }
            throw IllegalArgumentException("Illegal class! Expected LoginViewModel")
        }

    }

    interface ObservableLoginView {
        var edtTxtNameObservable: Observable<Pair<String, Int>>
        var edtTxtUserNameObservable: Observable<Pair<String, Int>>
        var edtTxtEmailObservable: Observable<Pair<String, Int>>
        var edtTxtPhoneObservable: Observable<Pair<String, Int>>
        var edtTxtPasswordObservable: Observable<Pair<String, Int>>
        var edtTxtPasswordRepeatObservable: Observable<Pair<String, Int>>
        var onClickObservable: Observable<List<Pair<String, Int>>>
    }
}