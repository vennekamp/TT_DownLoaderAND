package com.teufelsturm.tt_downloader_kotlin.feature.results.adapter

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.helper.widget.Carousel
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import coil.load
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTCommentPhotosAND

private const val TAG = "CommentImageCarouselAdp"

class CommentImageCarouselAdapter : Carousel.Adapter {

    var data: MutableList<MyTTCommentPhotosAND> = mutableListOf()

    override fun count(): Int {
        return data.size
    }

    override fun populate(view: View?, index: Int) {
        Log.e(
            TAG,
            "populate(view: View?, index: Int) for #$index; of unknown type ${view?.let { it::class.simpleName }}"
        )
        when (view) {
            is ConstraintLayout -> {
                view.children.forEach { childView ->
                    Log.v(TAG, " children --> type ${childView.let { it::class.simpleName }}")
                    when (childView) {
                        is ImageView -> {
                            childView.setTag(R.id.TAG_COMMENT_ID, data[index].commentID)
                            Log.e(TAG,"childView.setTag(R.id.TAG_COMMENT_ID, ${data[index].commentID}")
                            childView.setTag(R.id.TAG_PHOTO_ID, data[index].Id
                            )
                            Log.e(TAG,"childView.setTag(R.id.TAG_PHOTO_ID, ${data[index].Id}")
                            childView.load(Uri.parse(data[index].uri)) {
                                Log.v(
                                    TAG,
                                    "populate(view: View?, index: Int) for #$index; of type: IMAGEVIEW - image ${data[index].caption} "
                                )
                                crossfade(true)
                                placeholder(R.drawable.add_image_wait)
                                error(R.drawable.added_image_not_found)
                            }
                        }
                        is TextView -> {
                            Log.v(
                                TAG,
                                "populate(view: View?, index: Int) for #$index; of type: TEXTVIEW --> ${data[index].caption} "
                            )
                            childView.text = data[index].caption
                        }
                    }
                }
            }
            else -> {
                Log.v(TAG, "populate(view: View?, index: Int) for #$index; of unknown type")
            }
        }
    }

    override fun onNewItem(index: Int) {
        Log.v(TAG, "onNewItem(index: Int) für #$index")
    }
}
