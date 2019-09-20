package com.example.sulemaia.Helper;

import android.text.InputFilter;
import android.text.Spanned;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
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
        int[][] mapValues;
        int i = 0, j = 0;
        String[] rows = fileDataUnFiltered.split("(\n|\n\r|\r\n|\r)");
        mapValues = new int[rows.length][rows[0].split(",").length];
        for (String line: rows) {
            String[] numbers = line.split(",");
            for (String number: numbers) {
                mapValues[i][j] = Integer.parseInt(number);
                j++;
            }
            j=0;
            i++;
        }
        return mapValues;
    }

    public static class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }

    }
}
