package keiseruniversity.marvel

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso


/**
 * Actividad que muestra los detalles de un personaje de Marvel.
 * Esta actividad recibe un objeto `Character` a través de un `Intent` y muestra su nombre, descripción e imagen.
 */
class CharacterDetailActivity : AppCompatActivity() {

    private lateinit var characterName: TextView // TextView para mostrar el nombre del personaje
    private lateinit var characterImage: ImageView // ImageView para mostrar la imagen del personaje
    private lateinit var characterDescription: TextView // TextView para mostrar la descripción del personaje

    /**
     * Método que se llama cuando la actividad se crea.
     * Aquí se inicializan las vistas y se cargan los datos del personaje.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_detail)

        // Inicializamos las vistas a partir del archivo de layout
        characterName = findViewById(R.id.characterName)
        characterImage = findViewById(R.id.characterImage)
        characterDescription = findViewById(R.id.characterDescription)

        // Recuperamos el objeto `Character` del `Intent`
        val character: Character? = intent.getParcelableExtra("character")

        if (character != null) {
            // Si el objeto `Character` no es nulo, asignamos los datos a las vistas correspondientes
            characterName.text = character.name
            characterDescription.text = character.description.ifEmpty { "No description available" }

            // Construimos la URL de la imagen a partir de la información de `thumbnail`
            val imageUrl = "${character.thumbnail.path}.${character.thumbnail.extension}".replace("http://", "https://")

            // Usamos Picasso para cargar la imagen de forma eficiente
            Picasso.get()
                .load(imageUrl) // Cargamos la imagen desde la URL
                .placeholder(R.drawable.placeholder_image) // Imagen que se muestra mientras se carga
                .error(R.drawable.error_image) // Imagen que se muestra si ocurre un error
                .into(characterImage) // Asignamos la imagen cargada al `ImageView`
        } else {
            // Si el objeto `Character` es nulo, mostramos un mensaje de error
            characterName.text = getString(R.string.personaje_no_encontrado)
            characterDescription.text =
                getString(R.string.errror_de_carga_del_detalle_del_personaje)
        }
    }
}
