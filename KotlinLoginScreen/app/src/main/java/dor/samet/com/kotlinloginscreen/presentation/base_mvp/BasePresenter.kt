package dor.samet.com.rest.presentation.base_mvp

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.annotation.VisibleForTesting
import java.lang.ref.WeakReference
import android.arch.lifecycle.LifecycleOwner




open class BasePresenter<VIEW: MvpView>: LifecycleObserver {

    @VisibleForTesting private lateinit var _viewWeakRef: WeakReference<VIEW>

    private val _view: VIEW?
        get() = if (_viewWeakRef.get() != null) _viewWeakRef.get() else null


    fun applyOnView(block: VIEW.() -> Unit) = _view?.apply { block() }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onLifecycleStarted(source: LifecycleOwner) {
        attachView(source as VIEW)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onLifecycleStopped(source: LifecycleOwner) {
        detachView()
    }


    internal open fun attachView(view: VIEW) {
        this._viewWeakRef = WeakReference(view)
    }

    internal open fun detachView() {
        _viewWeakRef.clear()
    }
}