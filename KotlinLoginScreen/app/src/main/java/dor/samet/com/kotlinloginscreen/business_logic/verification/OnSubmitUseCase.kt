package dor.samet.com.kotlinloginscreen.business_logic.verification

import dor.samet.com.kotlinloginscreen.business_logic.use_case.UseCase


class OnSubmitUseCase: UseCase.BaseUseCase<Collection<Boolean>, Boolean>() {

    override fun internalExecute(request: Collection<Boolean>): Boolean {
        return request.reduce { acc, b -> acc && b }
    }

}