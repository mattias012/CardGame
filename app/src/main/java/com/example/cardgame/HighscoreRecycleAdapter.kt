package com.example.cardgame

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HighscoreRecycleAdapter(val context: Context, val highscoreList : List<GameResult>) : RecyclerView.Adapter<HighscoreRecycleAdapter.ViewHolder>() {

    private var layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.highscore_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return highscoreList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#D3D3D3")) // Ljusgrå
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF")) // Vit
        }

        when (position) {
            0 -> {
                val resID = holder.itemView.resources.getIdentifier("gold", "drawable", holder.itemView.context.packageName)
                holder.prizeImageView?.setBackgroundResource(resID)
            }
            1 -> {
                val resID = holder.itemView.resources.getIdentifier("silver", "drawable", holder.itemView.context.packageName)
                holder.prizeImageView?.setBackgroundResource(resID)
            }
            2 -> {
                val resID = holder.itemView.resources.getIdentifier("bronze", "drawable", holder.itemView.context.packageName)
                holder.prizeImageView?.setBackgroundResource(resID)
            }
            else -> {
                holder.prizeImageView?.isVisible = false
            }
        }
        val gameresult = highscoreList[position]

        holder.rankTextView?.text = (position+1).toString() + "."
        holder.nameTextView?.text = gameresult.playerName
        holder.scoreTextView?.text = gameresult.score.toString() + "p"

        val date = Date(gameresult.timestamp)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
        val dateString = format.format(date)

        holder.dateandtime?.text = dateString

        val avatar = gameresult.avatar
        val resID = holder.itemView.resources.getIdentifier(avatar, "drawable", holder.itemView.context.packageName)
        holder.avatarImageView?.setBackgroundResource(resID)

    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var rankTextView: TextView? = itemView.findViewById<TextView>(R.id.positionTextView)
        var nameTextView: TextView? = itemView.findViewById<TextView>(R.id.nameTextView)
        var scoreTextView: TextView? = itemView.findViewById<TextView>(R.id.scoreTextView)
        var dateandtime: TextView? = itemView.findViewById<TextView>(R.id.dateandtime)
        var avatarImageView: ImageView? = itemView.findViewById<ImageView>(R.id.avatarImageView)
        var prizeImageView: ImageView? = itemView.findViewById<ImageView>(R.id.prizeImageView)
    }
}