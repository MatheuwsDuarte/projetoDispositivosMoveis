package com.example.projetodispositivosmoveis.ui.subtopico;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetodispositivosmoveis.R;
import com.example.projetodispositivosmoveis.data.model.Subtopico;
public class SubtopicoAdapter extends ListAdapter<Subtopico, SubtopicoAdapter.SubtopicoViewHolder> {
    public interface OnSubtopicoClickListener {
        void onSubtopicoClick(Subtopico subtopico);
        void onSubtopicoLongClick(Subtopico subtopico);
        void onFavoritoClick(Subtopico subtopico); // toggle de favorito
    }

    private OnSubtopicoClickListener listener;

    private static final DiffUtil.ItemCallback<Subtopico> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Subtopico>() {
                @Override
                public boolean areItemsTheSame(@NonNull Subtopico oldItem,
                                               @NonNull Subtopico newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Subtopico oldItem,
                                                  @NonNull Subtopico newItem) {
                    // Compara todos os campos relevantes para a UI
                    return oldItem.getTitulo().equals(newItem.getTitulo())
                            && oldItem.isFavorite() == newItem.isFavorite();
                }
            };

    public SubtopicoAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setOnSubtopicoClickListener(OnSubtopicoClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubtopicoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subtopico, parent, false);
        return new SubtopicoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubtopicoViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class SubtopicoViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewTitulo;
        private final TextView textViewPreview;
        private final TextView textViewTags;
        private final ImageView imageViewFavorito;

        SubtopicoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo    = itemView.findViewById(R.id.textViewTituloSubtopico);
            textViewPreview   = itemView.findViewById(R.id.textViewPreviewConteudo);
            textViewTags      = itemView.findViewById(R.id.textViewTags);
            imageViewFavorito = itemView.findViewById(R.id.imageViewFavorito);
        }

        void bind(Subtopico subtopico, OnSubtopicoClickListener listener) {
            textViewTitulo.setText(subtopico.getTitulo());

            // Prévia do conteúdo: mostra os primeiros 100 caracteres do Markdown
            String conteudo = subtopico.getConteudoMarkdown();
            textViewPreview.setText(
                    conteudo != null && !conteudo.isEmpty() ? conteudo : "Sem conteúdo"
            );

            // Tags: transforma "python,pandas" em "#python #pandas"
            String tags = subtopico.getTags();
            if (tags != null && !tags.isEmpty()) {
                // Divide por vírgula e adiciona # em cada tag
                String[] tagArray = tags.split(",");
                StringBuilder tagBuilder = new StringBuilder();
                for (String tag : tagArray) {
                    tagBuilder.append("#").append(tag.trim()).append("  ");
                }
                textViewTags.setText(tagBuilder.toString().trim());
                textViewTags.setVisibility(View.VISIBLE);
            } else {
                textViewTags.setVisibility(View.GONE);
            }

            // Ícone de favorito: estrela cheia ou vazia
            imageViewFavorito.setImageResource(
                    subtopico.isFavorite()
                            ? android.R.drawable.btn_star_big_on
                            : android.R.drawable.btn_star_big_off
            );

            // Cliques
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onSubtopicoClick(subtopico);
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) listener.onSubtopicoLongClick(subtopico);
                return true;
            });

            // Clique na estrela faz toggle do favorito
            imageViewFavorito.setOnClickListener(v -> {
                if (listener != null) listener.onFavoritoClick(subtopico);
            });
        }
    }
}
