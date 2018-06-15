package util;

import exceptions.ValidationError;

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
}
