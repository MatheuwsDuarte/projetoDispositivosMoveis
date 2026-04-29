package com.example.projetodispositivosmoveis.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * ENTIDADE: Subtopico (chamado "Insight" na UI)
 * Representa uma anotação dentro de uma Trilha.
 *
 * foreignKeys: Define a relação com a tabela "trilhas".
 * indices: Cria um índice em "trilha_id" para buscas mais rápidas.
 *          O Room também EXIGE índice em colunas de ForeignKey.
 */
@Entity(
        tableName = "subtopicos",
        foreignKeys = @ForeignKey(
                entity    = Trilha.class,
                parentColumns = "id",         // coluna na tabela PAI
                childColumns  = "trilha_id",  // coluna NESTA tabela
                onDelete  = ForeignKey.CASCADE
        ),
        indices = { @Index("trilha_id") }
)
public class Subtopico {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "titulo")
    private String titulo;

    /**
     * Armazena texto em formato Markdown.
     * Ex: "## Fórmula\n```dax\nCALCULATE(...)\n```"
     * Room salva como TEXT no SQLite sem problemas.
     */
    @ColumnInfo(name = "conteudo_markdown")
    private String conteudoMarkdown;

    /**
     * Caminho local para uma imagem no dispositivo.
     * Não salvamos a imagem no banco (ficaria enorme!),
     * apenas o caminho para o arquivo.
     * Ex: "/storage/emulated/0/Pictures/dvault_img1.jpg"
     */
    @ColumnInfo(name = "caminho_imagem")
    private String caminhoImagem;

    /**
     * Room armazena boolean como INTEGER (0 ou 1) no SQLite.
     * false = 0 (não favoritado), true = 1 (favoritado).
     */
    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;

    /**
     * A CHAVE ESTRANGEIRA: deve conter um ID que EXISTA em "trilhas".
     * O SQLite rejeita qualquer inserção com trilha_id inexistente.
     */

    /**
     * [NOVIDADE - Sprint 2]
     * Campo de tags para futura integração com IA.
     * Armazena palavras-chave separadas por vírgula.
     * Exemplo: "dax,powerbi,calculate,medida"
     *
     * Por que String e não uma tabela separada?
     * Para este app pessoal, a simplicidade vence.
     * Quando a IA for integrada, ela pode fazer split(",")
     * e trabalhar com o array resultante.
     */
    @ColumnInfo(name = "tags")
    private String tags;
    @ColumnInfo(name = "trilha_id")
    private int trilhaId;

    // -------------------------------------------------------
    // CONSTRUTOR (sem o id — autogerado)
    // -------------------------------------------------------
    public Subtopico(String titulo, String conteudoMarkdown,
                     String caminhoImagem, boolean isFavorite, int trilhaId) {
        this.titulo          = titulo;
        this.conteudoMarkdown = conteudoMarkdown;
        this.caminhoImagem   = caminhoImagem;
        this.isFavorite      = isFavorite;
        this.tags             = tags;
        this.trilhaId        = trilhaId;
    }

    // GETTERS E SETTERS
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getConteudoMarkdown() { return conteudoMarkdown; }
    public void setConteudoMarkdown(String conteudoMarkdown) { this.conteudoMarkdown = conteudoMarkdown; }

    public String getCaminhoImagem() { return caminhoImagem; }
    public void setCaminhoImagem(String caminhoImagem) { this.caminhoImagem = caminhoImagem; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public int getTrilhaId() { return trilhaId; }
    public void setTrilhaId(int trilhaId) { this.trilhaId = trilhaId; }
}
