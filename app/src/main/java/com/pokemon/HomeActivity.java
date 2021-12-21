package com.pokemon;

import static com.pokemon.Components.Api.getPokeImg;
import static com.pokemon.Components.Utility.DialogLoading;
import static com.pokemon.Components.Utility.DialogStatus;
import static com.pokemon.Components.Utility.roundRecWhite;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.JsonObject;
import com.pokemon.Components.Api;
import com.pokemon.Components.ItemObject;
import com.pokemon.Components.RecyclerviewAdapterMyPokemon;
import com.pokemon.Components.viewpager_containing_layout.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    // store itu barang2 yang ada di toko
    // cart itu barang yang dibeli dan udah masuk kekeranjang belanja user
    LinearLayout btn_1, btn_2;

    @SuppressLint("StaticFieldLeak")
    static RecyclerviewAdapterMyPokemon adapterPokemon;
    static RecyclerView recyclerViewPokemon;
    static List<ItemObject> rowListItemPokemon = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    static RecyclerviewAdapterMyPokemon adapterMy;
    static RecyclerView recyclerViewMy;
    static List<ItemObject> rowListItemMy = new ArrayList<>();

    static Dialog dialog;
    ViewPager viewPager;
    ViewPagerAdapter vpa4;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dialog = DialogLoading(HomeActivity.this);

        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        btn_1.setBackground(roundRecWhite(9, "#fafafa", "#ffffa9"));
        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);
        btn_2.setBackground(roundRecWhite(9, "#fafafa", "#FFD580"));

        recyclerViewPokemon = new RecyclerView(getBaseContext());
        recyclerViewMy = new RecyclerView(getBaseContext());

        adapterPokemon = new RecyclerviewAdapterMyPokemon(rowListItemPokemon);
        adapterMy = new RecyclerviewAdapterMyPokemon(rowListItemMy);

        LinearLayoutManager ll_cart = new LinearLayoutManager(getBaseContext());
        ll_cart.setOrientation(RecyclerView.VERTICAL);
        LinearLayoutManager ll_store = new LinearLayoutManager(getBaseContext());
        ll_store.setOrientation(RecyclerView.VERTICAL);

        recyclerViewPokemon.setNestedScrollingEnabled(true);
        recyclerViewPokemon.setLayoutManager(ll_store);
        recyclerViewMy.setNestedScrollingEnabled(true);
        recyclerViewMy.setLayoutManager(ll_cart);

        recyclerViewPokemon.setAdapter(adapterPokemon);
        recyclerViewPokemon.setHasFixedSize(true);
        recyclerViewMy.setAdapter(adapterMy);
        recyclerViewMy.setHasFixedSize(true);

        try {
            vpa4 = new ViewPagerAdapter(HomeActivity.this,
                    new RecyclerView[]{recyclerViewPokemon, recyclerViewMy});
            viewPager = findViewById(R.id.viewpager);
            viewPager.setAdapter(vpa4);
            viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (viewPager.getCurrentItem() == 0) {
                        btn_1.setBackground(roundRecWhite(9, "#fafafa", "#ffffa9"));
                        btn_2.setBackground(roundRecWhite(9, "#fafafa", "#FFD580"));
                    } else {
                        btn_1.setBackground(roundRecWhite(9, "#fafafa", "#FFD580"));
                        btn_2.setBackground(roundRecWhite(9, "#fafafa", "#ffffa9"));
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            new AsynGetPokemon(HomeActivity.this, rowListItemPokemon, adapterPokemon).execute(btn_1);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public static void showDialogStatus(int id_si_pemanggil, int lokasi_item_di_list, ItemObject io) {
        //bmp,nama,url,jenis = 1, [0,1,2,3]
        if (id_si_pemanggil == 1) {
            DialogStatus(dialog.getContext(),
                    1,
                    io,
                    rowListItemMy,
                    lokasi_item_di_list,
                    adapterMy).show();
        } else {
            DialogStatus(dialog.getContext(),
                    2,
                    io,
                    rowListItemMy,
                    lokasi_item_di_list,
                    adapterMy).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_1) {
            viewPager.setCurrentItem(0, true);
            btn_1.setBackground(roundRecWhite(9, "#fafafa", "#ffffa9"));
            btn_2.setBackground(roundRecWhite(9, "#fafafa", "#FFD580"));
        }
        if (v.getId() == R.id.btn_2) {
            viewPager.setCurrentItem(1, true);
            btn_1.setBackground(roundRecWhite(9, "#fafafa", "#FFD580"));
            btn_2.setBackground(roundRecWhite(9, "#fafafa", "#ffffa9"));
        }
    }

    @SuppressLint("StaticFieldLeak")
    class AsynGetPokemon extends AsyncTask<LinearLayout, Void, Void> {
        Context context;
        Dialog dialog_loading;
        List<ItemObject> rowListItem;
        RecyclerviewAdapterMyPokemon adapter;

        public AsynGetPokemon() {

        }

        public AsynGetPokemon(Context context_, List<ItemObject> rowListItem_, RecyclerviewAdapterMyPokemon adapter_) {
            context = context_;
            dialog_loading = DialogLoading(context_);
            rowListItem = rowListItem_;
            adapter = adapter_;
        }

        @Override
        protected void onPreExecute() {
            dialog_loading.show();
            super.onPreExecute();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected Void doInBackground(LinearLayout... linearLayouts) {
            JsonObject[] jo_pokemon = new JsonObject[1];
            linearLayouts[0].post(() -> {
                jo_pokemon[0] = Api.getPokemon(getBaseContext(), 10);
                int jml_pokemon = jo_pokemon[0].get("results").getAsJsonArray().size();
                for (int i = 0; i < jml_pokemon; i++) {
// System.out.println(jo_pokemon[0].get("results").getAsJsonArray().get(i).getAsJsonObject().get("name").getAsString() + " ");
                    try {
                        rowListItem.add(new ItemObject(
                                getPokeImg(HomeActivity.this, i + 1),
                                new String[]{
                                jo_pokemon[0].get("results").getAsJsonArray().get(i).getAsJsonObject().get("name").getAsString(),
                                jo_pokemon[0].get("results").getAsJsonArray().get(i).getAsJsonObject().get("name").getAsString()+"-0",
                                jo_pokemon[0].get("results").getAsJsonArray().get(i).getAsJsonObject().get("url").getAsString(),
                                "1"}
                                ));
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                dialog_loading.dismiss();
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }

}