package octopus.inc.spotifysearch.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import octopus.inc.spotifysearch.R
import octopus.inc.spotifysearch.SpotifySearchApplication
import octopus.inc.spotifysearch.activity.LoginActivity
import octopus.inc.spotifysearch.databinding.FragmentSearchDetailBinding
import octopus.inc.spotifysearch.viewmodel.SearchViewModel
import octopus.inc.spotifysearch.viewmodel.SongDetailViewModel

class SongDetailFragment : Fragment() {

    private lateinit var binding: FragmentSearchDetailBinding
    private val viewModel: SongDetailViewModel by lazy {
        ViewModelProvider(this)[SongDetailViewModel::class.java]
    }
    private val args: SongDetailFragmentArgs by navArgs()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchDetailBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compositeDisposable.add(
            SpotifySearchApplication.api.getTrack(LoginActivity.SPOTIFY_ACCESS_TOKEN, args.trackId, "UA")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.songName.text = it.name
                binding.songArtist.text = it.artists[0].name
                binding.songAlbum.text = it.album.name
                loadImageIntoView(binding.songImage, it.album.images.get(0).url)
            }, {}))

//        binding.getTrackButton.setOnClickListener {
//            viewModel.getTrack(args.trackId)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun loadImageIntoView(view: ImageView, url: String) {
        Glide.with(view.context)
            .load(url)
            .into(view)

//        if (url.startsWith("gs://")) {
//            val storageReference = Firebase.storage.getReferenceFromUrl(url)
//            storageReference.downloadUrl
//                .addOnSuccessListener { uri ->
//                    val downloadUrl = uri.toString()
//
//                }
//                .addOnFailureListener { e ->
//                    Log.w(
//                        TAG,
//                        "Getting download url was not successful.",
//                        e
//                    )
//                }
//        } else {
//            Glide.with(view.context).load(url).into(view)
//        }
    }

    companion object {

    }
}