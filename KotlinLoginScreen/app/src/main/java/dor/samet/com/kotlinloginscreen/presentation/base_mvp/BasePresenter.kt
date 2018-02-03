package dor.samet.com.rest.presentation.base_mvp

import android.support.annotation.VisibleForTesting
import java.lang.ref.WeakReference


open class BasePresenter<VIEW: MvpView> {

    @VisibleForTesting private lateinit var _viewWeakRef: WeakReference<VIEW>

    private val _view: VIEW?
        get() = if (_viewWeakRef.get() != null) _viewWeakRef.get() else null


    fun applyOnView(block: VIEW.() -> Unit) = _view?.apply { block() }

    open internal fun attachView(view: VIEW) {
        this._viewWeakRef = WeakReference(view)
    }

    open internal fun detachView() {
        _viewWeakRef.clear()
    }
}