package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun createRandomString(len: Int): String {
    val charPool: List<Char> = listOf(' ') + ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (0..len)
        .map { i -> if (i % 7 == 0) 0 else kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}

fun <T> LiveData<T>.getValueBlocking(): T? {
    var value: T? = null
    val latch = CountDownLatch(1)

    val observer = Observer<T> { t ->
        value = t
        latch.countDown()
    }

    observeForever(observer)

    latch.await(2, TimeUnit.SECONDS)
    return value
}
