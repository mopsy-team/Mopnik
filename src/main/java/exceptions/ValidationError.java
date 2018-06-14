package exceptions;

import javax.swing.*;

public class ValidationError extends Exception{
    private String err;

    public ValidationError(String message){
        super(message);
        err = message;
    }

    public void alert(){
        JOptionPane.showMessageDialog(null, err);
    }

}
