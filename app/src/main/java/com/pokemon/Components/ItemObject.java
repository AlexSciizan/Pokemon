package com.pokemon.Components;

import android.graphics.Bitmap;

public class ItemObject {

    private String[] isiList;
    private Bitmap bmp;

    public ItemObject(String[] isi_list) {
        isiList = isi_list;
    }
    // bmp , nama , url , jenis
    public ItemObject(Bitmap _bmp, String[] isi_list)
    {
        bmp=_bmp;
        isiList = isi_list;
    }

    public String getItem(int i) {
        return isiList[i];
    }

    public Bitmap getBitmap() {
        return bmp;
    }
}

