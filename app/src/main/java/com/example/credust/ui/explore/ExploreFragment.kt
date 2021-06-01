package com.example.credust.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.credust.R
import com.example.credust.databinding.FragmentExploreBinding
import com.example.credust.utils.CardViewAdapter
import com.example.credust.utils.ViewModelFactory

class ExploreFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentExploreBinding
    private lateinit var viewModel: ExploreViewModel
    var favorite: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), factory).get(ExploreViewModel::class.java)
        viewModel.favorite.postValue(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(layoutInflater, container, false)
        val adapter = CardViewAdapter()

        viewModel.favorite.observe(this,
            {
                if (it) {
                    viewModel.getProjects().removeObservers(this)
                    viewModel.getFavoriteProjects().observe(this,
                        {
                            adapter.setProjects(it)
                        })
                } else {

                    viewModel.getFavoriteProjects().removeObservers(this)
                    viewModel.getProjects().observe(this,
                        {
                            adapter.setProjects(it)
                        })


                }

            })
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.bookmarkButton.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(view: View?) {
        viewModel.favorite.postValue(favorite)
        favorite = !favorite
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ExploreFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }


}