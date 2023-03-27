package com.example.imdb_application.data.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.*

var onProcess : Job? = null

fun onKeywordValueChange(
    view : EditText,
    debounceTime : Long = 1000,
    callback: (keyword: String) -> Unit) {

    view.addTextChangedListener(object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(editable: Editable?) {

            if(editable.isNullOrEmpty().not()) {
                val input = editable.toString()
                onProcess?.cancel()
                onProcess = CoroutineScope(Dispatchers.Main).launch {
                    delay(debounceTime)
                    callback.invoke(input)
                }

            }
        }

    })
}