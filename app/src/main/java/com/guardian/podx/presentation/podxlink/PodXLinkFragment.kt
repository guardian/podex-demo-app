package com.guardian.podx.presentation.podxlink

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.guardian.podx.R
import com.guardian.podx.databinding.LayoutPodxlinkfragmentBinding
import com.guardian.podx.utils.lifecycleAwareVar

class PodXLinkFragment :
    Fragment() {

    private var binding: LayoutPodxlinkfragmentBinding by lifecycleAwareVar()

    private val podXLinkArgs: PodXLinkFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_podxlinkfragment,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.caption = podXLinkArgs.caption
        binding.notification = podXLinkArgs.notification
        binding.imageUrlString = podXLinkArgs.imageUrlString

        binding.buttonPodxlinkNavigate.setOnClickListener {
            if (podXLinkArgs.urlString.isNotBlank()) {
                val webPage: Uri = Uri.parse(podXLinkArgs.urlString)
                val intent = Intent(Intent.ACTION_VIEW, webPage)
                startActivity(intent)
            }
        }

        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbarPodxlink)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.title = ""
    }
}
