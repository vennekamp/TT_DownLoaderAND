package com.teufelsturm.tt_downloader_kotlin.feature.results.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.fragment.app.Fragment
import com.davemorrissey.labs.subscaleview.ImageSource
import dagger.hilt.android.AndroidEntryPoint
import de.teufelsturm.tt_downloader_ktx.R
import de.teufelsturm.tt_downloader_ktx.databinding.ResultZoomImageBinding


private const val TAG = "ZoomImageView"

@AndroidEntryPoint
class ZoomImageView : Fragment(R.layout.result_zoom_image) {
    private lateinit var imageSource: ImageSource
    private lateinit var imageUri: Uri
    private lateinit var description: String

    private var _binding: ResultZoomImageBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.explode
        )
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
        // Retrieve the search arguments from the Bundle and initiate the query
        val args = ZoomImageViewArgs.fromBundle(requireArguments())
        imageUri = Uri.parse(args.imageUri)
        imageSource = ImageSource.uri(imageUri)
        description = args.description
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = ResultZoomImageBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.ivImage.setImage(imageSource)
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.zoom_image, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.zoom_image_menu_share -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.putExtra(Intent.EXTRA_TEXT, description)
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                // set type after putExtra to enable image & text
                shareIntent.type = "image/*"
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(Intent.createChooser(shareIntent, "Bild teilen"))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}