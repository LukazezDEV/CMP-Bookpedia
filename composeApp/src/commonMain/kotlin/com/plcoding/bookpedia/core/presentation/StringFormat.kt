package com.plcoding.bookpedia.core.presentation

fun Int.getCommaSeparated() =
    toString().reversed().chunked(3).joinToString(",").reversed()