package common

fun String.splitAtIndex(index: Int) = take(index) to substring(index)

fun String.toBinary(): String = toLong().toString(2).padStart(36, '0')