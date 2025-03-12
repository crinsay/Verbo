package com.example.verbo.models.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.verbo.models.DatabaseConstants
import com.example.verbo.models.entities.Language

@Dao
interface LanguageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLanguage(newLanguage: Language)

    @Update
    suspend fun updateLanguage(updatedLanguage: Language)

    @Delete
    suspend fun deleteLanguage(languageToDelete: Language)

    @Query("""
        SELECT languageId, name
        FROM ${DatabaseConstants.TableNames.LANGUAGE}
        ORDER BY name COLLATE NOCASE
    """)
    suspend fun getAllLanguages(): List<Language>

    @Query("""
        SELECT languageId, name
        FROM ${DatabaseConstants.TableNames.LANGUAGE}
        WHERE languageId = :languageId
        LIMIT 1
    """)
    suspend fun getLanguageById(languageId: Long): Language

    @Query("""
        SELECT
        EXISTS (SELECT 1
                FROM ${DatabaseConstants.TableNames.LANGUAGE}
                LIMIT 1)
    """)
    suspend fun isAnyLanguageExist(): Boolean
}