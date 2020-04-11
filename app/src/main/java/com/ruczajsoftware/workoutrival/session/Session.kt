package com.ruczajsoftware.workoutrival.session

import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ruczajsoftware.workoutrival.data.db.entity.AuthToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SessionManager(
    private val context: Context
) {
    private val _cachedToken = MutableLiveData<AuthToken>()

    val cachedToken: LiveData<AuthToken>
        get() = _cachedToken

    fun login(newValue: AuthToken) {
        setValue(newValue)
    }

    fun setValue(newValue: AuthToken?) {
        GlobalScope.launch(Dispatchers.Main) {
            _cachedToken.value = newValue
        }
    }

    fun isConnectedToTheInternet(): Boolean {
        val cm =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {
            return cm.activeNetworkInfo.isConnected
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "isConnectedToTheInternet: ${e.message}")
        }
        return false
    }

}