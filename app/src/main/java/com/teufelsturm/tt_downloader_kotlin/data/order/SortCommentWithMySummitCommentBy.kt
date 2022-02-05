package com.teufelsturm.tt_downloader_kotlin.data.order

import com.teufelsturm.tt_downloader_kotlin.data.entity.CommentInterface
import com.teufelsturm.tt_downloader_kotlin.data.entity.CommentsWithRouteWithSummit

/**
 * Ordering for [com.teufelsturm.tt_downloader_kotlin.data.entity.TTSummitAND]
 */
sealed class SortCommentsWithRouteWithSummitBy(var order: Order?) {
    class WegName(order: Order? = Order.Ascending) : SortCommentsWithRouteWithSummitBy(order)
    class GipfelName(order: Order? = Order.Ascending) : SortCommentsWithRouteWithSummitBy(order)
    class Bewertung(order: Order? = Order.Ascending) : SortCommentsWithRouteWithSummitBy(order)
    class Benutzer(order: Order? = Order.Ascending) : SortCommentsWithRouteWithSummitBy(order)
    class KommentarDatum(order: Order? = Order.Ascending) : SortCommentsWithRouteWithSummitBy(order)

    fun copy(order: Order): SortCommentsWithRouteWithSummitBy {
        return when (this) {
            is WegName -> WegName(order)
            is GipfelName -> GipfelName(order)
            is Bewertung -> Bewertung(order)
            is Benutzer -> Benutzer(order)
            is KommentarDatum -> KommentarDatum(order)
        }
    }
}


fun <E: CommentInterface> List<E>.sortCommentsBy(
    sortCommentBy: SortCommentsWithRouteWithSummitBy? = SortCommentsWithRouteWithSummitBy.GipfelName(
        Order.Ascending
    )
): List<E> =
    when (sortCommentBy?.order) {
        is Order.Descending -> {
            when (sortCommentBy) {
                is SortCommentsWithRouteWithSummitBy.Bewertung -> this.sortedByDescending { it.entryBewertung  }
                is SortCommentsWithRouteWithSummitBy.Benutzer -> this.sortedByDescending { it.strEntryUser?.lowercase() }
                is SortCommentsWithRouteWithSummitBy.KommentarDatum -> this.sortedByDescending { it.entryDatum }
                else -> this
            }
        }
        else /* is Order.Ascending || is null */ -> {
            when (sortCommentBy) {
                is SortCommentsWithRouteWithSummitBy.Bewertung -> this.sortedBy { it.entryBewertung  }
                is SortCommentsWithRouteWithSummitBy.Benutzer -> this.sortedBy { it.strEntryUser?.lowercase() }
                is SortCommentsWithRouteWithSummitBy.KommentarDatum -> this.sortedBy { it.entryDatum }
                else -> this
            }
        }
    }

fun <E: CommentsWithRouteWithSummit> List<E>.sortCommentsWithRouteSummitBy(
    sortCommentBy: SortCommentsWithRouteWithSummitBy? = SortCommentsWithRouteWithSummitBy.GipfelName(
        Order.Ascending
    )
): List<E> =
    when (sortCommentBy?.order) {
        is Order.Descending -> {
            when (sortCommentBy) {
                is SortCommentsWithRouteWithSummitBy.WegName -> this.sortedByDescending { it.WegName?.lowercase() }
                is SortCommentsWithRouteWithSummitBy.GipfelName -> this.sortedWith ( comparatorGipfelNameDescending() )
                else -> this.sortCommentsBy(sortCommentBy)
                }
            }
            else /* is Order.Ascending || is null */ -> {
                when (sortCommentBy) {
                    is SortCommentsWithRouteWithSummitBy.WegName -> this.sortedBy { it.WegName?.lowercase() }
                    is SortCommentsWithRouteWithSummitBy.GipfelName -> this.sortedWith ( comparatorGipfelName() )
                    else -> this.sortCommentsBy(sortCommentBy)
                }
            }
        }

private fun comparatorGipfelName() =
    compareBy<CommentsWithRouteWithSummit> { it.strName?.lowercase() }.thenBy { it.WegName }

private fun comparatorGipfelNameDescending() =
    compareByDescending<CommentsWithRouteWithSummit> { it.strName?.lowercase() }.thenByDescending { it.WegName }
