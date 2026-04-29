package com.example.projetodispositivosmoveis.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projetodispositivosmoveis.data.model.Trilha;
import java.util.List;

/**
 * @Dao: Marca esta interface para o Room processar.
 * O Room gera internamente a classe TrilhaDao_Impl com o SQL real.
 */
@Dao
public interface TrilhaDao {

    // INSERT → "INSERT INTO trilhas (nome) VALUES (?)"
    // IGNORE: se houver conflito de ID, ignora silenciosamente.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Trilha trilha);

    // UPDATE → "UPDATE trilhas SET nome=? WHERE id=?"
    // O Room usa o @PrimaryKey do objeto para saber qual linha atualizar.
    @Update
    void update(Trilha trilha);

    // DELETE → "DELETE FROM trilhas WHERE id=?"
    @Delete
    void delete(Trilha trilha);

    /**
     * Retorna LiveData<List<Trilha>>:
     * A UI "observa" este LiveData. Quando qualquer Trilha for inserida,
     * atualizada ou deletada, a lista na tela se atualiza SOZINHA.
     */
    @Query("SELECT * FROM trilhas ORDER BY nome ASC")
    LiveData<List<Trilha>> getAllTrilhas();

    @Query("SELECT * FROM trilhas WHERE id = :trilhaId")
    LiveData<Trilha> getTrilhaById(int trilhaId);
}
