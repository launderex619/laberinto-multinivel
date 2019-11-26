package com.example.sulemaia.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.example.sulemaia.Helper.Constants;
import com.example.sulemaia.R;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

/**
 * Class for the creation and implementation of the main tutorial that pops when the
 * app is running for the first time, along with some images to show examples.
 */
public class Tutorial extends IntroActivity {

    private int images[] = {
            R.drawable.tut0,
            R.drawable.tut1,
            R.drawable.tut2,
            R.drawable.tut3
    };
    private String descriptions[] = {
            "En este breve tutorial aprenderás las nociones básicas del juego, y posteriormente el sistema te guiará paso a paso. Desliza para continuar.",
            "En ésta área, podrás configurar aspectos como: Casilla Inicial y Final del juego, y los colores y nombres de los terrenos.",
            "Aquí, podrás seleccionar a tu personaje para jugar, así como editar sus características de movimiento, nombre e ícono.",
            "Finalmente, aquí tendrás tu área de juego cargada con todas las configuraciones previas que seleccionaste. ¡A jugar!"
    };
    private String titles[] = {
            "¡Bienvenido a la app!",
            "Configurando el juego",
            "Configurando los personajes",
            "Area de juego"
    };

    /**
     * On the creation of the tutorial, we activate and deactivate certain buttons, for it to
     * work accordingly to the normal flow, and show specific images with their description.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullscreen(true);
        super.onCreate(savedInstanceState);
        setButtonBackVisible(false);
        setButtonNextVisible(true);
        setButtonCtaVisible(true);
        setButtonCtaLabel(R.string.skip_tutorial);
        // setButtonCtaTintMode(R.color.secondaryLightColor);


        for (int i = 0; i < images.length; i++) {
            addSlide(new SimpleSlide.Builder()
                    .title(titles[i])
                    .description(descriptions[i])
                    .image(images[i])
                    .background(R.color.secondaryColor)
                    .backgroundDark(R.color.secondaryDarkColor)
                    .build());
        }

    }

}
