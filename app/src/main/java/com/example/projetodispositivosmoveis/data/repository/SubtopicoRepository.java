package com.example.projetodispositivosmoveis.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.projetodispositivosmoveis.data.dao.SubtopicoDao;
import com.example.projetodispositivosmoveis.data.database.AppDatabase;
import com.example.projetodispositivosmoveis.data.model.Subtopico;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class SubtopicoRepository {
    private final SubtopicoDao subtopicoDao;
    private final ExecutorService databaseWriteExecutor =
            Executors.newSingleThreadExecutor();

    public SubtopicoRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        subtopicoDao   = db.subtopicoDao();
    }

    // -------------------------------------------------------
    // LEITURA — LiveData (thread gerenciada pelo Room)
    // -------------------------------------------------------
    public LiveData<List<Subtopico>> getSubtopicosByTrilha(int trilhaId) {
        return subtopicoDao.getSubtopicosByTrilha(trilhaId);
    }

    public LiveData<List<Subtopico>> getFavorites() {
        return subtopicoDao.getFavorites();
    }

    /**
     * Formata a query para busca "contém".
     * A Activity passa "python" → o Repository transforma em "%python%"
     * antes de enviar ao DAO. A Activity não precisa saber desse detalhe.
     */
    public LiveData<List<Subtopico>> searchSubtopicos(String termo) {
        return subtopicoDao.searchSubtopicos("%" + termo + "%");
    }

    public LiveData<Subtopico> getSubtopicoById(int id) {
        return subtopicoDao.getSubtopicoById(id);
    }

    // -------------------------------------------------------
    // ESCRITA — ExecutorService (background thread)
    // -------------------------------------------------------
    public void insert(Subtopico subtopico) {
        databaseWriteExecutor.execute(() -> subtopicoDao.insert(subtopico));
    }

    public void update(Subtopico subtopico) {
        databaseWriteExecutor.execute(() -> subtopicoDao.update(subtopico));
    }

    public void delete(Subtopico subtopico) {
        databaseWriteExecutor.execute(() -> subtopicoDao.delete(subtopico));
    }

}
