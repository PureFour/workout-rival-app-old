package com.ruczajsoftware.workoutrival.util.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.ruczajsoftware.workoutrival.session.SessionManagerImpl
import com.ruczajsoftware.workoutrival.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein


abstract class BaseActivity : AppCompatActivity(), KodeinAware,
    DataStateChangeListener,
    UICommunicationListener {

    val TAG: String = "AppDebug"
    override val kodein by closestKodein()

    protected abstract val contentViewLayout: Int

    var sessionManager: SessionManagerImpl = SessionManagerImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentViewLayout)
        afterViews()
    }


    override fun onUIMessageReceived(uiMessage: UIMessage) {
        when (uiMessage.uiMessageType) {

            is UIMessageType.AreYouSureDialog -> {
                areYouSureDialog(
                    uiMessage.message,
                    uiMessage.uiMessageType.callback
                )
            }

            is UIMessageType.Toast -> {
                displayToast(uiMessage.message)
            }

            is UIMessageType.Dialog -> {
                displayInfoDialog(uiMessage.message)
            }

            is UIMessageType.None -> {
                Log.i(TAG, "onUIMessageReceived: ${uiMessage.message}")
            }
        }
    }

    override fun onDataStateChange(dataState: DataState<*>?) {
        dataState?.let {
            GlobalScope.launch(Dispatchers.Main) {
                displayProgressBar(it.loading.isLoading)

                it.error?.let { errorEvent ->
                    handleStateError(errorEvent)
                }

                it.data?.let {
                    it.response?.let { responseEvent ->
                        handleStateResponse(responseEvent)
                    }
                }
            }
        }
    }

    abstract fun displayProgressBar(bool: Boolean)

    private fun handleStateResponse(event: Event<Response>) {
        event.getContentIfNotHandled()?.let {

            when (it.responseType) {
                is ResponseType.Toast -> {
                    it.message?.let { message ->
                        displayToast(message)
                    }
                }

                is ResponseType.Dialog -> {
                    it.message?.let { message ->
                        displaySuccessDialog(message)
                    }
                }

                is ResponseType.None -> {
                    Log.i(TAG, "handleStateResponse: ${it.message}")
                }
            }

        }
    }

    private fun handleStateError(event: Event<StateError>) {
        event.getContentIfNotHandled()?.let {
            when (it.response.responseType) {
                is ResponseType.Toast -> {
                    it.response.message?.let { message ->
                        displayToast(message)
                    }
                }

                is ResponseType.Dialog -> {
                    it.response.message?.let { message ->
                        displayErrorDialog(message)
                    }
                }

                is ResponseType.None -> {
                    Log.i(TAG, "handleStateError: ${it.response.message}")
                }
            }
        }
    }

    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager
                .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    protected abstract fun afterViews()
}