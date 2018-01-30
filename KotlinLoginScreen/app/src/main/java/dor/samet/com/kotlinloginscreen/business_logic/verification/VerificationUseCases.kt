package dor.samet.com.kotlinloginscreen.business_logic.verification

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import dor.samet.com.kotlinloginscreen.business_logic.use_case.UseCase
import dor.samet.com.kotlinloginscreen.extensions.Email
import dor.samet.com.kotlinloginscreen.extensions.isEmail

sealed class VerificationUseCase<in Input>: UseCase.BaseUseCase<Input, Boolean>() {

    abstract val predicate: (Input) -> Boolean

    override fun internalExecute(request: Input): Boolean = predicate(request)

    class VerifyUserName: VerificationUseCase<String>() {
        override val predicate: (String) -> Boolean
            get() = {
                it.length > 6
            }
    }

    class VerifyPassword: VerificationUseCase<String>() {
        override val predicate: (String) -> Boolean
            get() = {
                it.length > 6
            }
    }

    class VerifyPasswordsMatch: VerificationUseCase<Pair<String, String>>() {
        override val predicate: (Pair<String, String>) -> Boolean
            get() = {
                it.first == it.second
            }

    }

    class VerifyEmail: VerificationUseCase<String>() {
        override val predicate: (Email) -> Boolean
            get() = {
                it.isEmail()
            }
    }

    class VerifyPhoneNumber(private val phoneNumberUtil: PhoneNumberUtil): VerificationUseCase<String>() {
        override val predicate: (String) -> Boolean
            get() = {
                try {
                    phoneNumberUtil.parse(it, "IL")
                    true
                } catch (e: NumberParseException) { // npe too suspicious :)
                    false
                }
            }
    }

}