package com.teufelsturm.tt_downloader_kotlin.data.order

import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteWithMyCommentWithSummit
import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteWithMyRouteCommentInterface

/**
 * Ordering for [com.teufelsturm.tt_downloader_kotlin.data.entity.RouteWithMyRouteComment]
 */
sealed class SortRouteWithMyRouteCommentBy(open var order: Order?) {
    class Name(order: Order? = Order.Ascending) : SortRouteWithMyRouteCommentBy(order)
    class Grade(order: Order? = Order.Ascending) : SortRouteWithMyRouteCommentBy(order)
    class CommentCount(order: Order? = Order.Ascending) : SortRouteWithMyRouteCommentBy(order)
    class MeanRating(order: Order? = Order.Ascending) : SortRouteWithMyRouteCommentBy(order)
    class Stars(order: Order? = Order.Ascending) : SortRouteWithMyRouteCommentBy(order)
    class MyAscend(order: Order? = Order.Ascending) : SortRouteWithMyRouteCommentBy(order)
    class Summit(order: Order? = Order.Ascending) : SortRouteWithMyRouteCommentBy(order)
    class Area(order: Order? = Order.Ascending) : SortRouteWithMyRouteCommentBy(order)


    open fun copy(order: Order): SortRouteWithMyRouteCommentBy {
        return when (this) {
            is Name -> Name(order)
            is Grade -> Grade(order)
            is CommentCount -> CommentCount(order)
            is MeanRating -> MeanRating(order)
            is Stars -> Stars(order)
            is MyAscend -> MyAscend(order)
            is Area -> Area(order)
            is Summit -> Summit(order)
        }
    }
}

fun <E : RouteWithMyRouteCommentInterface> List<E>.sortRoutesBy(
    sortRouteWithMyRouteCommentBy: SortRouteWithMyRouteCommentBy? = SortRouteWithMyRouteCommentBy.Name(
        Order.Ascending
    )
): List<E> =
    when (sortRouteWithMyRouteCommentBy?.order) {
        is Order.Descending -> {
            when (sortRouteWithMyRouteCommentBy) {
                is SortRouteWithMyRouteCommentBy.Name -> this.sortedByDescending { it.ttRouteAND.WegName?.lowercase() }
                is SortRouteWithMyRouteCommentBy.CommentCount -> this.sortedByDescending { it.ttRouteAND.intAnzahlDerKommentare }
                is SortRouteWithMyRouteCommentBy.MeanRating -> this.sortedByDescending { it.ttRouteAND.fltMittlereWegBewertung }
                is SortRouteWithMyRouteCommentBy.Grade -> this.sortedByDescending { getMaxGrade(it) }
                is SortRouteWithMyRouteCommentBy.MyAscend -> this.sortedWith(
                    comparatorCommentDescending()
                )
                is SortRouteWithMyRouteCommentBy.Stars -> this.sortedByDescending { it.ttRouteAND.intSterne }
                else -> this
            }
        }
        else /* is Order.Ascending || is null */ -> {
            when (sortRouteWithMyRouteCommentBy) {
                is SortRouteWithMyRouteCommentBy.Name -> this.sortedBy { it.ttRouteAND.WegName?.lowercase() }
                is SortRouteWithMyRouteCommentBy.CommentCount -> this.sortedBy { it.ttRouteAND.intAnzahlDerKommentare }
                is SortRouteWithMyRouteCommentBy.MeanRating -> this.sortedBy { it.ttRouteAND.fltMittlereWegBewertung }
                is SortRouteWithMyRouteCommentBy.Grade -> this.sortedBy { getMaxGrade(it) }
                is SortRouteWithMyRouteCommentBy.MyAscend -> this.sortedWith(comparatorComment())
                is SortRouteWithMyRouteCommentBy.Stars -> this.sortedBy { it.ttRouteAND.intSterne }
                else -> this
            }
        }
    }


fun <E : RouteWithMyCommentWithSummit> List<E>.sortRoutesWithSummitBy(
    sortRouteWithMyRouteCommentBy: SortRouteWithMyRouteCommentBy? = SortRouteWithMyRouteCommentBy.Name(
        Order.Ascending
    )
): List<E> =
    when (sortRouteWithMyRouteCommentBy?.order) {
        is Order.Descending -> {
            when (sortRouteWithMyRouteCommentBy) {
                is SortRouteWithMyRouteCommentBy.Area -> this.sortedByDescending { it.ttSummitAND.strGebiet?.lowercase() }
                is SortRouteWithMyRouteCommentBy.Summit -> this.sortedByDescending { it.ttSummitAND.strName?.lowercase() }
                else -> this.sortRoutesBy( sortRouteWithMyRouteCommentBy)
            }
        }
        else /* is Order.Ascending || is null */ -> {
            when (sortRouteWithMyRouteCommentBy) {
                is SortRouteWithMyRouteCommentBy.Area -> this.sortedBy { it.ttSummitAND.strGebiet?.lowercase() }
                is SortRouteWithMyRouteCommentBy.Summit -> this.sortedBy { it.ttSummitAND.strName?.lowercase() }
                else -> this.sortRoutesBy( sortRouteWithMyRouteCommentBy)
            }
        }
    }


private fun comparatorCommentDescending() = compareByDescending<RouteWithMyRouteCommentInterface> {
    it.myTTCommentANDList.firstOrNull()?.myIntDateOfAscend
}
    .thenByDescending {
        val letterCount = 0
        it.myTTCommentANDList.forEach {
            letterCount.plus(it.strMyComment?.length ?: 0)
        }
        letterCount
    }

private fun comparatorComment() = compareBy<RouteWithMyRouteCommentInterface> {
    it.myTTCommentANDList.firstOrNull()?.myIntDateOfAscend
}
    .thenBy {
        val letterCount = 0
        it.myTTCommentANDList.forEach {
            letterCount.plus(it.strMyComment?.length ?: 0)
        }
        letterCount
    }

private fun getMaxGrade(it: RouteWithMyRouteCommentInterface) =
    maxOf(
        it.ttRouteAND.sachsenSchwierigkeitsGrad ?: 0,
        it.ttRouteAND.rotPunktSchwierigkeitsGrad ?: 0,
        it.ttRouteAND.ohneUnterstuetzungSchwierigkeitsGrad ?: 0,
        it.ttRouteAND.intSprungSchwierigkeitsGrad ?: 0,
    )
