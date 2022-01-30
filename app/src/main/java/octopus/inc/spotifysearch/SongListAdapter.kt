package octopus.inc.spotifysearch

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import octopus.inc.spotifysearch.databinding.ListItemSongBinding
import octopus.inc.spotifysearch.db.model.Song

class SongListAdapter(var songList: ArrayList<Song>, val context: Context) :
    RecyclerView.Adapter<SongListAdapter.SongViewHolder>() {

    private var callbacks: Callbacks? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_song, parent, false)
        val binding = ListItemSongBinding.bind(view)

        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songList[position])
    }

    override fun getItemCount() = songList.size

    fun setCallbacks(callbacks: Callbacks) {
        this.callbacks = callbacks
    }

    fun addSong(song: Song) {
        songList.add(song)
        notifyDataSetChanged()
    }

    fun addSongList(newSongList: List<Song>) {
        songList.clear()
        songList.addAll(newSongList)
        notifyDataSetChanged()
    }

    interface Callbacks {
        fun onClickSongItem(song: Song)
    }

    inner class SongViewHolder(private val binding: ListItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
            binding.song = song
            val flow = song.flow

            if (flow == 1) {
                binding.imageView.setBackgroundResource(R.drawable.ic_looks_one)
                binding.constrainContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.color1))
            } else if (flow == 2) {
                binding.imageView.setBackgroundResource(R.drawable.ic_looks_two)
                binding.constrainContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.color2))
            } else if (flow == 3) {
                binding.imageView.setBackgroundResource(R.drawable.ic_looks_3)
                binding.constrainContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.color3))
            } else if (flow == 4) {
                binding.imageView.setBackgroundResource(R.drawable.ic_looks_4)
                binding.constrainContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.color4))
            }



            binding.card.setOnClickListener {
                callbacks?.onClickSongItem(song)
            }
        }
    }
}