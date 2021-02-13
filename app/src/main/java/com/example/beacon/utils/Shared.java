package com.example.beacon.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Shared {

    /**
     * Grava na memoria interna do aparelho o valor.
     * @param context
     *          Activity solicitante.
     * @param key
     *          Chave de identificação.
     * @param value
     *          Valor a ser armazenado.
     */
    public static final void putString(final Context context, final String key, final String value) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    /**
     * Retorna uma String que está armazenada na memória interna.
     * @param context
     *          Activity solicitante.
     * @param key
     *          Chave a ser encontrada.
     * @return
     */
    public static final String getString(final Context context, final String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, "");
    }

    /**
     * Grava na memoria interna do aparelho o valor.
     * @param context
     *          Activity solicitante.
     * @param key
     *          Chave de identificação.
     * @param value
     *          Valor a ser armazenado.
     */
    public static final void putBoolean(final Context context, final String key, final boolean value) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();

    }

    /**
     * Retorna uma String que está armazenada na memória interna.
     * @param context
     *          Activity solicitante.
     * @param key
     *          Chave a ser encontrada.
     * @return
     */
    public static final boolean getBoolean(final Context context, final String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false);
    }


    /**
     * Grava na memoria interna do aparelho o valor.
     * @param context
     *          Activity solicitante.
     * @param key
     *          Chave de identificação.
     * @param value
     *          Valor a ser armazenado.
     */
    public static final void putInt(final Context context, final String key, final int value) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();

    }

    /**
     * Retorna uma String que está armazenada na memória interna.
     * @param context
     *          Activity solicitante.
     * @param key
     *          Chave a ser encontrada.
     * @return
     */
    public static final int getInt(final Context context, final String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, 0);
    }

    /**
     * Grava na memoria interna do aparelho o valor.
     * @param context
     *          Activity solicitante.
     * @param key
     *          Chave de identificação.
     * @param value
     *          Valor a ser armazenado.
     */
    public static final void putLong(final Context context, final String key, final long value) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();

    }

    /**
     * Retorna uma String que está armazenada na memória interna.
     * @param context
     *          Activity solicitante.
     * @param key
     *          Chave a ser encontrada.
     * @return
     */
    public static final long getLong(final Context context, final String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(key, 0);
    }
}
