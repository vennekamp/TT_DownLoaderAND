package com.teufelsturm.tt_downloader_kotlin.results.vm.generics

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.teufelsturm.tt_downloader_kotlin.data.order.SortSummitWithMySummitCommentBy
import com.teufelsturm.tt_downloader_kotlin.feature.results.vm.generics.ViewModelSummitOrderWidget
import com.teufelsturm.tt_downloader_kotlin.util.getOrAwaitValueTest
import junit.framework.TestCase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ViewModelSummitOrderWidgetTest : TestCase() {

    private lateinit var vm: ViewModelSummitOrderWidget

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()//For LiveData

    @Before
    public override fun setUp() {
        vm = ViewModelSummitOrderWidget()
    }

    @Test
    fun `Toggle visibilty is working correctly`() {
        //vm = ViewModelSummitOrderWidget()
        val vis = runBlocking { vm.visibility.firstOrNull() }
        assertThat(vis).isEqualTo(View.INVISIBLE)
        vm.startAnimation()
        assertThat(vm.futureVisibility.getOrAwaitValueTest()).isEqualTo(View.VISIBLE)

    }


    @Test
    fun `Initially SortSummitBy is set correctly`() {

        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.Name())).isNotNull()
        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.Name())!!.isChecked.getOrAwaitValueTest()).isFalse()


        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.GipfelNr())).isNotNull()
        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.GipfelNr())!!.isChecked.getOrAwaitValueTest()).isTrue()


        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.Gebiet())).isNotNull()
        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.Gebiet())!!.isChecked.getOrAwaitValueTest()).isFalse()


        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.AnzahlSternchenWege())).isNotNull()
        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.AnzahlSternchenWege())!!.isChecked.getOrAwaitValueTest()).isFalse()


        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.AnzahlWege())).isNotNull()
        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.AnzahlWege())!!.isChecked.getOrAwaitValueTest()).isFalse()


        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.LeichtesterWeg())).isNotNull()
        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.LeichtesterWeg())!!.isChecked.getOrAwaitValueTest()).isFalse()

        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.LeichtesterWeg())!!.isChecked.getOrAwaitValueTest()).isFalse()

        vm.onClick(SortSummitWithMySummitCommentBy.LeichtesterWeg())

        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.Name())).isNotNull()
        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.Name())!!.isChecked.getOrAwaitValueTest()).isFalse()


        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.GipfelNr())).isNotNull()
        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.GipfelNr())!!.isChecked.getOrAwaitValueTest()).isFalse()


        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.Gebiet())).isNotNull()
        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.Gebiet())!!.isChecked.getOrAwaitValueTest()).isFalse()


        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.AnzahlSternchenWege())).isNotNull()
        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.AnzahlSternchenWege())!!.isChecked.getOrAwaitValueTest()).isFalse()


        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.AnzahlWege())).isNotNull()
        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.AnzahlWege())!!.isChecked.getOrAwaitValueTest()).isFalse()


        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.LeichtesterWeg())).isNotNull()
        assertThat(vm.getViewModelRadioButton(SortSummitWithMySummitCommentBy.LeichtesterWeg())!!.isChecked.getOrAwaitValueTest()).isTrue()

    }


}