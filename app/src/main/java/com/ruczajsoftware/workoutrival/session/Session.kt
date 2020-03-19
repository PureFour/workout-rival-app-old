package com.ruczajsoftware.workoutrival.session


interface SessionManager {
    fun login(newValue: Boolean)
    fun setValue(newValue: Boolean?)
}