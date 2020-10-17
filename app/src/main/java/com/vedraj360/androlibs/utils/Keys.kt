package com.vedraj360.androlibs.utils

/*
object Keys {
    init {
        System.loadLibrary("keys")
    }

    private external fun getKey(): String?

    fun readKey(): String? {
        Log.e("TAG", "readFirstKey: ${getKey()}")
        return getKey()
    }
}

*/

object Keys {

//    init {
//        System.loadLibrary("native-lib")
//    }

    private external fun getKey(): String

    fun readKey(): String? {
//        Log.e("TAG", "readFirstKey: ${getKey()}")
        return getKey()
    }
}
