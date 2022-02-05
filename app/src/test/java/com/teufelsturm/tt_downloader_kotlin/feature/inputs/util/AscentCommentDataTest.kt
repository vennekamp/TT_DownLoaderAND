package com.teufelsturm.tt_downloader_kotlin.feature.inputs.util

import com.google.common.truth.Truth.assertThat
import com.teufelsturm.tt_downloader_kotlin.searches.generics.convertDateTimeStringToLong
import com.teufelsturm.tt_downloader_kotlin.searches.generics.convertLongToDateTimeString


import junit.framework.TestCase

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class AscentCommentDataTest : TestCase() {

    @Test
    fun testDateStringConverter() {
        assertThat(convertDateTimeStringToLong("9.9.1999")).isEqualTo(936831600000L)
        assertThat(convertLongToDateTimeString(936831600000L)).isEqualTo("Do., 9.Sep. 1999 - 01:00")

        assertThat(convertDateTimeStringToLong("1.1.1970")).isEqualTo(0)
        assertThat(convertLongToDateTimeString(0)).isEqualTo("Do., 1.Jan. 1970 - 01:00")

        assertThat(convertDateTimeStringToLong("Di., 01.Feb. 2022")).isEqualTo(1643673600000)
        assertThat(convertLongToDateTimeString(1643673600000)).isEqualTo("Di., 1.Feb. 2022 - 01:00")

        assertThat(convertDateTimeStringToLong("Sa., 01.Jan. 2022")).isEqualTo(1640995200000)
        assertThat(convertLongToDateTimeString(1640995200000)).isEqualTo("Sa., 1.Jan. 2022 - 01:00")
        assertThat(convertDateTimeStringToLong("Sa., 1.Jan. 2022")).isEqualTo(1640995200000)
        assertThat(convertLongToDateTimeString(1640995200000)).isEqualTo("Sa., 1.Jan. 2022 - 01:00")

        assertThat(convertDateTimeStringToLong("Sa., 29.Nov. 1958")).isEqualTo(-350006400000)
        assertThat(convertLongToDateTimeString(-350006400000)).isEqualTo("Sa., 29.Nov. 1958 - 01:00")

        val dateTime0 = convertDateTimeStringToLong("Sa., 29.Nov. 1958 12:23")
        assertThat(dateTime0).isEqualTo(-349965420000)
        assertThat(convertLongToDateTimeString(dateTime0!!)).isEqualTo("Sa., 29.Nov. 1958 - 12:23")

        val dateTime1 = convertDateTimeStringToLong("Sa., 29.Nov. 1958 00:00")
        assertThat(dateTime1).isEqualTo(-350010000000)
        assertThat(convertLongToDateTimeString(dateTime1!!)).isEqualTo("Sa., 29.Nov. 1958 - 00:00")

        val dateTime2 = convertDateTimeStringToLong("Sa., 29.Nov. 1958 23:59")
        assertThat(dateTime2).isEqualTo(-349923660000)
        assertThat(convertLongToDateTimeString(dateTime2!!)).isEqualTo("Sa., 29.Nov. 1958 - 23:59")

        val dateTime3 = convertDateTimeStringToLong("Mo., 15.Juni 2020 - 00:00")
        assertThat(dateTime3).isEqualTo(1592172000000)
        assertThat(convertLongToDateTimeString(dateTime3!!)).isEqualTo("Mo., 15.Juni 2020 - 00:00")

        val dateTime4 = convertDateTimeStringToLong("Mo., 15.Juni 2020 23:59")
        assertThat(dateTime4).isEqualTo(1592258340000)
        assertThat(convertLongToDateTimeString(dateTime4!!)).isEqualTo("Mo., 15.Juni 2020 - 23:59")


        val dateTime5 = convertDateTimeStringToLong("Mo., 15.Juni 2020 - 0:0")
        assertThat(dateTime5).isEqualTo(1592172000000)
        assertThat(convertLongToDateTimeString(dateTime5!!)).isEqualTo("Mo., 15.Juni 2020 - 00:00")

    }

    @Test
    fun swapCarouselItemViewModels() {
        val data = CarouselAdapterData()

        var mutableList = mutableListOf(1, 2, 3, 4, 5, 6, 7)
        val swapped1 = data.swapCarouselItemViewModels(true, mutableList, 3)
        assertThat(swapped1).isEqualTo(3)
        assertThat(mutableList).isEqualTo(mutableListOf(1, 2, 4, 3, 5, 6, 7))

        mutableList = mutableListOf(1, 2, 3, 4, 5, 6, 7)
        val swapped2 = data.swapCarouselItemViewModels(false, mutableList, 3)
        assertThat(swapped2).isEqualTo(1)
        assertThat(mutableList).isEqualTo(mutableListOf(1, 3, 2, 4, 5, 6, 7))

        mutableList = mutableListOf(1, 2, 3, 4, 5, 6, 7)
        val swapped3 = data.swapCarouselItemViewModels(true, mutableList, 1)
        assertThat(swapped3).isEqualTo(1)
        assertThat(mutableList).isEqualTo(mutableListOf(2, 1, 3, 4, 5, 6, 7))

        mutableList = mutableListOf(1, 2, 3, 4, 5, 6, 7)
        val swapped4 = data.swapCarouselItemViewModels(false, mutableList, 2)
        assertThat(swapped4).isEqualTo(0)
        assertThat(mutableList).isEqualTo(mutableListOf(2, 1, 3, 4, 5, 6, 7))

        mutableList = mutableListOf(1, 2, 3, 4, 5, 6, 7)
        val swapped5 = data.swapCarouselItemViewModels(true, mutableList, 6)
        assertThat(swapped5).isEqualTo(null)

        mutableList = mutableListOf(1, 2, 3, 4, 5, 6, 7)
        val swapped6 = data.swapCarouselItemViewModels(true, mutableList, 7)
        assertThat(swapped6).isEqualTo(null)

        mutableList = mutableListOf(1, 2, 3, 4, 5, 6, 7)
        val swapped7 = data.swapCarouselItemViewModels(false, mutableList, 7)
        assertThat(swapped7).isEqualTo(null)

        mutableList = mutableListOf(1, 2, 3, 4, 5, 6, 7)
        val swapped8 = data.swapCarouselItemViewModels(false, mutableList, 17)
        assertThat(swapped8).isEqualTo(null)

        mutableList = mutableListOf(1, 2, 3, 4, 5, 6, 7)
        val swapped9 = data.swapCarouselItemViewModels(false, mutableList, 1)
        assertThat(swapped9).isEqualTo(null)

        mutableList = mutableListOf(1, 2, 3, 4, 5, 6, 7)
        val swapped10 = data.swapCarouselItemViewModels(false, mutableList, 0)
        assertThat(swapped10).isEqualTo(null)
    }
}