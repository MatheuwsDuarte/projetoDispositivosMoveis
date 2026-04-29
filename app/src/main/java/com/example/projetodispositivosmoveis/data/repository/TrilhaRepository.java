package com.example.projetodispositivosmoveis.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.projetodispositivosmoveis.data.dao.TrilhaDao;
import com.example.projetodispositivosmoveis.data.database.AppDatabase;
import com.example.projetodispositivosmoveis.data.model.Trilha;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class TrilhaRepository {

    private final TrilhaDao trilhaDao;
    private final LiveData<List<Trilha>> allTrilhas;

    /**
     * ExecutorService com 1 thread dedicada ao banco.
     * Por que 1 thread? Para evitar condições de corrida (race conditions)
     * onde dois escritas simultâneas corrompem os dados.
     * Você pode usar newFixedThreadPool(4) se precisar de paralelismo,
     * mas para escrita em banco, 1 thread é mais seguro.
     */
    private final ExecutorService databaseWriteExecutor =
            Executors.newSingleThreadExecutor();

    public TrilhaRepository(Application application) {
        // Obtém a instância Singleton do banco e o DAO correspondente
        AppDatabase db = AppDatabase.getInstance(application);
        trilhaDao = db.trilhaDao();

        // allTrilhas é carregado UMA VEZ e fica "vivo" graças ao LiveData.
        // O Room notifica automaticamente quando os dados mudarem.
        allTrilhas = trilhaDao.getAllTrilhas();
    }

    // -------------------------------------------------------
    // MÉTODOS DE LEITURA (LiveData — Room já gerencia a thread)
    // -------------------------------------------------------
    public LiveData<List<Trilha>> getAllTrilhas() {
        return allTrilhas;
    }

    public LiveData<Trilha> getTrilhaById(int id) {
        return trilhaDao.getTrilhaById(id);
    }

    // -------------------------------------------------------
    // MÉTODOS DE ESCRITA (ExecutorService — roda em background)
    // A lambda () -> ... é a tarefa enviada para a thread worker.
    // -------------------------------------------------------
    public void insert(Trilha trilha) {
        databaseWriteExecutor.execute(() -> trilhaDao.insert(trilha));
    }

    public void update(Trilha trilha) {
        databaseWriteExecutor.execute(() -> trilhaDao.update(trilha));
    }

    public void delete(Trilha trilha) {
        databaseWriteExecutor.execute(() -> trilhaDao.delete(trilha));
    }
}
