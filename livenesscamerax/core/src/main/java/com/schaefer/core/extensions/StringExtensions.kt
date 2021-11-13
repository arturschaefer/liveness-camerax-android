package com.schaefer.core.extensions

import android.util.Base64
import java.io.File

private const val FILE_NAME_INDEX = 2
private const val DELIMITER_PATH = "/"
private const val DELIMITER_FILE = "."

fun String.getFileNameWithoutExtension(): String {
    return this.split(DELIMITER_PATH, DELIMITER_FILE)
        .let { splitList -> splitList[splitList.size - FILE_NAME_INDEX] }
}

fun String.encoderFilePath(): String {
    val bytes = File(this).readBytes()
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}
