package com.example.dailytasks.girl

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytasks.R
import com.example.dailytasks.Results

class GirlRecyclerAdapter(private val resultsList: List<Results>):
    RecyclerView.Adapter<GirlRecyclerAdapter.ResultsViewHolder>() {

    class ResultsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val smileImage: ImageView = itemView.findViewById(R.id.smile_image)
        val fullDate: TextView = itemView.findViewById(R.id.date_full)
        val shortDate: TextView = itemView.findViewById(R.id.date_short)
        val invisibleDate: TextView = itemView.findViewById(R.id.invisible_date)
        val editButton: Button = itemView.findViewById(R.id.edit_button)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val rating: TextView = itemView.findViewById(R.id.rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.results, parent, false)
        return ResultsViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        val result = resultsList[position]
        holder.smileImage.setImageResource(result.smileImage)
        holder.fullDate.text = result.fullDate
        holder.shortDate.text = result.shortDate
        holder.invisibleDate.text = result.invisibleDate
        holder.editButton.setOnClickListener {
            val action = GirlFragmentGeneralDirections.actionGirlFragmentGeneralToGirlFragmentHistory(bundleDate = holder.invisibleDate.text.toString())
            holder.itemView.findNavController().navigate(action)
        }
        holder.ratingBar.rating = result.rating
        holder.rating.text = result.rating.toString()
    }

    override fun getItemCount(): Int {
        return resultsList.size
    }
}