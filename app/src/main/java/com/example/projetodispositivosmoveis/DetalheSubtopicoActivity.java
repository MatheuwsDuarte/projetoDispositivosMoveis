package com.example.projetodispositivosmoveis;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.projetodispositivosmoveis.ui.subtopico.SubtopicoViewModel;
import com.google.android.material.button.MaterialButton;

// Apenas o core do Markwon — sem syntax-highlight por enquanto
import io.noties.markwon.Markwon;

/**
 * @PrismBundle: Anotação do prism4j-bundler.
 * O annotationProcessor lê isso em tempo de compilação e gera
 * automaticamente a classe 'GrammarLocatorDef' que ensina ao
 * Prism4j quais linguagens de código ele deve conhecer.
 * Adicione aqui as linguagens que você usa nas suas anotações.
 */

public class DetalheSubtopicoActivity extends AppCompatActivity {
    public static final String EXTRA_SUBTOPICO_ID = "EXTRA_SUBTOPICO_ID";

    private String conteudoMarkdownBruto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_subtopico);

        int subtopicoId = getIntent().getIntExtra(EXTRA_SUBTOPICO_ID, -1);
        if (subtopicoId == -1) { finish(); return; }

        // --- TOOLBAR ---
        Toolbar toolbar = findViewById(R.id.toolbarDetalhe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // --- REFERÊNCIAS ---
        TextView textViewConteudo   = findViewById(R.id.textViewConteudoMarkdown);
        TextView textViewTags       = findViewById(R.id.textViewTagsDetalhe);
        MaterialButton buttonCopiar = findViewById(R.id.buttonCopiarConteudo);

        // -------------------------------------------------------
        // MARKWON — versão simplificada apenas com o core.
        //
        // O Markwon.create() já suporta:
        //   **negrito**, *itálico*, # Títulos, listas, > citações
        //   e blocos de código ```  com fonte monospace.
        //
        // O syntax highlight colorido será adicionado em Sprint
        // futura quando o prism4j estiver disponível no repositório.
        // -------------------------------------------------------
        Markwon markwon = Markwon.create(this);

        // --- VIEWMODEL ---
        SubtopicoViewModel viewModel = new ViewModelProvider(this)
                .get(SubtopicoViewModel.class);

        viewModel.getSubtopicoById(subtopicoId).observe(this, subtopico -> {
            if (subtopico == null) { finish(); return; }

            // Título na Toolbar
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(subtopico.getTitulo());
            }

            // Tags formatadas como #tag
            String tags = subtopico.getTags();
            if (tags != null && !tags.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String tag : tags.split(",")) {
                    sb.append("#").append(tag.trim()).append("  ");
                }
                textViewTags.setText(sb.toString().trim());
            }

            // Guarda o Markdown bruto para o botão copiar
            conteudoMarkdownBruto = subtopico.getConteudoMarkdown() != null
                    ? subtopico.getConteudoMarkdown() : "";

            // Renderiza o Markdown no TextView
            markwon.setMarkdown(textViewConteudo, conteudoMarkdownBruto);
        });

        // -------------------------------------------------------
        // BOTÃO COPIAR — ClipboardManager
        //
        // ClipboardManager é o serviço do Android para a área
        // de transferência. O fluxo é sempre:
        //   1. Obter o serviço via getSystemService()
        //   2. Criar um ClipData com o texto
        //   3. Definir como clip primário (setPrimaryClip)
        // -------------------------------------------------------
        buttonCopiar.setOnClickListener(v -> copiarParaClipboard(conteudoMarkdownBruto));
    }

    private void copiarParaClipboard(String texto) {
        if (texto.isEmpty()) {
            Toast.makeText(this, "Nada para copiar!", Toast.LENGTH_SHORT).show();
            return;
        }

        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText("Conteúdo do Subtópico", texto);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this,
                "✅ Copiado para a área de transferência!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
