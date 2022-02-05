package com.teufelsturm.tt_downloader_kotlin.data.order

sealed class Order {
    object Ascending: Order()
    object Descending: Order()
}
