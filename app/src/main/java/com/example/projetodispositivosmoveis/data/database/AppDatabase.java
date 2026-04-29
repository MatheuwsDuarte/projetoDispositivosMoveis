package com.example.projetodispositivosmoveis.data.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.projetodispositivosmoveis.data.dao.SubtopicoDao;
import com.example.projetodispositivosmoveis.data.dao.TrilhaDao;
import com.example.projetodispositivosmoveis.data.model.Subtopico;
import com.example.projetodispositivosmoveis.data.model.Trilha;

/**
 * @Database: Instrui o Room sobre:
 *   entities → TODAS as tabelas. Nova @Entity? Adicione aqui.
 *   version  → Ao MUDAR a estrutura de uma tabela, incremente este
 *              número e forneça uma Migration. Caso contrário o app trava.
 *   exportSchema → false durante desenvolvimento. Em produção considere true.
 */
@Database(
        entities = {Trilha.class, Subtopico.class},
        version  = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    // volatile: visível a todas as threads corretamente
    private static volatile AppDatabase instance;
    private static final String DATABASE_NAME = "data_logic_vault_db";

    /**
     * Ponto de acesso global — Double-Checked Locking.
     * 'synchronized' garante que só uma thread cria o banco por vez.
     * A verificação dupla do 'if (instance == null)' evita que,
     * após a primeira thread criar a instância, outras a recriem.
     */
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(), // NUNCA use Activity aqui!
                                    AppDatabase.class,
                                    DATABASE_NAME
                            )
                            // Durante desenvolvimento: reconstrói o banco se a versão mudar.
                            // ATENÇÃO: apaga todos os dados. Remova em produção.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }

    // O Room implementa esses métodos automaticamente.
    // É assim que você acessa os DAOs no Repository.
    public abstract TrilhaDao trilhaDao();
    public abstract SubtopicoDao subtopicoDao();
}
