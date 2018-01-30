package dor.samet.com.kotlinloginscreen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dor.samet.com.kotlinloginscreen.business_logic.use_case.UseCase
import dor.samet.com.kotlinloginscreen.business_logic.verification.VerificationUseCase
import dor.samet.com.kotlinloginscreen.widgets.DefaultTextWatcher
import dor.samet.com.kotlinloginscreen.widgets.addTextWatcher
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        with(edtTxtName) {
            addTextWatcher(
                    onTextChanged = {
                        s: CharSequence?, _: Int, _: Int, _: Int ->
                        val useCase: UseCase<String, Boolean> = VerificationUseCase.VerifyUserName()
                        useCase.executeAsync(s.toString()) {
                            if(it) {
                                this@with.error = null
                            } else {
                                this@with.error = "Wrong username"
                            }
                        }
                    })
        }
    }
}
