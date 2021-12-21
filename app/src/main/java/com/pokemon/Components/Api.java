package com.pokemon.Components;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.pokemon.Components.Utility.cekKoneksi;
import static com.pokemon.Components.Utility.logd;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.pokemon.R;

import java.util.concurrent.ExecutionException;

public class Api {
    static String urls = "https://pokeapi.co/api/v2/";
    public static JsonObject getPokemon(Context context, int jml_pokemon) {
        JsonObject jo = new JsonObject();
        try {
            if (cekKoneksi(context, false, true)) {
                jo = Ion.with(context)
                        .load(urls + "/pokemon/?limit=" + jml_pokemon)
                        .setHeader("Content-Type", "application/x-www-form-urlencoded")
                        .setHeader("Accept", "application/json")
                        .asJsonObject().get();
                logd(TAG, "post " + jo.toString());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(context, "Anda belum terhubung dengan Jaringan", Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jo;
    }
    public static Bitmap getPokeImg(Context context, int idx_pokemon) throws ExecutionException, InterruptedException {
        return Ion.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + idx_pokemon + ".png")
                .withBitmap()
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .asBitmap().get();
    }

}