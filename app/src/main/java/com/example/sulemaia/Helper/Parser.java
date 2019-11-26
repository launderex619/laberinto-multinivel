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

/**
 * Class for the correct parsing of the txt that the user tries
 * to load into de app.
 */
public class Parser {
    public static final int FULL_FILE_IS_CORRECT = 0;
    public static final int INCONSISTENT_ID_QUANTITY = 1;
    public static final int INVALID_FILE_CONTENT = 2;
    public static final int TOO_MANY_COLUMNS_OR_ROWS = 4;

    /**
     * We receive a whole document (whichever the user chooses, so be careful with this;
     * it means this can load anything, like photos or documents, but is only intended
     * to load incredibly light txt files that go along the project specifications. This
     * includes too many columns, rows, data amount inconsistency, or directly invalid
     * content.
     * @param fileContent The whole file that was actually selected by the user.
     * @return any code that represent either a failure or a correct archive.
     */
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
                    return INCONSISTENT_ID_QUANTITY;
                }
                numbers = line.split(",");
                if (numbers.length > 15){
                    return TOO_MANY_COLUMNS_OR_ROWS;
                }
                if (total == 0)
                    total = numbers.length;
                localTotal = numbers.length;

                if (total != localTotal)
                    return INCONSISTENT_ID_QUANTITY;
                ///Numero inconsistente de datos entre cada fila.
                for (String a : numbers) {
                    match = regPattern.matcher(a);
                    if (!match.matches()) {
                        return INVALID_FILE_CONTENT;
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
                return TOO_MANY_COLUMNS_OR_ROWS;   //Too many rows
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FULL_FILE_IS_CORRECT;
        ///txt correcto.
    }

    /**
     * Get every number into an array, to process later on.
     * @param fileDataUnFiltered the raw file to upload to the program, that
     *                           should have any errors, since those are looked for
     *                           in the parsing method.
     * @return the map values (numeric codes) of the read txt file.
     */
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

    /**
     * Method to return a letter based on a code to create the first row in the map.
     * @param j the position on the alphabet.
     * @return either a blank space (for the fist row - which relates to the first
     * column and shouldnt have anythin) or the corresponding letter.
     */
    public static String getLetterForInt(int j) {
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};

        if(j == 0){
            return "";
        }
        else{
            return letters[j-1];
        }
    }

    /**
     * Method to adapt the sizes of the map depending on how many tiles the map has.
     * @param length a base length to process the new calculation.
     * @return the maxSize.
     */
    public static float getTextSizeForMap(int length) {
        float maxSize = 40f;
        float maxLength = 17f;

        return maxSize -((maxSize /maxLength) * (--length));
    }

    /**
     * The regrex formula to control the nodes weights that can written down by the user.
     */
    public static class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern, nPattern;

        /**
         * Actual regex formula.
         * @param digitsBeforeZero digit quantity able to be put before the dot.
         * @param digitsAfterZero digit quantity able to be put after the dot.
         */
        public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            //mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
            mPattern = Pattern.compile("([0-9]{0," + (digitsBeforeZero) + "})(\\.[0-9]{0," + (digitsAfterZero-1)  + "})?");
        }

        /**
         * Filter to check for a correct pattern after the initial regex.
         * @param source initial char sequence.
         * @param start start of the sequence.
         * @param end enf of the sequence.
         * @param dest destiny
         * @param dstart destiny start
         * @param dend destiny end
         * @return return either a blank space or a null.
         */
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }

    }
}
