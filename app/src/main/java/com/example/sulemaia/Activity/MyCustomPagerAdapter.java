package com.example.sulemaia.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.sulemaia.R;

public class MyCustomPagerAdapter extends PagerAdapter {

    Context context;
    int images[];
    LayoutInflater layoutInflater;

    public MyCustomPagerAdapter(Context context, int[] images) {
        this.context = context;
        this.images = images;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView=layoutInflater.inflate(R.layout.item, container, false);
        //Usamos la clase layout y creamos una View

        //Hay que encontrar el imageView correspondiente
        ImageView imageView = (ImageView)itemView.findViewById(R.id.imageView);

        //Agregamos todas las imagenes al imageView
        imageView.setImageResource(images[position]);

        container.addView(itemView);

        //Clicklistener para la posicion de la imagen
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        return itemView;
    }
}
