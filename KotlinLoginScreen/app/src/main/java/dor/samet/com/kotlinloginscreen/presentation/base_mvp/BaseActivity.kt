package dor.samet.com.rest.presentation.base_mvp

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import dor.samet.com.kotlinloginscreen.KotlinLoginApplication
import org.jetbrains.anko.toast
import javax.inject.Inject


@SuppressLint("Registered")
@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<VIEW: MvpView, PRESENTER: BasePresenter<VIEW>>: AppCompatActivity(), MvpView {

    @Inject protected lateinit var presenter: PRESENTER

    protected fun onCreate(savedInstanceState: Bundle?, block: (Bundle?) -> Unit) {
        super.onCreate(savedInstanceState)
        block(savedInstanceState)
    }

    protected fun onStart(block: () -> Unit) {
        super.onStart()
        block()
        presenter.attachView(this as VIEW)
    }

    protected fun onStop(block: () -> Unit) {
        presenter.detachView()
        block()
        super.onStop()
    }

    protected fun onDestroy(block: () -> Unit) {
        presenter.detachView()
        block()
        super.onDestroy()
    }

    override fun showToast(message: String) {
        toast(message)
    }
}