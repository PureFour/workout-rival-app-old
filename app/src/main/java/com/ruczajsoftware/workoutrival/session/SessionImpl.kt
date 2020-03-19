package com.ruczajsoftware.workoutrival.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SessionManagerImpl() : SessionManager {

    private val _cachedToken = MutableLiveData<Boolean>()

    val cachedToken: LiveData<Boolean>
        get() = _cachedToken

    override fun login(newValue: Boolean) {
        setValue(newValue)
    }

    override fun setValue(newValue: Boolean?) {
        GlobalScope.launch(Dispatchers.Main) {
            _cachedToken.value = newValue

        }
    }
}
