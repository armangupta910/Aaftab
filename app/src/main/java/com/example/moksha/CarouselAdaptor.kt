package com.example.moksha

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarouselAdapter(private val carouselItems: List<CarouselItem>) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carousel_item, parent, false)
        val image:ImageView = view.findViewById(R.id.imageView)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val item = carouselItems[position]
        holder.itemView.findViewById<ImageView>(R.id.imageView).setImageResource(item.imageResId)
    }

    override fun getItemCount(): Int = carouselItems.size

    class CarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}
