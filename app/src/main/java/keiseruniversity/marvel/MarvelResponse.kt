package keiseruniversity.marvel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class MarvelResponse(
    val data: Data
)

data class Data(
    val results: List<Character>
)

@Parcelize
data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail // Esta línea es crucial

) : Parcelable

@Parcelize
data class Thumbnail(
    val path: String,
    val extension: String
) : Parcelable


