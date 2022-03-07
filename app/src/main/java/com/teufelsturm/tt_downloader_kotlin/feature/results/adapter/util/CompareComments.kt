package com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util

import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments

class CompareComments {
    companion object : Comparator<Comments> {
        override fun compare(a: Comments, b: Comments): Int {
            when (a) {
                is Comments.AddComment -> Int.MIN_VALUE
                is Comments.MyTTCommentANDWithPhotos -> throw IllegalArgumentException("Argument not expected.")
                is Comments.RouteWithMyComment ->  throw IllegalArgumentException("Argument not expected.")
                is Comments.RouteWithMyTTCommentANDWithPhotos -> {
                    if (b is Comments.RouteWithMyTTCommentANDWithPhotos) {
                        if (b.ttRouteAND == null && a.ttRouteAND != null) {
                            return Int.MAX_VALUE
                        } else if (b.ttRouteAND != null && a.ttRouteAND == null) {
                            return Int.MIN_VALUE + 1
                        } else if (b.ttRouteAND == null && a.ttRouteAND == null) {
                            return a.myTTCommentAND.myTTCommentAND.myCommentTimStamp
                                .minus(b.myTTCommentAND.myTTCommentAND.myCommentTimStamp).toInt()
                        } else if (b.ttRouteAND != null && a.ttRouteAND != null) {
                            return a.ttRouteAND!!.WegName?.compareTo(b.ttRouteAND!!.WegName ?: "")!!
                        }
                    } else return 0
                }
                is Comments.TTCommentAND ->  throw IllegalArgumentException("Argument not expected.")
            }
            throw IllegalArgumentException("Argument not expected: a=${a.javaClass}; b=${b.javaClass}")
        }
    }
}
