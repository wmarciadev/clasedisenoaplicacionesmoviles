package data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Insert
    suspend fun insertFavorite(character: CharacterEntity)

    @Delete
    suspend fun deleteFavorite(character: CharacterEntity)

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getFavoriteById(id: Int): CharacterEntity?

    @Query("SELECT * FROM characters")
    fun getAllFavorites(): Flow<List<CharacterEntity>>
}