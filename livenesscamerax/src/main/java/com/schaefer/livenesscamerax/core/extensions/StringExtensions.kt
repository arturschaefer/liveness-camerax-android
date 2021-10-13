package com.schaefer.livenesscamerax.core.extensions

private const val FILE_NAME_INDEX = 2
private const val DELIMITER_PATH = "/"
private const val DELIMITER_FILE = "."

internal fun String.getFileNameWithoutExtension(): String {
    return this.split(DELIMITER_PATH, DELIMITER_FILE)
        .let { splitList -> splitList[splitList.size - FILE_NAME_INDEX] }
}
