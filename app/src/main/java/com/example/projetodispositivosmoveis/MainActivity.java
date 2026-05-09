package com.example.projetodispositivosmoveis;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;

import com.example.projetodispositivosmoveis.data.model.Trilha;
import com.example.projetodispositivosmoveis.ui.trilha.TrilhaAdapter;
import com.example.projetodispositivosmoveis.ui.trilha.TrilhaViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    // Referências às Views — declaradas como campos para acesso em todo a classe
    private TrilhaViewModel trilhaViewModel;
    private TrilhaAdapter adapter;
    private TextView textViewVazio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- 1. CONFIGURAR A TOOLBAR ---
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // --- 2. CONFIGURAR O RECYCLERVIEW ---
        RecyclerView recyclerView = findViewById(R.id.recyclerViewTrilhas);
        textViewVazio = findViewById(R.id.textViewVazio);

        // LinearLayoutManager: organiza os itens em lista vertical (um abaixo do outro)
        // Alternativas existem: GridLayoutManager (grade), StaggeredGridLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true); // otimização: o tamanho do RV não muda

        // --- 3. CRIAR E CONECTAR O ADAPTER ---
        adapter = new TrilhaAdapter();
        recyclerView.setAdapter(adapter);

        // Define o que acontece quando o usuário clica em uma Trilha
        adapter.setOnTrilhaClickListener(new TrilhaAdapter.OnTrilhaClickListener() {

            @Override
            public void onTrilhaClick(Trilha trilha) {
                // Intent é o "bilhete de passagem" entre Activities.
                // Passamos os dados da Trilha como "extras" — pares chave/valor.
                // A SubtopicosActivity vai ler esses extras para saber qual trilha exibir.
                Intent intent = new Intent(MainActivity.this, SubtopicosActivity.class);
                intent.putExtra("EXTRA_TRILHA_ID", trilha.getId());
                intent.putExtra("EXTRA_TRILHA_NOME", trilha.getNome());
                startActivity(intent);
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "Trilha: " + trilha.getNome(),
                        Snackbar.LENGTH_SHORT
                ).show();
            }

            @Override
            public void onTrilhaLongClick(Trilha trilha) {
                // AlertDialog de confirmação de delete
                new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle("Excluir Trilha")
                        .setMessage("Deseja excluir \"" + trilha.getNome() + "\"?\n\nTodos os subtópicos serão apagados.")
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Excluir", (dialog, which) -> {
                            trilhaViewModel.delete(trilha);
                            Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "Trilha excluída",
                                    Snackbar.LENGTH_SHORT
                            ).show();
                        })
                        .show();
            }
        });

        // --- 4. INICIALIZAR O VIEWMODEL ---
        // ViewModelProvider garante que o ViewModel sobrevive à rotação de tela.
        // NÃO use: new TrilhaViewModel() — isso cria uma instância nova a cada rotação!
        trilhaViewModel = new ViewModelProvider(this).get(TrilhaViewModel.class);

        // --- 5. OBSERVAR O LIVEDATA ---
        // observe(this, ...): "this" é o LifecycleOwner (a Activity).
        // O Room para de enviar updates automaticamente quando a Activity
        // é destruída, evitando crashes e memory leaks.
        trilhaViewModel.getAllTrilhas().observe(this, trilhas -> {
            // Este bloco roda TODA VEZ que a lista de Trilhas mudar no banco.
            // O DiffUtil do ListAdapter cuida de animar apenas o que mudou.
            adapter.submitList(trilhas);

            // Mostra a mensagem de "lista vazia" se não houver trilhas
            textViewVazio.setVisibility(
                    trilhas.isEmpty() ? View.VISIBLE : View.GONE
            );
        });

        // --- 6. FAB — ABRIR DIALOG PARA NOVA TRILHA ---
        FloatingActionButton fab = findViewById(R.id.fabAdicionarTrilha);
        fab.setOnClickListener(view -> mostrarDialogNovaTrilha());
    }

    /**
     * Exibe um AlertDialog com um campo de texto para o usuário
     * digitar o nome da nova Trilha.
     *
     * Por que AlertDialog aqui e não uma nova Activity?
     * Para operações simples (um único campo), um Dialog é mais ágil
     * e mantém o contexto visual da tela atual.
     * Sprints futuras usarão uma Activity/Fragment dedicada para
     * formulários mais complexos (Subtópicos com Markdown, imagem, tags...).
     */
    private void mostrarDialogNovaTrilha() {
        // Infla um EditText dinamicamente para colocar dentro do Dialog
        final EditText editTextNome = new EditText(this);
        editTextNome.setHint("Ex: Python, Power BI, Logística...");
        editTextNome.setSingleLine(true);
        // Padding interno para o EditText não colar nas bordas do Dialog
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        editTextNome.setPadding(padding, padding, padding, padding);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Nova Trilha de Conhecimento")
                .setMessage("Digite o nome da área de estudo:")
                .setView(editTextNome)
                .setNegativeButton("Cancelar", null) // null = apenas fecha o dialog
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String nome = editTextNome.getText().toString().trim();

                    // Validação: não salva nomes vazios ou só com espaços
                    if (TextUtils.isEmpty(nome)) {
                        Snackbar.make(
                                findViewById(android.R.id.content),
                                "O nome não pode ser vazio!",
                                Snackbar.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    // Cria o objeto e envia ao ViewModel → Repository → DAO → Room
                    // A UI se atualiza automaticamente via LiveData. Zero código extra.
                    Trilha novaTrilha = new Trilha(nome);
                    trilhaViewModel.insert(novaTrilha);
                })
                .show();
    }
}