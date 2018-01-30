package dor.samet.com.kotlinloginscreen.business_logic.use_case

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.Future


interface UseCase<in Request, out Response> {

    /**
     * this call is going to asynchronous!
     */
    fun executeAsync(request: Request, callback: (Response) -> Unit)

    /**
     * this will cancel an ongoing task
     */
    fun cancelTask()

    abstract class BaseUseCase<in Request, out Response>: UseCase<Request, Response> {
        private var job: Future<Unit>? = null

        override fun executeAsync(request: Request, callback: (Response) -> Unit) {
            job = doAsync {
                val response = internalExecute(request)
                // do generic stuff with the response
                // ...
                uiThread {
                    callback(response)
                }
            }
        }

        override fun cancelTask() {
            job?.let {
                unwrappedJob ->
                if (!unwrappedJob.isDone) {
                    unwrappedJob.cancel(true)
                }
            }
        }

        /**
         * This code is going to run synchronously on a different thread!
         */
        protected abstract fun internalExecute(request: Request): Response

    }

}