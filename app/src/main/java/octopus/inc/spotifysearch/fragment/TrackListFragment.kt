package octopus.inc.spotifysearch.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import octopus.inc.spotifysearch.SongListAdapter
import octopus.inc.spotifysearch.databinding.FragmentSearchBinding
import octopus.inc.spotifysearch.db.model.Song
import octopus.inc.spotifysearch.viewmodel.TrackListViewModel

class TrackListFragment : Fragment(), SongListAdapter.Callbacks {
    
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: TrackListViewModel by lazy {
        ViewModelProvider(this)[TrackListViewModel::class.java]
    }
    private lateinit var myAdapter: SongListAdapter
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        myAdapter = SongListAdapter(ArrayList(), requireContext())
        myAdapter.setCallbacks(this)

        binding.searchList.layoutManager = LinearLayoutManager(context)
        binding.searchList.adapter = myAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compositeDisposable.add(viewModel.getSongsFromDB()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                myAdapter.addSongList(it)
            }, {}))

        viewModel.track.observe(viewLifecycleOwner) {
            myAdapter.addSong(it)
            viewModel.addSongToDB(it)
        }

        binding.searchEditText.setEndIconOnClickListener {
            viewModel.deleteAllFromDB()
            val userSearch = binding.searchEditText.editText?.text.toString()
            myAdapter.songList.clear()
            viewModel.search(userSearch, childFragmentManager)
        }
    }

    override fun onClickSongItem(song: Song) {
        val action = TrackListFragmentDirections.actionSearchFragmentToSearchDetailFragment(song.id)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    companion object {
        private const val TAG = "TrackListFragment"
    }
}