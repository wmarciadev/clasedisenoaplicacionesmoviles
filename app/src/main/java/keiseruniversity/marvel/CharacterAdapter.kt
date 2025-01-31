package keiseruniversity.marvel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class CharacterAdapter(private val onCharacterClick: (Character) -> Unit) :
    ListAdapter<Character, CharacterAdapter.CharacterViewHolder>(CharacterDiffCallback()) {

    inner class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val characterName: TextView = itemView.findViewById(R.id.characterName)
        private val characterImage: CircleImageView = itemView.findViewById(R.id.characterImage)

        fun bind(character: Character) {
            characterName.text = character.name

            var imageUrl = "${character.thumbnail.path}.${character.thumbnail.extension}"
            imageUrl = imageUrl.replace("http://", "https://")

            Picasso.get()
                .load(imageUrl)
                .into(characterImage)

            // Agregar evento de clic en toda la tarjeta
            itemView.setOnClickListener {
                onCharacterClick(character) // Llama a la función pasada desde el MainActivity
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character)
    }

    class CharacterDiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }
}




