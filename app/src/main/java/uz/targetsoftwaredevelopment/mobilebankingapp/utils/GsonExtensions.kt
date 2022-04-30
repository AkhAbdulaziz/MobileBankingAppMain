package uz.targetsoftwaredevelopment.mobilebankingapp.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

val gson = Gson()

inline fun <reified T> String.parseTo(): T {
    val type = object : TypeToken<T>() {}.type
    return gson.fromJson<T>(this, type)
}

fun <T> T.toJson(): String {
    return gson.toJson(this)
}