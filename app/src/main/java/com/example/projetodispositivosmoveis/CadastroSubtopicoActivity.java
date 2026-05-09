package com.example.projetodispositivosmoveis;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.projetodispositivosmoveis.data.model.Subtopico;
import com.example.projetodispositivosmoveis.ui.subtopico.SubtopicoViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroSubtopicoActivity extends AppCompatActivity {
    // Constantes públicas para as chaves dos extras do Intent.
    // 'public static final' permite que outras classes usem sem instanciar.
    public static final String EXTRA_TRILHA_ID   = "EXTRA_TRILHA_ID";
    public static final String EXTRA_TRILHA_NOME = "EXTRA_TRILHA_NOME";

    private SubtopicoViewModel subtopicoViewModel;

    // Campos de entrada
    private TextInputEditText editTextTitulo;
    private TextInputEditText editTextTags;
    private TextInputEditText editTextConteudo;

    // Dados recebidos da tela anterior
    private int trilhaId;
    private String trilhaNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_subtopico);

        // --- 1. RECUPERAR DADOS DO INTENT ---
        trilhaId   = getIntent().getIntExtra(EXTRA_TRILHA_ID, -1);
        trilhaNome = getIntent().getStringExtra(EXTRA_TRILHA_NOME);
        if (trilhaNome == null) trilhaNome = "Trilha";

        // Segurança: não abre sem um ID válido
        if (trilhaId == -1) {
            finish();
            return;
        }

        // --- 2. TOOLBAR ---
        Toolbar toolbar = findViewById(R.id.toolbarCadastro);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            // Título dinâmico mostra em qual trilha está criando
            getSupportActionBar().setTitle("Novo: " + trilhaNome);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // --- 3. REFERÊNCIAS AOS CAMPOS ---
        editTextTitulo   = findViewById(R.id.editTextTitulo);
        editTextTags     = findViewById(R.id.editTextTags);
        editTextConteudo = findViewById(R.id.editTextConteudoMarkdown);

        // --- 4. VIEWMODEL ---
        subtopicoViewModel = new ViewModelProvider(this)
                .get(SubtopicoViewModel.class);

        // --- 5. BOTÃO SALVAR ---
        MaterialButton buttonSalvar = findViewById(R.id.buttonSalvarSubtopico);
        buttonSalvar.setOnClickListener(v -> salvarSubtopico());
    }

    private void salvarSubtopico() {
        // Captura e limpa espaços extras dos campos
        String titulo   = editTextTitulo.getText() != null
                ? editTextTitulo.getText().toString().trim() : "";
        String tags     = editTextTags.getText() != null
                ? editTextTags.getText().toString().trim() : "";
        String conteudo = editTextConteudo.getText() != null
                ? editTextConteudo.getText().toString().trim() : "";

        // Validação: título é obrigatório
        if (TextUtils.isEmpty(titulo)) {
            // setError() mostra a mensagem de erro abaixo do campo
            editTextTitulo.setError("O título é obrigatório!");
            editTextTitulo.requestFocus();
            return;
        }

        // Cria o objeto Subtopico com todos os dados preenchidos
        Subtopico novoSubtopico = new Subtopico(
                titulo,
                conteudo,   // texto Markdown que será renderizado na tela de detalhe
                null,       // caminho de imagem — Sprint futura
                false,      // não favoritado por padrão
                tags,       // palavras-chave para busca e futura IA
                trilhaId    // chave estrangeira vinculando ao pai
        );

        // Envia ao ViewModel → Repository → ExecutorService → Room
        subtopicoViewModel.insert(novoSubtopico);

        Toast.makeText(this, "✅ Subtópico salvo!", Toast.LENGTH_SHORT).show();

        // Fecha esta Activity e volta para SubtopicosActivity
        // O LiveData vai atualizar a lista automaticamente
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
