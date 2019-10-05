package com.example.sulemaia.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.example.sulemaia.Helper.Constants;
import com.example.sulemaia.R;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class Tutorial extends IntroActivity {

    /*
    TODO: Man, varios detalles, 1: nunca se te olvide el encapsulamiento, si tienes variables que solo usaras en una clase, declaralas privadas, son buenas practicas, 2: No manches, me parti la madre creando una estructura en el proyecto para que metas un Adapter en la carpeta De activities, no manches xd, como diria tu tia cholis, me quitas esa chingadera de ahi, sigue el orden del proyecto, tambien ponle un nombre que represente lo que estas haciendo, MyCustomPager no dice nada solbre su comportamiento o a que va dirigido, ponte trucha man tkm
     */
    private int images[] = {
            R.drawable.calamar,
            R.drawable.tut1,
            R.drawable.tut2,
            R.drawable.tut3,
            R.drawable.tut4,
            R.drawable.tut5,
            R.drawable.tut6,
            R.drawable.tut7,
            R.drawable.tut8,
            R.drawable.tut9,
            R.drawable.tut10,
            R.drawable.tut11,
            R.drawable.tut12,
            R.drawable.tut13,
            R.drawable.tut14,
            R.drawable.tut15,
            R.drawable.tut16
    };
    private String descriptions[] = {
            "Aqui agrega las descripciones para cada imagen plox, no se como las vayas a meter pd, para que este texto salga, la imagen debe ser mas o menos del tamaño del calamar, fijate en las siguientes para que veas"
    };
    private String titles[] = {
            "Aqui van los titulos de las imagenes, administralas como quieras xd,"
    };

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
            //TODO: cambia los 0's por i, lo deje asi para que no hubiera desbordamiento
            addSlide(new SimpleSlide.Builder()
                    //TODO: si quieres cambiar el color de fondo de cada uno, sientete libre, igual metelos en un arreglo como los anteriores
                    .title(titles[0])
                    .description(descriptions[0])
                    .image(images[i])
                    .background(R.color.secondaryColor)
                    .backgroundDark(R.color.secondaryDarkColor)
                    .build());
        }

    }

}
