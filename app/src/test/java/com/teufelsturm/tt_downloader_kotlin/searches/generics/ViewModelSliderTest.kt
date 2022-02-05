package com.teufelsturm.tt_downloader_kotlin.searches.generics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.teufelsturm.tt_downloader_kotlin.util.getOrAwaitValueTest

import junit.framework.TestCase

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ViewModelSliderTest : TestCase() {

    lateinit var vm: ViewModelSlider

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()//For access to LiveData


    @Before
    public override fun setUp() {
        vm = ViewModelSlider("TestLabel", 10f, { value -> (value / 2).toString() }, 5f)

    }

    @Test
    fun getLabel() {
        assertThat(vm.label.getOrAwaitValueTest()).isEqualTo("TestLabel 2.5")
    }
}