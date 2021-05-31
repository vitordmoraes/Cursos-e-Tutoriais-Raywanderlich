package com.raywenderlich.android.creatures.ui
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.app.inflate
import com.raywenderlich.android.creatures.model.Creature
import kotlinx.android.synthetic.main.lis_item_creature_card.view.*

class CreatureCardAdapter (private val creatures: MutableList<Creature>): RecyclerView.Adapter<CreatureCardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) :View.OnClickListener, RecyclerView.ViewHolder(itemView) {
        private lateinit var creature: Creature

        init {
            itemView.setOnClickListener(this)
        }

        fun bind (creature: Creature) {
            this.creature = creature
            val context = itemView.context
            val imageResource = context.resources.getIdentifier(creature.uri, null, context.packageName)
            itemView.creatureImage.setImageResource(imageResource)
            itemView.fullName.text = creature.fullName
            setBackgroudColors(context, imageResource)
        }

        override fun onClick(view: View?) {
            view?.let{
            val context = it.context
            val intent = CreatureActivity.newIntent(context, creature.id)
                context.startActivity(intent)
            }
        }

        private fun setBackgroudColors(context: Context, imageResource: Int) {
            val image = BitmapFactory.decodeResource(context.resources, imageResource)
            Palette.from(image).generate() { palette ->
                palette?.let {
                    val backgroudColor = it.getDominantColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    itemView.creatureCard.setBackgroundColor(backgroudColor)
                    itemView.nameHolder.setBackgroundColor(backgroudColor)
                    val textColor = if (isColorDark(backgroudColor)) Color.WHITE else Color.BLACK
                    itemView.fullName.setTextColor(textColor)
                }
            }
        }

        private fun isColorDark(color: Int): Boolean {
            val darkness = 1 - (0.299 * Color.red(color) +
                    0.587 * Color.green(color) +
                    0.144 * Color.blue(color)) / 255
            return  darkness >= 0.5
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.lis_item_creature_card))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(creatures[position])
    }

    override fun getItemCount() = creatures.size


}