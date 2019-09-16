package com.example.sulemaia.Helper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {
    public static final int mapValues[][] = {
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,},
            {0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 3},
            {0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0},
            {1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}};
    public static final String biomes[] = {
            "Tundra",
            "Desierto",
            "Taiga",
            "Selva",
            "Chaparral",
            "Arrecife de coral",
            "Rio",
            "Pastizales",
            "Océano",
            "Sabana",
            "Bosque templado",
            "Humedal",
            "Montaña",
            "Camino",
            "Bosque",
            "Lava",
            "Tonala"
    };

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL = 1;
    public static final int REQUEST_FILE_READ_EXTERNAL = 2;
    public static final int RESULT_FOR_FIELD_INFORMATION = 3;

    public static boolean isValidStringInFile(String fileContent) {
        //estoy pensando que quiza en lugar de regresar true o false deberia regresar un codigo de accion
        //de esta manera si no es valido podemos mostrar el mensaje dependiendo su codigo, por ejemplo
        //si es correcto regresamos 0, si es incorrecto vemos por que, si tiene letras, regresamos 1,
        //si tiene distinto numero de columnas regresamos 2, etc, etc, al final de cuentas estamos en
        //una clase helper que nos ayuda a verificar estos datos, simplemente creamos constantes como
        //un hashTable de errorCodes, y ya validamos mejor en la aplicacion, no se que opines.

        //pd, no es necesario que comentes linea por linea xd, solo bloques de codigo
        String line;
        String[] numbers;
        int result;
        int total = 0, localTotal = 0;
        boolean exists = false;
        ArrayList<Integer> codes = new ArrayList<Integer>();
        Pattern regPattern = Pattern.compile("\\d+");
        Matcher match;
        Reader stringReader = new StringReader(fileContent);
        try(BufferedReader bufferedReader = new BufferedReader(stringReader)) {
            while ((line = bufferedReader.readLine()) != null) {
                numbers = line.split(",");
                if (total == 0)
                    total = numbers.length;
                localTotal = numbers.length;

                if (total != localTotal)
                    return false;
                ///Numero inconsistente de datos.
                for (String a : numbers) {
                    match = regPattern.matcher(a);
                    if (!match.matches()) {
                        return false;
                    } else {
                        result = Integer.parseInt(a);
                        ///Revisamos que el numero leido no este en el arreglo de codigos y lo agregamos.
                        for (int x = 0; x < codes.size(); x++) {
                            if (result == codes.get(x)) {
                                exists = true;
                            }
                        }
                        if (exists == false) {
                            codes.add(result);
                        }
                    }
                    exists = false;
                }
            }
            for (int z = 0; z < codes.size(); z++) {
                System.out.println(codes.get(z));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static int[][] getFileArray(String fileDataUnFiltered) {
        //modifica esta funcion
        //vas a recibir exactamente la misma cadena que la funcion de arriba, pero ya tienes la certeza que es valida
        //solo crea un arreglo de enteros y lo debuelves
        return mapValues;
    }
}
