package octopus.inc.spotifysearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import octopus.inc.spotifysearch.databinding.DialogFragmentMyProgressDialogBinding
import octopus.inc.spotifysearch.fragment.TrackListFragment
import octopus.inc.spotifysearch.viewmodel.TrackListViewModel

class MyProgressDialog : DialogFragment() {

    lateinit var binding: DialogFragmentMyProgressDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentMyProgressDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "MyProgressDialog"
    }
}