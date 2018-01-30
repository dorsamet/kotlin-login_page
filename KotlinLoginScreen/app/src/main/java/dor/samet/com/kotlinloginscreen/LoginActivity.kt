package dor.samet.com.kotlinloginscreen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dor.samet.com.kotlinloginscreen.business_logic.use_case.UseCase
import dor.samet.com.kotlinloginscreen.business_logic.verification.VerificationUseCase
import dor.samet.com.kotlinloginscreen.widgets.DefaultTextWatcher
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        with(edtTxtName) {
            addTextChangedListener(object : DefaultTextWatcher() {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val useCase: UseCase<String, Boolean> = VerificationUseCase.VerifyUserName()
                    useCase.executeAsync(s.toString()) {
                        if(it) {
                            this@with.error = null
                        } else {
                            this@with.error = "Wrong username"
                        }
                    }
                }
            })
        }
    }
}
