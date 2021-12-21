package com.pokemon.Components.viewpager_containing_layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.pokemon.R;

import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {
//    LinearLayout[] linearLayout;
    RecyclerView[] recyclerViews;
    LayoutInflater inflater;
    Context context;
    LinearLayout ll_main;

    public ViewPagerAdapter(Context context_, RecyclerView[] recyclerViews_) {
        recyclerViews = recyclerViews_;
        context = context_;
    }

    @Override
    public int getCount() {
        return recyclerViews.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ll_main = (LinearLayout) inflater.inflate(R.layout.viewpager_adapter_layout, container, false);
        Objects.requireNonNull(container).addView(ll_main);
        recyclerViews[position].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        try {
            if (recyclerViews[position].getParent() != null) {
                ((ViewGroup) recyclerViews[position].getParent()).removeView(recyclerViews[position]);
                ll_main.addView(recyclerViews[position]);
            } else{
                ll_main.addView(recyclerViews[position]);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return ll_main;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}