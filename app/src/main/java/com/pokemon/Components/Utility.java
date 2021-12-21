package com.pokemon.Components;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.pokemon.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Utility {
    public static GradientDrawable roundRecWhite(int rad, String strokeColor, String feelColor) {
// return roundRect(Color.rgb(255, 255, 255), rad, Color.parseColor("#000000"));
        return roundRect(rad, Color.parseColor(strokeColor), Color.parseColor(feelColor));
    }

    public static void logd(String x, String y) {
        Log.d(x, y);
    }

    public static boolean cekKoneksi(Context context, boolean show_snackbar, boolean show_toast) {
        boolean konek = false; /*TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);*/
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE); /*NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);*/ /*NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);*/ /*if (wifiNetwork != null && wifiNetwork.isConnected()){*/ /*konek = true;*/ /*Logd("koneksi ", "cekKoneksi wifi " + konek);*/ /*} else if (mobileNetwork != null && mobileNetwork.isConnected()){*//*konek = true;Logd("koneksi ", "cekKoneksi hp " + konek);}NetworkInfo activeNetwork = cm.getActiveNetworkInfo();if (activeNetwork != null && activeNetwork.isConnected()){konek = true;Logd("koneksi ", "cekKoneksi jaringan aktif " + konek);}*/
        boolean isWifiConnected = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean is3gConnected = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        try {
            Ion.with(context).load("https://www.google.com").asString().get();
            if ((isWifiConnected || is3gConnected) && activeNetworkInfo != null && activeNetworkInfo.isConnected())
                konek = true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            if (show_snackbar) {
                noInternetSnackbar(context).show();
            } else if (show_toast) {
                Toast.makeText(context, "Anda belum terhubung dengan Internet", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return konek;
    }

    public static Snackbar noInternetSnackbar(Context context) {
        View my_view = ((AppCompatActivity) context).findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(my_view, "No Connection Internet", Snackbar.LENGTH_INDEFINITE);
        snackbar.setTextColor(Color.parseColor("#fafafa"));
        snackbar.setBackgroundTint(Color.parseColor("#ffffff"));
        snackbar.setAction("X", view -> snackbar.dismiss());
        return snackbar;
    }

    static boolean isPrimeNumber(int n) {
        int i;
        int num;
        for (i = 1; i <= n; i++) {
            int counter = 0;
            for (num = i; num >= 1; num--) {
                if (i % num == 0) {
                    counter = counter + 1;
                }
            }
            if (counter == 2) {
                if (i == n) {
                    return true;
                }
            }
        }
        return false;
    }

    public static GradientDrawable roundRect(int rad, int strokeColor, int feelColor) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(feelColor);
        gd.setCornerRadius(rad);
        gd.setStroke(1, strokeColor);
        return gd;
    }

    public static Dialog DialogLoading(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_loading);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        ProgressBar progressBar = dialog.findViewById(R.id.progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.biru), android.graphics.PorterDuff.Mode.SRC_ATOP);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.x = 0;
        lp.y = 0;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    public static Dialog DialogStatus(Context context,
                                      int id_si_pemanggil,
                                      ItemObject io,
                                      List<ItemObject> rowListItemPoke,
                                      int lokasi_item_di_list_poke,
                                      RecyclerviewAdapterMyPokemon adapter_poke) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_status);
        int click[] = {0};
        TextView tv_foto_poke, tv_nama_poke, tv_nick_poke, btn_rename, tv_move, tv_type, btn_catch, btn_save;
        tv_foto_poke = dialog.findViewById(R.id.tv_foto_poke);
        tv_nama_poke = dialog.findViewById(R.id.tv_nama_poke);
        tv_nick_poke = dialog.findViewById(R.id.tv_nick_poke);
        btn_rename = dialog.findViewById(R.id.btn_rename);
        Resources res = context.getResources();
        Bitmap bitmap = null;
        try {
            bitmap = io.getBitmap();
            RoundedBitmapDrawable roundBMP = RoundedBitmapDrawableFactory.create(res, bitmap);
            roundBMP.setCornerRadius(25f);
            tv_foto_poke.setBackground(roundBMP);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        tv_nama_poke.setText(io.getItem(0));
        tv_nick_poke.setText(io.getItem(1));
        tv_move = dialog.findViewById(R.id.tv_move);
        tv_type = dialog.findViewById(R.id.tv_type);
        String move = "", type = "";
//moves arr,geti,aso,get move aso,get name asst
        JsonObject all;
        try {
            all = Ion.with(context).load(io.getItem(2)).asJsonObject().get();
            for (int i = 0; i < all.get("moves").getAsJsonArray().size(); i++) {
                move += all.get("moves").getAsJsonArray().get(i).getAsJsonObject().get("move").getAsJsonObject().get("name").getAsString() + ", ";
            }
            for (int j = 0; j < all.get("types").getAsJsonArray().size(); j++) {
                type += all.get("types").getAsJsonArray().get(j).getAsJsonObject().get("type").getAsJsonObject().get("name").getAsString() + ", ";
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        tv_move.setText(move);
        tv_type.setText(type);
        Bitmap finalBitmap = bitmap;
        btn_save = dialog.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(v -> {
            rowListItemPoke.set(lokasi_item_di_list_poke, new ItemObject(
                    finalBitmap, new String[]{
                    io.getItem(0),
                    tv_nick_poke.getText().toString(),
                    io.getItem(2),
                    "2"}
            ));
            adapter_poke.notifyDataSetChanged();
        });
        btn_catch = dialog.findViewById(R.id.btn_catch);
        btn_catch.setOnClickListener(v -> {
            ++click[0];
            if (click[0] % 2 == 0) {
                DialogRenameName(context, io, rowListItemPoke, adapter_poke).show();
                dialog.dismiss();
            }
        });
        int n_rename[] = {0};
        int limit = 1000, past, current, fibonacci;
        past = 1;
        current = 0;
        fibonacci = 1;
        int fibArr[] = new int[1001];
        System.out.println("awalnya");
        for (int i = 0; i <= limit; i++) {
            fibArr[i] = current;
            System.out.print(current + ", ");
            fibonacci = past + current;
            past = current;
            current = fibonacci;
        }
        btn_rename.setOnClickListener(v -> {
            tv_nick_poke.setText(tv_nama_poke.getText().toString() + "-" + fibArr[n_rename[0]++]);
        });
        if (id_si_pemanggil == 1) {
            tv_nick_poke.setVisibility(View.GONE);
            btn_rename.setVisibility(View.GONE);
            btn_catch.setVisibility(View.VISIBLE);
            btn_save.setVisibility(View.GONE);
        } else {
            tv_nick_poke.setVisibility(View.VISIBLE);
            btn_rename.setVisibility(View.VISIBLE);
            btn_catch.setVisibility(View.GONE);
            btn_save.setVisibility(View.VISIBLE);
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.x = 0;
        lp.y = 0;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    public static Dialog DialogRenameName(Context context,
                                          ItemObject io,
                                          List<ItemObject> rowListItemPoke,
                                          RecyclerviewAdapterMyPokemon adapter_poke) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rename_name);
        TextView btn_save;
        EditText et_nick_poke;
        et_nick_poke = dialog.findViewById(R.id.et_nick_poke);
        Resources res = context.getResources();
        Bitmap bitmap = null;
        RoundedBitmapDrawable roundBMP;
        try {
            bitmap = io.getBitmap();
            roundBMP = RoundedBitmapDrawableFactory.create(res, bitmap);
            roundBMP.setCornerRadius(25f);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        et_nick_poke.setText(io.getItem(1));
//moves arr,geti,aso,get move aso,get name asst
        Bitmap finalBitmap = bitmap;
        btn_save = dialog.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(v -> {
            rowListItemPoke.add(new ItemObject(
                    finalBitmap, new String[]{
                    io.getItem(0),
                    et_nick_poke.getText().toString(),
                    io.getItem(2),
                    "2"}
            ));
            adapter_poke.notifyDataSetChanged();
            dialog.dismiss();
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.x = 0;
        lp.y = 0;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }
}