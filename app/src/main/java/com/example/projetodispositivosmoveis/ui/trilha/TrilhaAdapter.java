package com.example.projetodispositivosmoveis.ui.trilha;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetodispositivosmoveis.R;
import com.example.projetodispositivosmoveis.data.model.Trilha;

/**
 * TrilhaAdapter — "tradutor" entre a lista de dados e as Views na tela.
 *
 * Por que ListAdapter em vez de RecyclerView.Adapter simples?
 * O ListAdapter usa DiffUtil internamente: ele COMPARA a lista antiga
 * com a nova e anima APENAS os itens que mudaram.
 * Isso é mais eficiente e bonito do que recarregar tudo com notifyDataSetChanged().
 */
public class TrilhaAdapter extends ListAdapter<Trilha, TrilhaAdapter.TrilhaViewHolder> {

    // Interface de callback — o Adapter avisa a Activity quando
    // um item é clicado, sem precisar ter referência à Activity.
    // Isso mantém o Adapter desacoplado e reutilizável.
    public interface OnTrilhaClickListener {
        void onTrilhaClick(Trilha trilha);

        void onTrilhaLongClick(Trilha trilha); // segura para deletar
    }

    private OnTrilhaClickListener listener;

    /**
     * DIFFUTIL CALLBACK:
     * O ListAdapter usa isso para saber SE e O QUE mudou na lista.
     * areItemsTheSame: "são o mesmo item?" → compara IDs (identidade)
     * areContentsTheSame: "o conteúdo mudou?" → compara o nome (igualdade)
     */
    private static final DiffUtil.ItemCallback<Trilha> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Trilha>() {

                @Override
                public boolean areItemsTheSame(@NonNull Trilha oldItem,
                                               @NonNull Trilha newItem) {
                    // Mesmo ID = mesmo item na lista (mesmo que o nome tenha mudado)
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Trilha oldItem,
                                                  @NonNull Trilha newItem) {
                    // Conteúdo igual = não precisa redesenhar este item
                    return oldItem.getNome().equals(newItem.getNome());
                }
            };

    // O construtor passa o DiffCallback para o pai (ListAdapter)
    public TrilhaAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setOnTrilhaClickListener(OnTrilhaClickListener listener) {
        this.listener = listener;
    }

    /**
     * onCreateViewHolder — chamado quando o RecyclerView PRECISA de um novo "molde".
     * Pense como: "fabrique um novo assento de trem vazio".
     * Só é chamado para criar Views novas (não para cada item da lista!).
     */
    @NonNull
    @Override
    public TrilhaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater transforma o XML item_trilha.xml em um objeto View real
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trilha, parent, false);
        return new TrilhaViewHolder(itemView);
    }

    /**
     * onBindViewHolder — chamado para PREENCHER um "molde" com dados reais.
     * Pense como: "coloque o nome do passageiro no assento da posição X".
     * É chamado cada vez que um item fica visível na tela.
     */
    @Override
    public void onBindViewHolder(@NonNull TrilhaViewHolder holder, int position) {
        // getItem() é método do ListAdapter — retorna o item na posição atual
        Trilha trilhaAtual = getItem(position);
        holder.bind(trilhaAtual, listener);
    }

    // =========================================================
    // VIEWHOLDER — "molde" que guarda referências às Views do item.
    //
    // Por que existe? Sem o ViewHolder, o RecyclerView chamaria
    // findViewById() toda vez que um item aparecesse na tela.
    // O ViewHolder guarda essas referências em memória, tornando
    // a rolagem da lista muito mais fluida.
    // =========================================================
    static class TrilhaViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewNome;
        private final TextView textViewSubtitulo;

        TrilhaViewHolder(@NonNull View itemView) {
            super(itemView);
            // Busca as Views UMA VEZ e guarda na memória do ViewHolder
            textViewNome = itemView.findViewById(R.id.textViewNomeTrilha);
            textViewSubtitulo = itemView.findViewById(R.id.textViewSubtituloTrilha);
        }

        /**
         * bind() — preenche este ViewHolder com os dados de uma Trilha específica.
         * Separamos em método próprio para deixar o onBindViewHolder limpo.
         */
        void bind(Trilha trilha, OnTrilhaClickListener listener) {
            textViewNome.setText(trilha.getNome());
            // Subtítulo fixo por enquanto — Sprint futura pode mostrar contagem real
            textViewSubtitulo.setText("Toque para ver os conteúdos");

            // Click simples → navega para a tela de Subtópicos
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onTrilhaClick(trilha);
            });

            // Click longo → opção de deletar
            itemView.setOnLongClickListener(v -> {
                if (listener != null) listener.onTrilhaLongClick(trilha);
                return true; // true = evento consumido, não propaga
            });
        }
    }
}



