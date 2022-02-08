package com.teufelsturm.tt_downloader_kotlin.results.vm.generics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.teufelsturm.tt_downloader_kotlin.data.order.SortCommentsWithRouteWithSummitBy
import com.teufelsturm.tt_downloader_kotlin.data.order.dialogs.ViewModel4CommentOrder
import com.teufelsturm.tt_downloader_kotlin.util.getOrAwaitValueTest
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ViewModel4CommentOrderDialogTest : TestCase() {

    private lateinit var vm: ViewModel4CommentOrder

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()//For LiveData

    @Before
    public override fun setUp() {
        vm = ViewModel4CommentOrder()
    }

    @Test
    fun `Initially SortCommentsBy is set correctly in the Dialog`() {

        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.Bewertung()))
            .isNotNull()
        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.Bewertung()).isChecked.getOrAwaitValueTest())
            .isFalse()

        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.KommentarDatum()))
            .isNotNull()
        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.KommentarDatum()).isChecked.getOrAwaitValueTest())
            .isTrue()

        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.Benutzer()))
            .isNotNull()
        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.Benutzer()).isChecked.getOrAwaitValueTest())
            .isFalse()

        vm.onClickKommentator()

        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.Bewertung()))
            .isNotNull()
        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.Bewertung()).isChecked.getOrAwaitValueTest())
            .isFalse()

        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.KommentarDatum()))
            .isNotNull()
        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.KommentarDatum()).isChecked.getOrAwaitValueTest())
            .isFalse()

        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.Benutzer()))
            .isNotNull()
        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.Benutzer()).isChecked.getOrAwaitValueTest())
            .isTrue()


        vm.onClickKommentarDatum()

        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.Bewertung()))
            .isNotNull()
        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.Bewertung()).isChecked.getOrAwaitValueTest())
            .isFalse()

        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.KommentarDatum()))
            .isNotNull()
        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.KommentarDatum()).isChecked.getOrAwaitValueTest())
            .isTrue()

        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.Benutzer()))
            .isNotNull()
        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.Benutzer()).isChecked.getOrAwaitValueTest())
            .isFalse()
    }
}