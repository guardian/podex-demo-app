package com.guardian.podx.presentation.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.guardian.podx.R
import com.guardian.podx.databinding.LayoutAboutfragmentBinding
import com.guardian.podx.utils.lifecycleAwareVar

class AboutFragment : Fragment() {
    private var binding: LayoutAboutfragmentBinding by lifecycleAwareVar()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_aboutfragment,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbarAbout)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        super.onViewCreated(view, savedInstanceState)
    }
}
