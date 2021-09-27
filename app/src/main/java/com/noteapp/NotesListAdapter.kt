package com.noteapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noteapp.dataclass.Notes


class NotesListAdapter(private val dataSet: List<Notes>,
                       private val context:Context,
                       val itemClickListener: (View, Int) -> Unit): RecyclerView.Adapter<NotesListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        //var ivImage: ImageView? = null
        var tvTitle: TextView? = null
        var tvDetails: TextView? = null
        var tvDay: TextView? = null
        //val myImage: Drawable? = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_baseline_image_60_purple, null)


        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvDetails = itemView.findViewById(R.id.tvDetails)
            tvDay = itemView.findViewById(R.id.tvDay)


        }
    }

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
        holder.tvDay?.text = dataSet.get(position).day
        holder.tvDetails?.text = dataSet.get(position).details
        holder.tvTitle?.text = dataSet.get(position).title
    }

    override fun getItemCount(): Int = dataSet.size
}

