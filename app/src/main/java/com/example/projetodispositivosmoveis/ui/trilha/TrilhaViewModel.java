package com.example.projetodispositivosmoveis.ui.trilha;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projetodispositivosmoveis.data.model.Trilha;
import com.example.projetodispositivosmoveis.data.repository.TrilhaRepository;

import java.util.List;
public class TrilhaViewModel extends AndroidViewModel {

    private final TrilhaRepository repository;

    /**
     * O LiveData exposto para a UI.
     * A Activity OBSERVA este dado — ela não sabe nada sobre
     * banco de dados, DAOs ou threads. Apenas reage às mudanças.
     */
    private final LiveData<List<Trilha>> allTrilhas;

    public TrilhaViewModel(@NonNull Application application) {
        super(application);
        // Cria o Repository passando o Application (não a Activity!)
        repository  = new TrilhaRepository(application);
        allTrilhas  = repository.getAllTrilhas();
    }

    // -------------------------------------------------------
    // API PÚBLICA — o que a Activity enxerga
    // Métodos simples que delegam ao Repository.
    // A Activity chama viewModel.insert(trilha) e não sabe
    // que por baixo existe um ExecutorService rodando a tarefa.
    // -------------------------------------------------------
    public LiveData<List<Trilha>> getAllTrilhas() { return allTrilhas; }

    public LiveData<Trilha> getTrilhaById(int id) {
        return repository.getTrilhaById(id);
    }

    public void insert(Trilha trilha)  { repository.insert(trilha);  }
    public void update(Trilha trilha)  { repository.update(trilha);  }
    public void delete(Trilha trilha)  { repository.delete(trilha);  }

}
