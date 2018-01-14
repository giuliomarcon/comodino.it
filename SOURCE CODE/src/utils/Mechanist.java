package utils;

import java.lang.reflect.Array;

public class Mechanist {
    //checkSingleMapParameter
    public static String checkSMP(Object obj){
        if(obj == null)
            return null;
        if(obj.getClass() == String[].class){
            if(((String[])obj).length == 1) {
                return ((String[]) obj)[0];
            }
            else {
                System.out.println("Mechanist: mi aspettavo una paramentro singolo e mi hai deluso" );
            }
        }
        return null;
    }

    public static String[] getMMP(Object obj){
        if(obj == null)
            return null;
        if(obj.getClass() == String[].class){
            if(((String[])obj).length > 0) {
                return (String[]) obj;
            }
        }
        return null;
    }

    public static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
