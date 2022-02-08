package com.teufelsturm.tt_downloader_kotlin.results.adapter

import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CommentImageCarouselAdapterTest : TestCase() {

    @Test
    fun populate() {
        (0..100 step 2).forEach {
            assertThat(it / 2 ).isEqualTo(it /2 )
            assertThat((it.plus(1)) / 2 ).isEqualTo(it /2 )
        }
    }
}