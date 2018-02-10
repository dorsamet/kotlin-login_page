package dor.samet.com.kotlinloginscreen.business_logic.observable


interface Observable<OBSERVER> {

    val observers: Set<OBSERVER>

    operator fun plus(observer: OBSERVER)

    operator fun plusAssign(observer: OBSERVER)

    operator fun minus(observer: OBSERVER)

    operator fun minusAssign(observer: OBSERVER)

    fun <RESPONSE> notifyObservers(notify: (observer: OBSERVER) -> RESPONSE)

}

open class BaseObservable<OBSERVER>: Observable<OBSERVER> {

    private var _observers: MutableSet<OBSERVER> = mutableSetOf()
    override val observers: Set<OBSERVER>
        get() = _observers.toSet()

    override operator fun plus(observer: OBSERVER) {
        this += observer
    }

    override infix operator fun plusAssign(observer: OBSERVER) {
        _observers.add(observer)
    }

    override operator fun minus(observer: OBSERVER) {
        this -= observer
    }

    override operator fun minusAssign(observer: OBSERVER) {
        _observers.remove(observer)
    }

    override fun <RESPONSE> notifyObservers(notify: (observer: OBSERVER) -> RESPONSE) {
        observers.forEach { notify(it) }
    }
}
