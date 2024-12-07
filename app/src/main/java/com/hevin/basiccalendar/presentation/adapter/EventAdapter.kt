package com.hevin.basiccalendar.presentation.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.hevin.basiccalendar.R
import com.hevin.basiccalendar.common.Constants.BASE_TAG
import com.hevin.basiccalendar.databinding.ItemEventLayoutBinding
import com.hevin.basiccalendar.domain.model.CalendarModel
import com.hevin.basiccalendar.domain.model.Event
import com.hevin.basiccalendar.domain.repository.DateItemClickListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventAdapter(private var list: ArrayList<Event>, private val dateItemClickListener: DateItemClickListener):
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    private final val TAG = BASE_TAG + EventAdapter::class.java.simpleName

    init { for (i in list){ //Log.e(TAG, "init $i" )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: ArrayList<Event>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    //region Optional: If you only need to change a small set of data, you can use more specific methods
    //endregion
    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(newList: ArrayList<Event>) {
        list = newList
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, newItem: Event) {
        list[position] = newItem
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val lp = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        binding.root.apply {
            layoutParams = lp
        }
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position = position)
    }

    inner class ViewHolder(private val binding: ItemEventLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        private val layout = binding.root

        @SuppressLint("SetTextI18n")
        fun bind(event: Event, position: Int){
            binding.apply {
                eventDate.text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(event.startTime))
                eventTitle.text = event.title
                eventTimePeriod.text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(event.endTime))
            }
        }
    }
}