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
import com.hevin.basiccalendar.databinding.ItemCalendarDateLayoutBinding
import com.hevin.basiccalendar.domain.model.CalendarModel
import com.hevin.basiccalendar.domain.repository.DateItemClickListener

class CalendarAdapter(private var list: ArrayList<CalendarModel>, private val dateItemClickListener: DateItemClickListener):RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    private final val TAG = BASE_TAG + CalendarAdapter::class.java.simpleName

    init { for (i in list){
//        Log.e(TAG, "init $i" )
    } }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: ArrayList<CalendarModel>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    //region Optional: If you only need to change a small set of data, you can use more specific methods
    //endregion
    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(newList: ArrayList<CalendarModel>) {
        list = newList
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, newItem: CalendarModel) {
        list[position] = newItem
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalendarDateLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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

    inner class ViewHolder(private val binding: ItemCalendarDateLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        private val layout = binding.root

        @SuppressLint("SetTextI18n")
        fun bind(calendarModel: CalendarModel, position: Int){
            val day = calendarModel.day

            binding.apply {
                num.text = "$day"
                if (calendarModel.isSelected)
                    binding.constraintLay1.background = ContextCompat.getDrawable(itemView.context, R.drawable.month_navigator_shape)
                else
                    binding.constraintLay1.background = null

                when(calendarModel.state) {
                    -1->{
                        itemCalendarDateLayout.visibility = View.INVISIBLE
                    }
                    0->{
                        num.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                        today.visibility = View.INVISIBLE
                    }
                    1->{
                        // past days
                        num.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                        num.background = ContextCompat.getDrawable(itemView.context, R.drawable.shape_circle_green)
                        today.visibility = View.INVISIBLE
                        layout.setOnClickListener {

                            selectItem(position = position)
                            dateItemClickListener.onDateClick(position = position, calendarModel) }
                    }
                    2->{
                        // today
                        num.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                        num.background = ContextCompat.getDrawable(itemView.context, R.drawable.shape_circle_red)
                        today.visibility = View.VISIBLE
                        layout.setOnClickListener {

                            selectItem(position = position)
                            dateItemClickListener.onDateClick(position = position, calendarModel) }
                    }
                    3 -> {
                        // future days
                        num.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                        num.background = ContextCompat.getDrawable(itemView.context, R.drawable.shape_circle_gray)
                        today.visibility = View.INVISIBLE
                        layout.setOnClickListener {

                            selectItem(position = position)
                            dateItemClickListener.onDateClick(position = position, calendarModel) }
                    }
                }
            }
        }
        // This function is responsible for selecting the clicked item and deselecting others
        @SuppressLint("NotifyDataSetChanged")
        private fun selectItem(position: Int) {
            Log.e(TAG, "selectItem: ", )
            // Deselect all items
            list.forEach { it.isSelected = false }
            // Select the clicked item
            list[position].isSelected = true
            notifyDataSetChanged() // Notify the adapter to update the UI
        }
    }
}