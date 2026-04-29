package com.example.projetodispositivosmoveis.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projetodispositivosmoveis.data.model.Subtopico;
import java.util.List;
@Dao
public interface SubtopicoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Subtopico subtopico);

    @Update
    void update(Subtopico subtopico);

    @Delete
    void delete(Subtopico subtopico);

    /**
     * ":trilhaId" é um parâmetro bind — Room substitui pelo argumento passado
     * no método, de forma SEGURA (previne SQL Injection).
     */
    @Query("SELECT * FROM subtopicos WHERE trilha_id = :trilhaId ORDER BY titulo ASC")
    LiveData<List<Subtopico>> getSubtopicosByTrilha(int trilhaId);

    // Para a tela de "Favoritos / Acesso Rápido"
    // is_favorite = 1 pois SQLite armazena boolean como inteiro
    @Query("SELECT * FROM subtopicos WHERE is_favorite = 1 ORDER BY titulo ASC")
    LiveData<List<Subtopico>> getFavorites();

    /**
     * Busca em título, conteúdo E tags.
     * A IA futura poderá filtrar por tags diretamente.
     */
    @Query("SELECT * FROM subtopicos WHERE titulo LIKE :searchQuery " +
            "OR conteudo_markdown LIKE :searchQuery " +
            "OR tags LIKE :searchQuery")
    LiveData<List<Subtopico>> searchSubtopicos(String searchQuery);

    @Query("SELECT * FROM subtopicos WHERE id = :subtopicoId")
    LiveData<Subtopico> getSubtopicoById(int subtopicoId);
}
