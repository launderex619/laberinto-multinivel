package test;
import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        
        System.out.println(parseFile());
    }
    
    public static boolean parseFile(){
        String fileName = "test.txt";
        // No estoy seguro de como pides la direccion del archivo en Android, jsjsjs
        //Como puedes imaginar, "test.txt" es un documento que esta en la raiz de mi proyecto.

        String line = null;
        String[] numbers;  ///Cada posible numero en cada linea leida.
        int result; ///Cada numero leido del texto.
        int total = 0, localTotal = 0;  ///Con esto revisaremos que todas las filas tengan siempre el mismo numero de elementos.
        boolean exists = false;
        ArrayList<Integer> codes = new ArrayList<Integer>();
        Pattern regPattern = Pattern.compile("\\d+");
        Matcher match;

        try {
            // FileReader lee el texto en el encoding por defecto.
            FileReader fileReader = 
                new FileReader(fileName);

            // Siempre encapsular Filereader en Bufferreader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                numbers = line.split(",");
                if(total == 0)
                    total = numbers.length;
                localTotal = numbers.length;
                
                if(total != localTotal)
                    return false;
                ///Numero inconsistente de datos.
                
                for(String a : numbers){
                    match = regPattern.matcher(a);
                    if(!match.matches()){
                        return false;
                    }else{
                        result = Integer.parseInt(a);
                        ///Revisamos que el numero leido no este en el arreglo de codigos y lo agregamos.
                        for(int x = 0; x<codes.size(); x++){
                            if(result == codes.get(x)){
                                exists = true;
                            }
                        }
                        if(exists == false){
                            codes.add(result);
                        }
                    }
                    exists = false;
                }
            }  

            //Nunca olvidemos cerrar el documento de lectura.
            bufferedReader.close();  
            
            for(int z = 0; z<codes.size(); z++){
                System.out.println(codes.get(z));
            }
            
            

        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Error al abrir el archivo '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error al leer el archivo '" 
                 + fileName + "'");                  
        }
        ///Si todo sale bien:
        return true;
    }
}
