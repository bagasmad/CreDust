package com.example.credust

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.credust.data.ProjectDataClass
import com.example.credust.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    var project: ProjectDataClass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            project = it.getParcelable("project")
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
        binding.craftImage.setImageResource(
            resources.getIdentifier(
                project?.image?.substring(1),
                null,
                context?.packageName
            )
        )
        val stringBuilder = StringBuilder()
        project?.materials?.forEach {
            stringBuilder.append(" • ")
            stringBuilder.append(it)
        }
        binding.textMaterials.text = stringBuilder.toString()
        binding.title.text = project?.title


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