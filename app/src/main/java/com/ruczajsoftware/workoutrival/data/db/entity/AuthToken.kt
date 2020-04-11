package com.ruczajsoftware.workoutrival.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

const val CURRENT_TOKEN_INDEX = 0

@Parcelize
@Entity(tableName = "auth_token")
data class AuthToken(
    @SerializedName("token")
    var token: String? = null
) : Parcelable {
    @PrimaryKey(autoGenerate = false)
    var index: Int = CURRENT_TOKEN_INDEX
}