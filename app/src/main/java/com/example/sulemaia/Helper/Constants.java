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

    public static int isValidStringInFile(String fileContent) {
        String line;
        String[] numbers;
        int result;
        int total = 0, localTotal = 0;
        boolean exists = false;
        ArrayList<Integer> codes = new ArrayList<>();
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
                    return 1;
                    ///Numero inconsistente de datos entre cada fila.
                for (String a : numbers) {
                    match = regPattern.matcher(a);
                    if (!match.matches()) {
                        return 2;
                        ///En lugar de un numero, tenemos un dato erroneo, e.g.:
                        ///float, char, negative, comas juntas.
                    } else {
                        result = Integer.parseInt(a);
                        ///Revisamos que el numero leido no este en el arreglo de codigos y lo agregamos.
                        for (int x = 0; x < codes.size(); x++) {
                            if (result == codes.get(x)) {
                                exists = true;
                            }
                        }
                        if (!exists) {
                            codes.add(result);
                        }
                    }
                    exists = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
        ///txt correcto.
    }

    public static int[][] getFileArray(String fileDataUnFiltered) {
        int[][] mapValues = new int[0][0];
        int row = 0;
        int size;
        String line;
        String[] numbers;
        Reader stringReader = new StringReader(fileDataUnFiltered);
        try (BufferedReader bufferedReader = new BufferedReader(stringReader)) {
            while ((line = bufferedReader.readLine()) != null) {
                numbers = line.split(",");
                size = numbers.length;
                int[] array = new int[size];
                for (int i = 0; i < size; i++) {
                    array[i] = Integer.parseInt(numbers[i]);
                }
                mapValues = new int[array.length][size];
                for (int y = 0; y < array.length; y++) {
                    mapValues[row][y] = array[y];
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapValues;
    }
}
