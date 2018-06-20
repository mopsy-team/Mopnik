package util;

import exceptions.ValidationError;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLowerCase;

public class Validator {
    public static int makeInt(Object o, String s) throws ValidationError {
        try {
            return Integer.parseInt(o.toString());
        } catch (java.lang.NumberFormatException e) {
            throw new ValidationError(s + " musi być liczbą całkowitą dodatnią");
        }
    }

    public static double makeDouble(Object d, String s) throws ValidationError {
        try {
            return Double.parseDouble(d.toString());
        }
        catch (java.lang.NumberFormatException e){
            throw new ValidationError(s + " musi być liczbą");
        }
    }

    public static double makeUnsignedDouble(Object d, String s) throws ValidationError {
        double dd = makeDouble(d, s);
        if (dd < 0) {
            throw new ValidationError(s + " musi być liczbą nieujemną");
        }
        return dd;
    }

    public static int makeRoadNumber(String ss) throws ValidationError {
        final String s = "Numer drogi";

        if (ss.isEmpty()) {
            throw new ValidationError(s + " nie może być pusty");
        }

        String[] strings = ss.split("/");
        ss = strings[strings.length - 1];
        int firstIndex = 0;
        int lastIndex = ss.length() - 1;

        char first = ss.charAt(firstIndex);
        char last = ss.charAt(lastIndex);

        if (first != 'S' && first != 'A' && !isDigit(first)) {
            throw new ValidationError(s + " musi zaczynać się literą A, literą S lub cyfrą");
        }
        if (!isLowerCase(last) && !isDigit(last)) {
            throw new ValidationError(s + " musi kończyć się małą literą lub cyfrą");
        }

        if (!isDigit(first)) {
            firstIndex++;
        }

        if(!isDigit(last)) {
            lastIndex--;
        }

//        System.out.println(ss);
        ss = ss.substring(firstIndex, lastIndex + 1);
//
//        System.out.println(ss);
//
//        System.out.println("");


        try {
            return Integer.parseInt(ss);
        } catch (java.lang.NumberFormatException e) {
            throw new ValidationError(s + " jest niepoprawny");
        }
    }
}
