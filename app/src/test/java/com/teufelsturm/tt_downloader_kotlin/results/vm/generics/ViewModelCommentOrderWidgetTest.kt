package com.teufelsturm.tt_downloader_kotlin.results.vm.generics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.teufelsturm.tt_downloader_kotlin.data.order.SortCommentsWithRouteWithSummitBy
import com.teufelsturm.tt_downloader_kotlin.feature.results.vm.generics.ViewModelCommentOrderWidget
import com.teufelsturm.tt_downloader_kotlin.util.getOrAwaitValueTest
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ViewModelCommentOrderWidgetTest : TestCase() {

    private lateinit var vm: ViewModelCommentOrderWidget

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()//For LiveData

    @Before
    public override fun setUp() {
        vm = ViewModelCommentOrderWidget()
    }

    @Test
    fun `Initially SortCommentsBy is set correctly`() {

        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.WegName()))
            .isNotNull()
        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.WegName()).isChecked.getOrAwaitValueTest())
            .isTrue()

        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.GipfelName()))
            .isNotNull()
        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.GipfelName()).isChecked.getOrAwaitValueTest())
            .isFalse()

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
            .isFalse()

        vm.onClickKommentator()

        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.WegName()))
            .isNotNull()
        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.WegName()).isChecked.getOrAwaitValueTest())
            .isFalse()

        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.GipfelName()))
            .isNotNull()
        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.GipfelName()).isChecked.getOrAwaitValueTest())
            .isFalse()

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

        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.WegName()))
            .isNotNull()
        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.WegName()).isChecked.getOrAwaitValueTest())
            .isFalse()

        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.GipfelName()))
            .isNotNull()
        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.GipfelName()).isChecked.getOrAwaitValueTest())
            .isFalse()

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


        vm.onClickSummitName()

        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.WegName()))
            .isNotNull()
        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.WegName()).isChecked.getOrAwaitValueTest())
            .isFalse()

        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.GipfelName()))
            .isNotNull()
        Truth.assertThat(vm.getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.GipfelName()).isChecked.getOrAwaitValueTest())
            .isTrue()

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
            .isFalse()
    }
}