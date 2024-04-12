package com.geekster.droidjobs.View

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.geekster.droidjobs.Models.JobData
import com.geekster.droidjobs.R
import com.geekster.droidjobs.databinding.ListItemBinding


class ListItemAdapter(private val onItemClicked : (JobData) -> Unit) :
    ListAdapter<JobData, ListItemAdapter.ListItemViewHolder> (ComparatorDiffUtil())
{
    private var originalList: List<JobData> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListItemAdapter.ListItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val item = getItem(position)
        item?.let{
            holder.bind(it)
        }
    }

    fun setOriginalList(list: List<JobData>) {
        originalList = list
        submitList(list)
    }
    fun filter(query: String?) {
        if (query.isNullOrEmpty()) {
            submitList(originalList)
        } else {
            val filteredList = originalList.filter { it.company.contains(query, true) }
            submitList(filteredList)
        }
    }

    inner class ListItemViewHolder(private val binding : ListItemBinding) :
            RecyclerView.ViewHolder(binding.root){
                fun bind(item : JobData){
                    binding.textRole.text = item.job_title
                    binding.textLocation.text = item.location
                    binding.textCompany.text = item.company
                    binding.textSalary.text = "$${item.salary_min} - $${item.salary_max}"
                    binding.imgCompany.load(item.logo) {
                        transformations(RoundedCornersTransformation(16f)) // Set the radius of rounded corners
                        size(128,128)
                        placeholder(R.drawable.loading_img) // Placeholder image while loading
                        error(R.drawable.no_img) // Error image if loading fails
                    }

                    binding.root.setOnClickListener {
                        onItemClicked(item)
                    }

                }
            }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<JobData>() {
        override fun areItemsTheSame(oldItem: JobData, newItem: JobData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: JobData, newItem: JobData): Boolean {
            return oldItem == newItem
        }
    }
}