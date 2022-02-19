package com.teufelsturm.tt_downloader_kotlin.data.order

import com.teufelsturm.tt_downloader_kotlin.data.entity.CommentsSummit
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.RouteGrade

/**
 * Ordering for [com.teufelsturm.tt_downloader_kotlin.data.entity.TTSummitAND]
 */
sealed class SortSummitWithMySummitCommentBy(var order: Order?) {
    class Name(order: Order? = Order.Ascending) : SortSummitWithMySummitCommentBy(order)
    class Gebiet(order: Order? = Order.Ascending) : SortSummitWithMySummitCommentBy(order)
    class GipfelNr(order: Order? = Order.Ascending) : SortSummitWithMySummitCommentBy(order)
    class AnzahlWege(order: Order? = Order.Ascending) : SortSummitWithMySummitCommentBy(order)
    class AnzahlSternchenWege(order: Order? = Order.Ascending) : SortSummitWithMySummitCommentBy(order)
    class LeichtesterWeg(order: Order? = Order.Ascending) : SortSummitWithMySummitCommentBy(order)

    fun copy(order: Order): SortSummitWithMySummitCommentBy {
        return when (this) {
            is Name -> Name(order)
            is Gebiet -> Gebiet(order)
            is GipfelNr -> GipfelNr(order)
            is AnzahlWege -> AnzahlWege(order)
            is AnzahlSternchenWege -> AnzahlSternchenWege(order)
            is LeichtesterWeg -> LeichtesterWeg(order)
        }
    }
}


fun List<CommentsSummit.SummitWithMySummitComment>.sortRoutesBy(
    sortSummitWithMySummitCommentBy: SortSummitWithMySummitCommentBy? = SortSummitWithMySummitCommentBy.GipfelNr(
        Order.Ascending
    )
): List<CommentsSummit.SummitWithMySummitComment> =
    when (sortSummitWithMySummitCommentBy?.order) {
        is Order.Descending -> {
            when (sortSummitWithMySummitCommentBy) {
                is SortSummitWithMySummitCommentBy.Name -> this.sortedByDescending { it.ttSummitAND.strName?.lowercase() }
                is SortSummitWithMySummitCommentBy.AnzahlSternchenWege -> this.sortedByDescending { it.ttSummitAND.intAnzahlSternchenWege }
                is SortSummitWithMySummitCommentBy.AnzahlWege -> this.sortedByDescending { it.ttSummitAND.intAnzahlWege }
                is SortSummitWithMySummitCommentBy.Gebiet -> this.sortedByDescending { it.ttSummitAND.strGebiet?.lowercase() }
                is SortSummitWithMySummitCommentBy.GipfelNr -> this.sortedWith(comparatorGipfelNrDescending()                )
                is SortSummitWithMySummitCommentBy.LeichtesterWeg -> this.sortedByDescending {
                    comperatorGradeByStrig(it)
                }
            }
        }
        else /* is Order.Ascending || is null */ -> {
            when (sortSummitWithMySummitCommentBy) {
                is SortSummitWithMySummitCommentBy.Name -> this.sortedBy { it.ttSummitAND.strName?.lowercase() }
                is SortSummitWithMySummitCommentBy.AnzahlSternchenWege -> this.sortedBy { it.ttSummitAND.intAnzahlSternchenWege }
                is SortSummitWithMySummitCommentBy.AnzahlWege -> this.sortedBy { it.ttSummitAND.intAnzahlWege }
                is SortSummitWithMySummitCommentBy.Gebiet -> this.sortedBy { it.ttSummitAND.strGebiet?.lowercase() }
                is SortSummitWithMySummitCommentBy.GipfelNr -> this.sortedWith(comparatorGipfelr())
                is SortSummitWithMySummitCommentBy.LeichtesterWeg -> this.sortedBy {comperatorGradeByStrig(it)}
                null -> this
            }
        }
    }

private fun comparatorGipfelr(): Comparator<CommentsSummit.SummitWithMySummitComment> =
    compareBy(
        { it.ttSummitAND.strGebiet?.lowercase() },
        { it.ttSummitAND.intKleFuGipfelNr })

private fun comperatorGradeByStrig(it: CommentsSummit.SummitWithMySummitComment) =
    RouteGrade.getRouteGradeByString(
        it.ttSummitAND.strLeichtesterWeg
    )?.ordinal ?: -1

private fun comparatorGipfelNrDescending() =
    compareByDescending<CommentsSummit.SummitWithMySummitComment> { it.ttSummitAND.strGebiet?.lowercase() }.thenByDescending { it.ttSummitAND.intKleFuGipfelNr }
