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

/**
 * Adaptador para mostrar los personajes de Marvel en un RecyclerView.
 * Este adaptador maneja la lista de personajes, infla las vistas y maneja los clics en cada elemento.
 */
class CharacterAdapter(private val onCharacterClick: (Character) -> Unit) :
    ListAdapter<Character, CharacterAdapter.CharacterViewHolder>(CharacterDiffCallback()) {

    /**
     * ViewHolder que representa un elemento en la lista de personajes.
     * Contiene la lógica para vincular los datos del personaje con las vistas.
     */
    inner class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val characterName: TextView = itemView.findViewById(R.id.characterName) // Vista para el nombre del personaje
        private val characterImage: CircleImageView = itemView.findViewById(R.id.characterImage) // Vista para la imagen del personaje

        /**
         * Método para vincular los datos del personaje con las vistas.
         * @param character El objeto Character a mostrar en la vista.
         */
        fun bind(character: Character) {
            characterName.text = character.name // Asigna el nombre del personaje

            // Construcción de la URL de la imagen del personaje y asegurar que use https
            var imageUrl = "${character.thumbnail.path}.${character.thumbnail.extension}"
            imageUrl = imageUrl.replace("http://", "https://")

            // Cargar la imagen usando Picasso (librería para cargar imágenes)
            Picasso.get()
                .load(imageUrl) // Carga la imagen desde la URL
                .into(characterImage) // Asigna la imagen cargada a la vista ImageView

            // Agregar un listener de clic en toda la tarjeta del elemento
            itemView.setOnClickListener {
                onCharacterClick(character) // Llama a la función que maneja el clic, pasando el personaje
            }
        }
    }

    /**
     * Método llamado para crear un nuevo ViewHolder, que inflará el layout correspondiente.
     * @param parent El contenedor donde se va a agregar la vista.
     * @param viewType Tipo de vista (en este caso solo hay un tipo).
     * @return Un nuevo ViewHolder para el item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(view) // Devuelve el ViewHolder con el layout inflado
    }

    /**
     * Método llamado para vincular el ViewHolder con los datos del personaje en la posición indicada.
     * @param holder El ViewHolder que contiene las vistas.
     * @param position La posición del item en la lista.
     */
    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = getItem(position) // Obtiene el personaje en la posición actual
        holder.bind(character) // Vincula el personaje con el ViewHolder
    }

    /**
     * DiffUtil.Callback que se usa para comparar los elementos de la lista y determinar si son iguales.
     * Utilizado para optimizar la actualización de la lista en el RecyclerView.
     */
    class CharacterDiffCallback : DiffUtil.ItemCallback<Character>() {
        /**
         * Compara si dos personajes son el mismo, basándose en su ID único.
         * @param oldItem El personaje anterior.
         * @param newItem El personaje nuevo.
         * @return true si los personajes tienen el mismo ID.
         */
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        /**
         * Compara si el contenido de dos personajes es igual.
         * @param oldItem El personaje anterior.
         * @param newItem El personaje nuevo.
         * @return true si los contenidos de los personajes son iguales.
         */
        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }
}

