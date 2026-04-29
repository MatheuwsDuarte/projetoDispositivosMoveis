package com.example.projetodispositivosmoveis.ui.subtopico;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.projetodispositivosmoveis.data.model.Subtopico;
import com.example.projetodispositivosmoveis.data.repository.SubtopicoRepository;

import java.util.List;


public class SubtopicoViewModel extends AndroidViewModel{
    private final SubtopicoRepository repository;

    /**
     * MutableLiveData para o ID da trilha atual.
     * Quando a Activity mudar de trilha (ex: usuário navega para "Python"),
     * ela chama setTrilhaId(novoId) e a lista de subtópicos se atualiza
     * automaticamente via Transformations.switchMap.
     */
    private final MutableLiveData<Integer> trilhaIdLiveData = new MutableLiveData<>();

    /**
     * Transformations.switchMap:
     * "Sempre que trilhaIdLiveData mudar, chame esta função e
     *  retorne um NOVO LiveData com os subtópicos da nova trilha."
     * É como um 'switch' reativo — troca a fonte de dados automaticamente.
     */
    public final LiveData<List<Subtopico>> subtopicosDaTrilha;

    public SubtopicoViewModel(@NonNull Application application) {
        super(application);

        // 1º → repository é criado
        repository = new SubtopicoRepository(application);

        // 2º → AGORA é seguro referenciar 'repository' dentro da lambda,
        //       pois ele já foi inicializado na linha acima.
        subtopicosDaTrilha = Transformations.switchMap(
                trilhaIdLiveData,
                id -> repository.getSubtopicosByTrilha(id)
        );
    }

    /**
     * A Activity chama este método ao abrir a tela de uma Trilha.
     * Ex: viewModel.setTrilhaId(trilha.getId());
     */
    public void setTrilhaId(int trilhaId) {
        trilhaIdLiveData.setValue(trilhaId);
    }

    public LiveData<List<Subtopico>> getFavorites() {
        return repository.getFavorites();
    }

    public LiveData<List<Subtopico>> searchSubtopicos(String termo) {
        return repository.searchSubtopicos(termo);
    }

    public LiveData<Subtopico> getSubtopicoById(int id) {
        return repository.getSubtopicoById(id);
    }

    public void insert(Subtopico subtopico)  { repository.insert(subtopico);  }
    public void update(Subtopico subtopico)  { repository.update(subtopico);  }
    public void delete(Subtopico subtopico)  { repository.delete(subtopico);  }
}
