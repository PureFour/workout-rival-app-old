package com.ruczajsoftware.workoutrival.util

interface DataStateChangeListener {

    fun onDataStateChange(dataState: DataState<*>?)

    fun hideSoftKeyboard()
}