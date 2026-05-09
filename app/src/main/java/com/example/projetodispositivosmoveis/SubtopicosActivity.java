package com.example.projetodispositivosmoveis;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetodispositivosmoveis.data.model.Subtopico;
import com.example.projetodispositivosmoveis.ui.subtopico.SubtopicoAdapter;
import com.example.projetodispositivosmoveis.ui.subtopico.SubtopicoViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class SubtopicosActivity extends AppCompatActivity{
    // Constantes para as chaves do Intent — usar constantes evita
    // erros de digitação ao ler/escrever os extras
    public static final String EXTRA_TRILHA_ID   = "EXTRA_TRILHA_ID";
    public static final String EXTRA_TRILHA_NOME = "EXTRA_TRILHA_NOME";

    private SubtopicoViewModel subtopicoViewModel;
    private SubtopicoAdapter adapter;
    private TextView textViewVazio;

    private int trilhaId;
    private String trilhaNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtopicos);

        // --- 1. RECUPERAR OS DADOS DO INTENT ---
        // getIntent() retorna o Intent que abriu esta Activity.
        // Se não encontrar o extra, usa valores padrão (-1 e "Trilha").
        trilhaId   = getIntent().getIntExtra(EXTRA_TRILHA_ID, -1);
        trilhaNome = getIntent().getStringExtra(EXTRA_TRILHA_NOME);

        if (trilhaNome == null) trilhaNome = "Trilha";

        // Segurança: se não recebeu um ID válido, fecha a Activity
        if (trilhaId == -1) {
            finish();
            return;
        }

        // --- 2. CONFIGURAR TOOLBAR COM O NOME DA TRILHA ---
        Toolbar toolbar = findViewById(R.id.toolbarSubtopicos);
        setSupportActionBar(toolbar);

        // Exibe o nome da trilha como título da tela
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(trilhaNome);
            // Habilita o botão de voltar (seta) na toolbar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // --- 3. RECYCLERVIEW ---
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSubtopicos);
        textViewVazio = findViewById(R.id.textViewVazioSubtopicos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // --- 4. ADAPTER ---
        adapter = new SubtopicoAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnSubtopicoClickListener(new SubtopicoAdapter.OnSubtopicoClickListener() {

            @Override
            public void onSubtopicoLongClick(Subtopico subtopico) {
                new MaterialAlertDialogBuilder(SubtopicosActivity.this)
                        .setTitle("Excluir Subtópico")
                        .setMessage("Deseja excluir \"" + subtopico.getTitulo() + "\"?")
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Excluir", (dialog, which) ->
                                subtopicoViewModel.delete(subtopico)
                        )
                        .show();
            }

            @Override
            public void onFavoritoClick(Subtopico subtopico) {
                // Inverte o estado de favorito e salva no banco
                subtopico.setFavorite(!subtopico.isFavorite());
                subtopicoViewModel.update(subtopico);
            }

            @Override
            public void onSubtopicoClick(Subtopico subtopico) {
                Intent intent = new Intent(SubtopicosActivity.this,
                        DetalheSubtopicoActivity.class);
                intent.putExtra(DetalheSubtopicoActivity.EXTRA_SUBTOPICO_ID,
                        subtopico.getId());
                startActivity(intent);
            }
        });

        // --- 5. VIEWMODEL ---
        subtopicoViewModel = new ViewModelProvider(this).get(SubtopicoViewModel.class);

        // Informa ao ViewModel qual trilha deve ser carregada.
        // Isso dispara o Transformations.switchMap que criamos na Sprint 2.
        subtopicoViewModel.setTrilhaId(trilhaId);

        // --- 6. OBSERVAR LIVEDATA ---
        subtopicoViewModel.subtopicosDaTrilha.observe(this, subtopicos -> {
            adapter.submitList(subtopicos);
            textViewVazio.setVisibility(
                    subtopicos.isEmpty() ? View.VISIBLE : View.GONE
            );
        });

        // --- 7. FAB ---
        FloatingActionButton fab = findViewById(R.id.fabAdicionarSubtopico);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(SubtopicosActivity.this, CadastroSubtopicoActivity.class);
            // Passa o ID e nome da trilha para a tela de cadastro
            intent.putExtra(CadastroSubtopicoActivity.EXTRA_TRILHA_ID, trilhaId);
            intent.putExtra(CadastroSubtopicoActivity.EXTRA_TRILHA_NOME, trilhaNome);
            startActivity(intent);
        });
    }

    /**
     * onSupportNavigateUp: chamado quando o usuário clica na seta de voltar da Toolbar.
     * finish() encerra esta Activity e volta para a anterior (MainActivity).
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void mostrarDialogNovoSubtopico() {
        // Layout do dialog com campos de título e tags
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_novo_subtopico, null);
        EditText editTitulo = dialogView.findViewById(R.id.editTextTituloSubtopico);
        EditText editTags   = dialogView.findViewById(R.id.editTextTagsSubtopico);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Novo Subtópico em " + trilhaNome)
                .setView(dialogView)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String titulo = editTitulo.getText().toString().trim();
                    String tags   = editTags.getText().toString().trim();

                    if (TextUtils.isEmpty(titulo)) {
                        Snackbar.make(
                                findViewById(android.R.id.content),
                                "O título não pode ser vazio!",
                                Snackbar.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    // Cria o Subtopico vinculado a esta trilha pelo trilhaId
                    Subtopico novoSubtopico = new Subtopico(
                            titulo,
                            "",       // conteúdo Markdown — editado na tela de detalhe (Sprint futura)
                            null,     // caminho de imagem — Sprint futura
                            false,    // não favoritado por padrão
                            tags,     // tags digitadas pelo usuário
                            trilhaId  // FK: vincula ao pai correto
                    );
                    subtopicoViewModel.insert(novoSubtopico);
                })
                .show();
    }
}
