package com.example.beacon.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.beacon.api.models.Presenca;

import java.util.ArrayList;
import java.util.List;

public class BancoController {

    private SQLiteDatabase db;
    private Banco banco;

    public BancoController(Context context){
        banco = new Banco(context);
    }

    public boolean save(String data, String academicoId, String turmaId, String status, Integer materialCardId, Integer textViewId){
        System.out.println("----------------------------------------------------------------------");
        System.out.println("FAZENDO SAVE NO SQLITE");
        System.out.println("----------------------------------------------------------------------");
        ContentValues valores;
        long result;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put("data", data);
        valores.put("academico_id", academicoId);
        valores.put("turma_id", turmaId);
        valores.put("status", status);
        valores.put("material_card_id", materialCardId);
        valores.put("text_view_id", textViewId);

        result = db.insert("frequencias", null, valores);
        db.close();

        return result != -1;
    }

    public List<Presenca> list(){
        db = banco.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM frequencias;", null);

        if(cursor == null){
            return null;
        }

        cursor.moveToFirst();

        List<Presenca> presencas = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            Presenca presenca = new Presenca();
            presenca.setData(cursor.getString(cursor.getColumnIndex("data")));
            presenca.setIdAcademico(cursor.getString(cursor.getColumnIndex("academico_id")));
            presenca.setIdTurma(cursor.getString(cursor.getColumnIndex("turma_id")));
            presenca.setStatus(cursor.getString(cursor.getColumnIndex("status")));
            presenca.setMaterialCardId(cursor.getString(cursor.getColumnIndex("material_card_id")));
            presenca.setTextViewId(cursor.getString(cursor.getColumnIndex("text_view_id")));
            presenca.setIdSqlite(cursor.getInt(cursor.getColumnIndex("id")));

            presencas.add(presenca);
            cursor.moveToNext();
        }
        return presencas;
    }

    public void delete(int id){
        db = banco.getWritableDatabase();
        db.execSQL("DELETE FROM frequencias WHERE id = "+id);
        db.close();
    }
}
