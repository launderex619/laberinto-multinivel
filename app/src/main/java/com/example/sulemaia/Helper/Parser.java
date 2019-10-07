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
    public static final int FULL_FILE_IS_CORRECT = 0;
    public static final int INCONSISTENT_ID_QUANTITY = 1;
    public static final int INVALID_FILE_CONTENT = 2;
    public static final int TOO_MANY_COLUMNS_OR_ROWS = 4;

    public static int isValidStringInFile(String fileContent) {
        String line;
        String[] numbers;
        int result;
        int total = 0, localTotal = 0, rows = 0;
        boolean exists = false;
        ArrayList<Integer> codes = new ArrayList<>();
        Pattern regPattern = Pattern.compile("\\d+");
        Matcher match;
        Reader stringReader = new StringReader(fileContent);
        try(BufferedReader bufferedReader = new BufferedReader(stringReader)) {
            while ((line = bufferedReader.readLine()) != null) {
                rows++;
                if(line.charAt(line.length()-1) == ','){
                    return 1;
                }
                numbers = line.split(",");
                if (numbers.length > 15){
                    return 4;   ///Too many columns.
                }
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
            if(rows > 15){
                return 4;   //Too many rows
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
        mapValues = new int[rows.length][rows[0].split(",").length ];
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

    public static String getLetterForInt(int j) {
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};

        if(j == 0){
            return "";
        }
        else{
            return letters[j-1];
        }
    }

    public static float getTextSizeForMap(int length) {
        float maxSize = 40f;
        float maxLength = 17f;

        return maxSize -((maxSize /maxLength) * (--length));
    }

    public static class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern, nPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            //mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
            mPattern = Pattern.compile("([0-9]{0," + (digitsBeforeZero) + "})(\\.[0-9]{0," + (digitsAfterZero-1)  + "})?");
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
