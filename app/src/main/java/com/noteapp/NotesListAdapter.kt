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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = dataSet.get(position)
        holder.tvDay?.text = note.day
        holder.tvDetails?.text = note.details
        holder.tvTitle?.text = note.title
        Picasso.get()
            .load(note.imagePath)
            .into(holder.ivImage);

        holder.checkbox?.isChecked = note.isSelected
        holder.checkbox?.setOnClickListener {
            checkBoxChangeListner(it, position)
        }
    }

    override fun getItemCount(): Int = dataSet.size
}

