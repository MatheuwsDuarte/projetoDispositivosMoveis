package com.example.projetodispositivosmoveis.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * ENTIDADE: Trilha
 * Representa as grandes áreas de estudo do usuário.
 * Exemplos: "Python", "Power BI", "Logística WEG".
 *
 * @Entity(tableName = "trilhas") → cria a tabela "trilhas" no SQLite.
 */
@Entity(tableName = "trilhas")
public class Trilha {
    /**
     * @PrimaryKey(autoGenerate = true):
     * O banco atribui IDs únicos e crescentes (1, 2, 3...) automaticamente.
     * Você NUNCA define o ID manualmente ao criar uma Trilha.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * @ColumnInfo(name = "nome"):
     * Define explicitamente o nome da coluna no banco.
     * Boa prática — evita surpresas se você renomear o campo Java no futuro.
     */
    @ColumnInfo(name = "nome")
    private String nome;

    // -------------------------------------------------------
    // CONSTRUTOR
    // O Room exige um construtor que corresponda às colunas.
    // O ID é excluído pois é autogerado.
    // -------------------------------------------------------
    public Trilha(String nome) {
        this.nome = nome;
    }

    // -------------------------------------------------------
    // GETTERS E SETTERS
    // O Room usa getters para ler dados e setId() para
    // preencher o ID automaticamente após a inserção.
    // -------------------------------------------------------
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
