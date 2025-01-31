package keiseruniversity.marvel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Respuesta de la API de Marvel que contiene los datos de los personajes.
 */
data class MarvelResponse(
    val data: Data // Contiene la lista de personajes obtenidos de la API
)

/**
 * Clase que envuelve la lista de resultados de personajes.
 */
data class Data(
    val results: List<Character> // Lista de objetos `Character` que representan a los personajes de Marvel
)

/**
 * Representa un personaje de Marvel.
 * Implementa `Parcelable` para permitir su paso entre actividades a través de `Intent`.
 */
@Parcelize
data class Character(
    val id: Int, // Identificador único del personaje
    val name: String, // Nombre del personaje
    val description: String, // Descripción del personaje
    val thumbnail: Thumbnail // Miniatura (imagen) del personaje, la cual es clave para mostrar la imagen
) : Parcelable // Implementa Parcelable para ser enviado a través de `Intent`

/**
 * Representa la miniatura de la imagen de un personaje.
 * Incluye la ruta y la extensión del archivo de la imagen.
 */
@Parcelize
data class Thumbnail(
    val path: String, // Ruta base de la imagen
    val extension: String // Extensión del archivo de la imagen (por ejemplo, ".jpg")
) : Parcelable // Implementa Parcelable para ser enviado a través de `Intent`

