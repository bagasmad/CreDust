package com.example.credust.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.credust.R
import com.example.credust.data.ProjectDataClass
import com.example.credust.databinding.FragmentDetailBinding
import com.example.credust.utils.InstructionAdapter
import com.example.credust.utils.ViewModelFactory


class DetailFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private var nextState: Boolean = true
    private var project: ProjectDataClass? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        detailViewModel =
            ViewModelProvider(requireActivity(), factory).get(DetailViewModel::class.java)
        arguments?.let {
            project = it.getParcelable("project")
            when (project?.favorite) {
                true -> nextState = false
                false -> nextState = true
            }
            project?.let { it1 ->
                detailViewModel.setProject(it1)
                detailViewModel.favorite.postValue(it1.favorite)
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailViewModel.getProject().observe(this, {
            setViews()
        })
        detailViewModel.favorite.observe(this,
            {
                if (it) {
                    binding.floatingButton.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_bookmark_yes
                        )
                    )
                    nextState = false
                } else {
                    binding.floatingButton.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_bookmark_not
                        )
                    )
                    nextState = true
                }
            })
        binding.floatingButton.setOnClickListener(this)

    }

    private fun setViews() {
        with(detailViewModel.getProject().value) {
            //set title
            binding.title.text = this?.title

            //set image
            binding.craftImage.setImageResource(
                resources.getIdentifier(
                    this?.image?.substring(1),
                    null,
                    context?.packageName
                )
            )
            //set materials
            val stringBuilder = StringBuilder()
            this?.materials?.forEach {
                stringBuilder.append(" • ")
                stringBuilder.append(it)
            }
            binding.textMaterials.text = stringBuilder.toString()

            //set Recyclerview
            val adapter = InstructionAdapter()
            adapter.setInstructions(this?.instructions)
            binding.instructionsRecycler.layoutManager = LinearLayoutManager(context)
            binding.instructionsRecycler.adapter = adapter

            //tags
            when (this?.plastic) {
                true -> binding.plasticCard.visibility = View.VISIBLE
                else -> binding.plasticCard.visibility = View.GONE
            }
            when (this?.glass) {
                true -> binding.glassCard.visibility = View.VISIBLE
                else -> binding.glassCard.visibility = View.GONE
            }
            when (this?.metal) {
                true -> binding.metalCard.visibility = View.VISIBLE
                else -> binding.metalCard.visibility = View.GONE
            }

            //points on views
            binding.pointsText.text = this?.points.toString()
            binding.button.text =
                resources.getString(R.string.post_your_creations_for_x_points, this?.points)


        }
    }

    override fun onClick(view: View?) {
        project?.let { detailViewModel.updateFavorite(it, nextState) }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DetailFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}