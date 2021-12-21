package com.pokemon.Components;


import static com.pokemon.Components.Utility.isPrimeNumber;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.pokemon.HomeActivity;
import com.pokemon.R;

import java.util.List;
import java.util.Random;

public class RecyclerviewAdapterMyPokemon extends RecyclerView.Adapter<RecyclerViewHolderPoke> {

    public List<ItemObject> itemList;

    public RecyclerviewAdapterMyPokemon(List<ItemObject> itemLists) {
        itemList = itemLists;
    }

    public List<ItemObject> getItemList() {
        return itemList;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public RecyclerViewHolderPoke onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout layoutView = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_list_store_cart, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 10, 0, 10);
        layoutView.setLayoutParams(lp);
        return new RecyclerViewHolderPoke(layoutView);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolderPoke holder, int position) {/*final int position*/
        try {
            Resources res = holder.root.getResources();
            try {
                Bitmap bitmap = itemList.get(holder.getAbsoluteAdapterPosition()).getBitmap();
                RoundedBitmapDrawable roundBMP = RoundedBitmapDrawableFactory.create(res, bitmap);
                roundBMP.setCornerRadius(25f);
                holder.tv_foto.setBackground(roundBMP);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            holder.tv_nama.setText(itemList.get(holder.getAbsoluteAdapterPosition()).getItem(0));
            holder.tv_nick.setText(itemList.get(holder.getAbsoluteAdapterPosition()).getItem(1));
            holder.url = itemList.get(holder.getAbsoluteAdapterPosition()).getItem(2);
            holder.jenisLayout = itemList.get(holder.getAbsoluteAdapterPosition()).getItem(3);
            holder.root.setOnClickListener(v ->
            {
                if (holder.jenisLayout.matches("1")) {
                    HomeActivity.showDialogStatus(
                            1,
                            holder.getAbsoluteAdapterPosition(),
                            new ItemObject(
                                    itemList.get(holder.getAbsoluteAdapterPosition()).getBitmap(),
                                    new String[]{
                                            itemList.get(holder.getAbsoluteAdapterPosition()).getItem(0),
                                            itemList.get(holder.getAbsoluteAdapterPosition()).getItem(1),
                                            itemList.get(holder.getAbsoluteAdapterPosition()).getItem(2),
                                            "2"}
                            )
                    );
                } else {
                    HomeActivity.showDialogStatus(
                            2,
                            holder.getAbsoluteAdapterPosition(),
                            new ItemObject(
                                    itemList.get(holder.getAbsoluteAdapterPosition()).getBitmap(),
                                    new String[]{
                                            itemList.get(holder.getAbsoluteAdapterPosition()).getItem(0),
                                            itemList.get(holder.getAbsoluteAdapterPosition()).getItem(1),
                                            itemList.get(holder.getAbsoluteAdapterPosition()).getItem(2),
                                            "2"}
                            )
                    );
                }
            });
            if (holder.jenisLayout.matches("1")) {
                holder.btn_release.setVisibility(View.GONE);
                holder.tv_nick.setVisibility(View.GONE);
            } else {
                holder.tv_nick.setText(itemList.get(holder.getAbsoluteAdapterPosition()).getItem(1));
                holder.tv_nick.setVisibility(View.VISIBLE);
                holder.btn_release.setVisibility(View.VISIBLE);
            }
            holder.btn_release.setOnClickListener(v ->
            {
                int x = new Random().nextInt(90);
                if (isPrimeNumber(x)) {
                    Toast.makeText(v.getContext(), x + " adalah bilangan prima", Toast.LENGTH_SHORT).show();
                    itemList.remove(holder.getAbsoluteAdapterPosition());
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(v.getContext(), x + " bukan bilangan prima", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            return itemList.size();
        } catch (NullPointerException ignored) {
        }
        return 0;
    }
}

class RecyclerViewHolderPoke extends RecyclerView.ViewHolder {
    LinearLayout root;
    TextView tv_foto, tv_nama, tv_nick, btn_release;
    String jenisLayout;
    String url;

    RecyclerViewHolderPoke(View itemView) {
        super(itemView);
        root = itemView.findViewById(R.id.root);
        tv_foto = itemView.findViewById(R.id.tv_foto);
        tv_nama = itemView.findViewById(R.id.tv_nama);
        tv_nick = itemView.findViewById(R.id.tv_nick);
        btn_release = itemView.findViewById(R.id.btn_release);
    }
}