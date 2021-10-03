package com.noteapp

import android.content.Context
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noteapp.dataclass.Notes
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class NotesListAdapter(private var dataSet: List<Notes>,
                       private val context:Context,
                       val itemClickListener: (View, Int) -> Unit,
                       val checkBoxChangeListner: (View, Int) -> Unit): RecyclerView.Adapter<NotesListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        //var ivImage: ImageView? = null
        var tvTitle: TextView? = null
        var tvDetails: TextView? = null
        var tvDay: TextView? = null
        var ivImage: ImageView? = null
        var checkbox: CheckBox? = null
        //val myImage: Drawable? = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_baseline_image_60_purple, null)

        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvDetails = itemView.findViewById(R.id.tvDetails)
            tvDay = itemView.findViewById(R.id.tvDay)
            ivImage = itemView.findViewById(R.id.ivImage)
            checkbox = itemView.findViewById(R.id.checkbox)
        }
    }

    public fun refreshData(_dataSet: List<Notes>) {
        dataSet = _dataSet
        dateValue = ""
        notifyDataSetChanged()
    }

    public fun getList(): List<Notes> = dataSet

    public fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(it, getAdapterPosition())
        }
        return this
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.fragment_home_contents, viewGroup, false)
        val holder = ViewHolder(itemView)
        holder.onClick(itemClickListener)
        return holder
    }

    var dateValue = "";

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = dataSet.get(position)
        holder.tvDetails?.text = note.details
        holder.tvTitle?.text = note.title
        Picasso.get()
            .load(note.imagePath)
            .into(holder.ivImage);

        val mDateValue = getFormattedDate(note.date)
        when(mDateValue) {
            dateValue -> holder.tvDay?.visibility = View.GONE
            else      -> {
                holder.tvDay?.visibility = View.VISIBLE
                holder.tvDay?.text = mDateValue
                dateValue = mDateValue
            }
        }

        holder.checkbox?.isChecked = note.isSelected
        holder.checkbox?.setOnClickListener {
            checkBoxChangeListner(it, position)
        }
    }

    private fun getFormattedDate(mDate: String): String {
        try {
            val currentTime = Calendar.getInstance().time
            val dateFormatter = SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss")
            val date1: Date = dateFormatter.parse(dateFormatter.format(currentTime))
            val date2: Date = dateFormatter.parse(mDate)
            val different: Long = date1.getTime() - date2.getTime()
            val daysInMilli = 1000 * 60 * 60 * 24
            val _dateFormatter = SimpleDateFormat("DD MMM, YYYY")
            val day: Long = different/daysInMilli
            return when (day) {
                0L    -> "Today"
                -1L   -> "Yesterday"
                else -> _dateFormatter.format(date2)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            return ""
        }
    }

    override fun getItemCount(): Int = dataSet.size
}

