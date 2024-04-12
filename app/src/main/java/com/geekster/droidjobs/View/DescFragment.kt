package com.geekster.droidjobs.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import coil.load
import coil.transform.RoundedCornersTransformation
import com.geekster.droidjobs.Models.JobData
import com.geekster.droidjobs.R
import com.geekster.droidjobs.databinding.FragmentDescBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DescFragment : Fragment() {

    private var _binding : FragmentDescBinding? = null

    private val binding get() = _binding!!

    private var itemData: JobData? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentDescBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()

        binding.descriptionText.movementMethod = ScrollingMovementMethod()      //Scroll of textview
        binding.descriptionText.setTextIsSelectable(true)
    }

    private fun setData() {
        val json = arguments?.getString("jobData")
        if(json != null){
            itemData = Gson().fromJson(json, JobData::class.java)

            itemData?.let {item ->
                val descData = HtmlCompat.fromHtml(item.description,HtmlCompat.FROM_HTML_MODE_COMPACT)
                binding.descriptionText.text = descData.toString()
                binding.jobRoleText.text = item.job_title
                binding.companyText.text = item.company
                binding.locationText.text = item.location
                binding.salaryText.text = "$${item.salary_min} - $${item.salary_max}"
                binding.remoteText.text = item.remote_type
                binding.typeText.text = item.contract_type
                binding.companyImageProfile.load(item.logo) {
                    transformations(RoundedCornersTransformation(16f)) // Set the radius of rounded corners
                    placeholder(R.drawable.loading_img) // Placeholder image while loading
                    error(R.drawable.no_img_desc_screen) // Error image if loading fails
                }
                binding.knowMoreBtn.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.apply_url))
                    startActivity(intent)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}