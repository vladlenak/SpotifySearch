package octopus.inc.spotifysearch.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import octopus.inc.spotifysearch.SongListAdapter
import octopus.inc.spotifysearch.api.Item
import octopus.inc.spotifysearch.viewmodel.SearchViewModel
import octopus.inc.spotifysearch.databinding.FragmentSearchBinding
import octopus.inc.spotifysearch.model.Song

class SongListFragment : Fragment(), SearchViewModel.Callbacks, SongListAdapter.Callbacks {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this)[SearchViewModel::class.java]
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
        viewModel.setCallbacks(this)

        binding.searchList.layoutManager = LinearLayoutManager(context)
        binding.searchList.adapter = myAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchEditText.setEndIconOnClickListener {
            viewModel.deleteAllFromDB()
            val userSearch = binding.searchEditText.editText?.text.toString()
            myAdapter.songList.clear()
            viewModel.search(userSearch)
        }

        compositeDisposable.add(viewModel.getSongsFromDB()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                myAdapter.addSongList(it)
            }, {}))
    }

    override fun addSongToAdapter(song: Song) {
        myAdapter.addSong(song)
        viewModel.addSongToDB(song)
    }

    override fun onClickSongItem(song: Song) {
        val action = SongListFragmentDirections.actionSearchFragmentToSearchDetailFragment()
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}