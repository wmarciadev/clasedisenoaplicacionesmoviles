package keiseruniversity.marvel

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class CharacterDetailActivity : AppCompatActivity() {

    private lateinit var characterName: TextView
    private lateinit var characterImage: ImageView
    private lateinit var characterDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_detail)

        characterName = findViewById(R.id.characterName)
        characterImage = findViewById(R.id.characterImage)
        characterDescription = findViewById(R.id.characterDescription)

        val character = intent.getParcelableExtra<Character>("character")

        if (character != null) {
            characterName.text = character.name
            characterDescription.text = character.description.ifEmpty { "No description available" }

            val imageUrl =
                "${character.thumbnail.path}.${character.thumbnail.extension}".replace(
                    "http://",
                    "https://"
                )

            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(characterImage)
        } else {
            characterName.text = "Character not found"
            characterDescription.text = "Error loading character details."
        }
    }
}
