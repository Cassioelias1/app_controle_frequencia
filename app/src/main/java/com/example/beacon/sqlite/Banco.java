package com.example.beacon.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Banco extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "banco_sqlite.db";
    private static final int VERSAO = 1;

    public Banco(@Nullable Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS frequencias (");
        sql.append("id integer primary key autoincrement,");
        sql.append("data text,");
        sql.append("academico_id text,");
        sql.append("turma_id text,");
        sql.append("status text,");
//        sql.append("id_beacon_1 text,");
//        sql.append("id_beacon_2 text,");
//        sql.append("id_beacon_3 text,");
        sql.append("material_card_id text,");
        sql.append("text_view_id text);");

        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS frequencias");
        onCreate(db);
    }
}
