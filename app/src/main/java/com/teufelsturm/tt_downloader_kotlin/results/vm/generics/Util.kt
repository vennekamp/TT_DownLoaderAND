package com.teufelsturm.tt_downloader_kotlin.results.vm.generics

import com.teufelsturm.tt_downloader_kotlin.data.order.Order


fun convertBooleanToOrder(isDescending: Boolean) : Order{
    return if (isDescending) Order.Ascending else Order.Descending
}