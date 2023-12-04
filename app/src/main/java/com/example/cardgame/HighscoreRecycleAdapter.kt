package com.example.cardgame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
        val gameresult = highscoreList[position]

        holder.rankTextView?.text = (position+1).toString()
        holder.nameTextView?.text = gameresult.playerName
        holder.scoreTextView?.text = gameresult.score.toString()

        val avatar = gameresult.avatar
        val resID = holder.itemView.resources.getIdentifier(avatar, "drawable", holder.itemView.context.packageName)
        holder.avatarImageView?.setBackgroundResource(resID)

    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var rankTextView: TextView? = itemView.findViewById<TextView>(R.id.positionTextView)
        var nameTextView: TextView? = itemView.findViewById<TextView>(R.id.nameTextView)
        var scoreTextView: TextView? = itemView.findViewById<TextView>(R.id.scoreTextView)
        var avatarImageView: ImageView? = itemView.findViewById<ImageView>(R.id.avatarImageView)
    }
}