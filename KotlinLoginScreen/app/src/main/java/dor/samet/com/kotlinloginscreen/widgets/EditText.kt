package dor.samet.com.kotlinloginscreen.widgets

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

open class DefaultTextWatcher: TextWatcher {
    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}

fun EditText.addTextWatcher(beforeTextChanged: (CharSequence?, Int, Int, Int) -> Unit =
                            { _, _, _, _ ->  },
                            afterTextChanged: (Editable?) -> Unit = {},
                            onTextChanged: (CharSequence?, Int, Int, Int) -> Unit =
                            { _, _, _, _ ->  }) {

    val textWatcher = object: DefaultTextWatcher() {
        override fun afterTextChanged(s: Editable?) = afterTextChanged(s)

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                beforeTextChanged(s, start, count, after)

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                onTextChanged(s, start, before, count)
    }
    this.addTextChangedListener(textWatcher)
}
